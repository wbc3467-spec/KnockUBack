package net.kairost.knockuback.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.kairost.knockuback.config.ModConfig;
import net.kairost.knockuback.mixin.accessor.LivingEntityAccessor;


@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin implements LivingEntityAccessor {
    @Unique
    private int timeUntilRegenTmp;

    @Unique
    private float lastDamageTakenTmp;

    @Unique
    private @Nullable EntityReference<Player> attackingPlayerTmp;

    @Unique
    private int playerHitTimerTmp;

    @Unique
    private @Nullable EntityReference<LivingEntity> attackerReferenceTmp;

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
        method = "hurtArmor",
        at = @At("HEAD"),
        argsOnly = true
    )
    private float knockuback$modifyDamageArmorAmount(
        float amount,
        DamageSource source
    ) {
        if (ModConfig.INSTANCE.enabled && !ModConfig.INSTANCE.damageArmor && (source.getDirectEntity() instanceof Snowball || source.getDirectEntity() instanceof ThrownEgg) && (source.getEntity() instanceof Player)) {
            return 0f;
        }
        return amount;
    }

    @ModifyVariable(
        method = "hurtServer",
        at = @At("HEAD"),
        argsOnly = true
    )
    private float knockuback$modifyAmount(
        float amount,
        ServerLevel world,
        DamageSource source
    ) {
        if (ModConfig.INSTANCE.enabled && (source.getDirectEntity() instanceof Snowball || source.getDirectEntity() instanceof ThrownEgg) && (source.getEntity() instanceof Player) && this.knockubackCoolDown == 0) {
            amount = EPSILON;
        }
        return amount;
    }

    @Inject(method = "hurtServer", at = @At("HEAD"))
    private void knockuback$onDamage(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable cir) {
        if (ModConfig.INSTANCE.enabled && (source.getDirectEntity() instanceof Snowball || source.getDirectEntity() instanceof ThrownEgg) && (source.getEntity() instanceof Player)) {
            if (ModConfig.INSTANCE.allowCombo) {
                this.timeUntilRegenTmp = this.invulnerableTime;
                this.invulnerableTime = 0;
                this.lastDamageTakenTmp = this.lastHurt;
                this.lastHurt = 0;
            }
            if (ModConfig.INSTANCE.allowAirHit) {
                this.onGroundTmp = this.onGround();
                knockuback$setOnGround(true);
            }
            if (!ModConfig.INSTANCE.causeAggro) {
                this.attackingPlayerTmp = this.lastHurtByPlayer;
                this.lastHurtByPlayer = null;
                this.playerHitTimerTmp = this.lastHurtByPlayerMemoryTime;
                this.lastHurtByPlayerMemoryTime = 0;
                this.attackerReferenceTmp = knockuback$getAttackerReference();
                knockuback$setAttackerReference(null);
                this.lastAttackedTimeTmp = this.getLastHurtByMobTimestamp();
                knockuback$setLastAttackedTime(0);
            }
        }
    }

    @Inject(method = "hurtServer", at = @At("RETURN"))
    private void knockuback$afterDamage(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable cir) {
        if (ModConfig.INSTANCE.enabled && (source.getDirectEntity() instanceof Snowball || source.getDirectEntity() instanceof ThrownEgg) && (source.getEntity() instanceof Player)) {
            if (ModConfig.INSTANCE.allowCombo) {
                this.invulnerableTime = timeUntilRegenTmp;
                this.lastHurt = this.lastDamageTakenTmp;
                if (this.knockubackCoolDown == 0) {
                    this.knockubackCoolDown = ModConfig.INSTANCE.comboTick;
                }
            }
            if (ModConfig.INSTANCE.allowAirHit) {
                knockuback$setOnGround(this.onGroundTmp);
            }
            if (!ModConfig.INSTANCE.causeAggro) {
                this.lastHurtByPlayerMemoryTime = this.playerHitTimerTmp;
                this.lastHurtByPlayer = this.attackingPlayerTmp;
                knockuback$setAttackerReference(this.attackerReferenceTmp);
                knockuback$setLastAttackedTime(this.lastAttackedTimeTmp);
            }
        }
    }
}