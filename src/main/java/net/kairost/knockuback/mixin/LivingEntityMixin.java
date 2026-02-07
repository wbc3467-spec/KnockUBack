package net.kairost.knockuback.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin{
    @Shadow
    public abstract int getLastHurtByMobTimestamp();

    @Shadow
    public abstract @Nullable LivingEntity getLastHurtByMob();

    @Shadow
    protected int lastHurtByPlayerTime;
    @Shadow
    @Nullable
    protected Player lastHurtByPlayer;
    @Shadow
    protected float lastHurt;
}
