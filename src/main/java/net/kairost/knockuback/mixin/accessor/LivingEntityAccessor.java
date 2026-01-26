package net.kairost.knockuback.mixin.accessor;

import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor extends EntityAccessor {
    @Accessor("attackerReference")
    @Nullable LazyEntityReference<LivingEntity> knockuback$getAttackerReference();

    @Accessor("attackerReference")
    void knockuback$setAttackerReference(@Nullable LazyEntityReference<LivingEntity> attackerReference);

    @Accessor("lastAttackedTime")
    void knockuback$setLastAttackedTime(int lastAttackedTime);
}
