package net.kairost.knockuback.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EntityReference;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin{
    @Shadow
    protected int lastHurtByPlayerMemoryTime;
    @Shadow
    @Nullable
    protected EntityReference<Player> lastHurtByPlayer;
    @Shadow
    protected float lastHurt;
    @Shadow
    public int hurtTime;
}
