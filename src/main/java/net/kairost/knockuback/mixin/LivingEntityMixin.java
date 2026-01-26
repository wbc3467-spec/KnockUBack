package net.kairost.knockuback.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin{
    @Shadow
    public abstract int getLastAttackedTime();

    @Shadow
    public abstract @Nullable LivingEntity getAttacker();

    @Shadow
    protected int playerHitTimer;
    @Shadow
    @Nullable
    protected PlayerEntity attackingPlayer;
    @Shadow
    protected float lastDamageTaken;
}
