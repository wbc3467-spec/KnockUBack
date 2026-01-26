package net.kairost.knockuback.mixin.invoker;

import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DamageSource.class)
public interface DamageSourceInvoker {
    @Invoker("setBypassesArmor")
    DamageSource invokeSetBypassesArmor();
}
