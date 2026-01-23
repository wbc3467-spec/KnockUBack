package net.kairost.knockuback.mixin;

import net.minecraft.entity.LazyEntityReference;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.kairost.knockuback.config.ModConfig;


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    @Unique
    private int timeUntilRegenTmp;

    @Unique
    private int hurtTimeTmp;

    @Unique
    private float lastDamageTakenTmp;

    @Unique
    private int playerHitTimerTmp;

    @Unique
    private int knockubackCoolDown;

    @Unique
    private @Nullable LazyEntityReference<PlayerEntity> attackingPlayerTmp;

    @Unique
    private static final float EPSILON = Float.MIN_VALUE;

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
        if (ModConfig.INSTANCE.enabled && (source.getSource() instanceof SnowballEntity || source.getSource() instanceof EggEntity) && (source.getAttacker() instanceof PlayerEntity) && amount == 0.0f && this.knockubackCoolDown == 0) {
            amount = EPSILON;
        }
        return amount;
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void knockuback$tick(CallbackInfo info) {
        if (this.knockubackCoolDown > 0) {
            this.knockubackCoolDown--;
        }
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void knockuback$onDamage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable cir) {
        if (ModConfig.INSTANCE.enabled && (source.getSource() instanceof SnowballEntity || source.getSource() instanceof EggEntity) && (source.getAttacker() instanceof PlayerEntity)) {
            if (ModConfig.INSTANCE.allowCombo) {
                this.timeUntilRegenTmp = this.timeUntilRegen;
                this.timeUntilRegen = 0;
            }
            this.hurtTimeTmp = this.hurtTime;
            this.hurtTime = 0;
            this.lastDamageTakenTmp = this.lastDamageTaken;
            this.lastDamageTaken = 0;
            this.playerHitTimerTmp = this.playerHitTimer;
            this.playerHitTimer = 0;
            this.attackingPlayerTmp = this.attackingPlayer;
            this.attackingPlayer = null;
        }
    }

    @Inject(method = "damage", at = @At("RETURN"))
    private void knockuback$afterDamage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable cir) {
        if (ModConfig.INSTANCE.enabled && (source.getSource() instanceof SnowballEntity || source.getSource() instanceof EggEntity) && (source.getAttacker() instanceof PlayerEntity)) {
            if (ModConfig.INSTANCE.allowCombo) {
                this.timeUntilRegen = timeUntilRegenTmp;
                if (this.knockubackCoolDown == 0) {
                    this.knockubackCoolDown = ModConfig.INSTANCE.comboTick;
                }
            }

            this.hurtTime = this.hurtTimeTmp;
            this.lastDamageTaken = this.lastDamageTakenTmp;
            this.playerHitTimer = this.playerHitTimerTmp;
            this.attackingPlayer = this.attackingPlayerTmp;
        }
    }
}