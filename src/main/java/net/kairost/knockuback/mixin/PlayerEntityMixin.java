package net.kairost.knockuback.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.kairost.knockuback.mixin.accessor.LivingEntityAccessor;
import net.kairost.knockuback.config.KnockUBackConfig;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin implements LivingEntityAccessor {
    @Unique
    private int knockUBack_forge$timeUntilRegenTmp;

    @Unique
    private float knockUBack_forge$lastDamageTakenTmp;

    @Unique
    private Player knockUBack_forge$attackingPlayerTmp;

    @Unique
    private int knockUBack_forge$playerHitTimerTmp;

    @Unique
    private LivingEntity knockUBack_forge$attackerTmp;

    @Unique
    private int knockUBack_forge$lastAttackedTimeTmp;

    @Unique
    private int knockUBack_forge$knockubackCoolDown;

    @Unique
    private boolean knockUBack_forge$onGroundTmp;

    @Unique
    private static final float knockUBack_forge$EPSILON = Float.MIN_VALUE;

    @Inject(method = "tick", at = @At("RETURN"))
    private void knockuback$tick(CallbackInfo info) {
        if (this.knockUBack_forge$knockubackCoolDown > 0) {
            this.knockUBack_forge$knockubackCoolDown--;
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
        if (KnockUBackConfig.CONFIG.enabled.get() && !KnockUBackConfig.CONFIG.damageArmor.get() && (source.getDirectEntity() instanceof Snowball || source.getDirectEntity() instanceof ThrownEgg) && (source.getEntity() instanceof Player)) {
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
        if (KnockUBackConfig.CONFIG.enabled.get() && (source.getDirectEntity() instanceof Snowball || source.getDirectEntity() instanceof ThrownEgg) && (source.getEntity() instanceof Player) && this.knockUBack_forge$knockubackCoolDown == 0) {
            amount = knockUBack_forge$EPSILON;
        }
        return amount;
    }


    @Inject(method = "hurtServer", at = @At("HEAD"))
    private void knockuback$onDamage(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable cir) {
        if (KnockUBackConfig.CONFIG.enabled.get() && (source.getDirectEntity() instanceof Snowball || source.getDirectEntity() instanceof ThrownEgg) && (source.getEntity() instanceof Player)) {
            if (KnockUBackConfig.CONFIG.allowCombo.get()) {
                this.knockUBack_forge$timeUntilRegenTmp = this.invulnerableTime;
                this.invulnerableTime = 0;
                this.knockUBack_forge$lastDamageTakenTmp = this.lastHurt;
                this.lastHurt = 0;
            }
            if (KnockUBackConfig.CONFIG.allowAirHit.get()) {
                this.knockUBack_forge$onGroundTmp = this.onGround();
                knockuback$setOnGround(true);
            }
            if (!KnockUBackConfig.CONFIG.causeAggro.get()) {
                this.knockUBack_forge$attackingPlayerTmp = this.lastHurtByPlayer;
                this.lastHurtByPlayer = null;
                this.knockUBack_forge$playerHitTimerTmp = this.lastHurtByPlayerTime;
                this.lastHurtByPlayerTime = 0;
                this.knockUBack_forge$attackerTmp = this.getLastHurtByMob();
                knockuback$setAttacker(null);
                this.knockUBack_forge$lastAttackedTimeTmp = this.getLastHurtByMobTimestamp();
                knockuback$setLastAttackedTime(0);
            }
        }
    }

    @Inject(method = "hurtServer", at = @At("RETURN"))
    private void knockuback$afterDamage(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable cir) {
        if (KnockUBackConfig.CONFIG.enabled.get() && (source.getDirectEntity() instanceof Snowball || source.getDirectEntity() instanceof ThrownEgg) && (source.getEntity() instanceof Player)) {
            if (KnockUBackConfig.CONFIG.allowCombo.get()) {
                this.invulnerableTime = knockUBack_forge$timeUntilRegenTmp;
                this.lastHurt = this.knockUBack_forge$lastDamageTakenTmp;
                if (this.knockUBack_forge$knockubackCoolDown == 0) {
                    this.knockUBack_forge$knockubackCoolDown = KnockUBackConfig.CONFIG.comboTick.get();
                }
            }
            if (KnockUBackConfig.CONFIG.allowAirHit.get()) {
                knockuback$setOnGround(this.knockUBack_forge$onGroundTmp);
            }
            if (!KnockUBackConfig.CONFIG.causeAggro.get()) {
                this.lastHurtByPlayerTime = this.knockUBack_forge$playerHitTimerTmp;
                this.lastHurtByPlayer = this.knockUBack_forge$attackingPlayerTmp;
                knockuback$setAttacker(this.knockUBack_forge$attackerTmp);
                knockuback$setLastAttackedTime(this.knockUBack_forge$lastAttackedTimeTmp);
            }
        }
    }
}
