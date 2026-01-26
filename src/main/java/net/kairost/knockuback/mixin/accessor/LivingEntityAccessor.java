package net.kairost.knockuback.mixin.accessor;

import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Accessor("attacker")
    void knockuback$setAttacker(@Nullable LivingEntity attacker);

    @Accessor("lastAttackedTime")
    void knockuback$setLastAttackedTime(int lastAttackedTime);
}
