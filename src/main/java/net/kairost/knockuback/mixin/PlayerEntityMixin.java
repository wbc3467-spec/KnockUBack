package net.kairost.knockuback.mixin;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.kairost.knockuback.config.ModConfig;
import net.kairost.knockuback.mixin.accessor.LivingEntityAccessor;


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin implements LivingEntityAccessor {
    @Unique
    private int timeUntilRegenTmp;

    @Unique
    private float lastDamageTakenTmp;

    @Unique
    private PlayerEntity attackingPlayerTmp;

    @Unique
    private int playerHitTimerTmp;

    @Unique
    private LivingEntity attackerTmp;

    @Unique
    private int lastAttackedTimeTmp;

    @Unique
    private int knockubackCoolDown;

    @Unique
    private boolean onGroundTmp;

    @Unique
    private static final float EPSILON = Float.MIN_VALUE;

    @Inject(method = "tick", at = @At("RETURN"))
    private void knockuback$tick(CallbackInfo info) {
        if (this.knockubackCoolDown > 0) {
            this.knockubackCoolDown--;
        }
    }

    @ModifyVariable(
        method = "damageArmor",
        at = @At("HEAD"),
        argsOnly = true
    )
    private float knockuback$modifyDamageArmorAmount(
        float amount,
        DamageSource source
    ) {
        if (ModConfig.INSTANCE.enabled && !ModConfig.INSTANCE.damageArmor && (source.getSource() instanceof SnowballEntity || source.getSource() instanceof EggEntity) && (source.getAttacker() instanceof PlayerEntity)) {
            return 0f;
        }
        return amount;
    }

    @ModifyVariable(
        method = "damage",
        at = @At("HEAD"),
        argsOnly = true
    )
    private float knockuback$modifyAmount(
        float amount,
        ServerWorld world,
        DamageSource source
    ) {
        if (ModConfig.INSTANCE.enabled && (source.getSource() instanceof SnowballEntity || source.getSource() instanceof EggEntity) && (source.getAttacker() instanceof PlayerEntity) && this.knockubackCoolDown == 0) {
            amount = EPSILON;
        }
        return amount;
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void knockuback$onDamage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable cir) {
        if (ModConfig.INSTANCE.enabled && (source.getSource() instanceof SnowballEntity || source.getSource() instanceof EggEntity) && (source.getAttacker() instanceof PlayerEntity)) {
            if (ModConfig.INSTANCE.allowCombo) {
                this.timeUntilRegenTmp = this.timeUntilRegen;
                this.timeUntilRegen = 0;
                this.lastDamageTakenTmp = this.lastDamageTaken;
                this.lastDamageTaken = 0;
            }
            if (ModConfig.INSTANCE.allowAirHit) {
                this.onGroundTmp = this.isOnGround();
                knockuback$setOnGround(true);
            }
            if (!ModConfig.INSTANCE.causeAggro) {
                this.attackingPlayerTmp = this.attackingPlayer;
                this.attackingPlayer = null;
                this.playerHitTimerTmp = this.playerHitTimer;
                this.playerHitTimer = 0;
                this.attackerTmp = this.getAttacker();
                knockuback$setAttacker(null);
                this.lastAttackedTimeTmp = this.getLastAttackedTime();
                knockuback$setLastAttackedTime(0);
            }
        }
    }

    @Inject(method = "damage", at = @At("RETURN"))
    private void knockuback$afterDamage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable cir) {
        if (ModConfig.INSTANCE.enabled && (source.getSource() instanceof SnowballEntity || source.getSource() instanceof EggEntity) && (source.getAttacker() instanceof PlayerEntity)) {
            if (ModConfig.INSTANCE.allowCombo) {
                this.timeUntilRegen = timeUntilRegenTmp;
                this.lastDamageTaken = this.lastDamageTakenTmp;
                if (this.knockubackCoolDown == 0) {
                    this.knockubackCoolDown = ModConfig.INSTANCE.comboTick;
                }
            }
            if (ModConfig.INSTANCE.allowAirHit) {
                knockuback$setOnGround(this.onGroundTmp);
            }
            if (!ModConfig.INSTANCE.causeAggro) {
                this.playerHitTimer = this.playerHitTimerTmp;
                this.attackingPlayer = this.attackingPlayerTmp;
                knockuback$setAttacker(this.attackerTmp);
                knockuback$setLastAttackedTime(this.lastAttackedTimeTmp);
            }
        }
    }
}