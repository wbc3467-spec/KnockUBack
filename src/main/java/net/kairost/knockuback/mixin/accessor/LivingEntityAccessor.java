package net.kairost.knockuback.mixin.accessor;

import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor extends EntityAccessor {
    @Accessor("lastHurtByMob")
    @Nullable EntityReference<LivingEntity> knockuback$getAttackerReference();

    @Accessor("lastHurtByMob")
    void knockuback$setAttackerReference(@Nullable EntityReference<LivingEntity> attackerReference);

    @Accessor("lastHurtByMobTimestamp")
    void knockuback$setLastAttackedTime(int lastAttackedTime);
}
