package net.kairost.knockuback;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.kairost.knockuback.config.KnockUBackConfig;

@Mod(KnockUBack.MODID)
public class KnockUBack {
    public static final String MODID = "knockuback";

    public KnockUBack(IEventBus modBus, ModContainer container) {
        ModLoadingContext.get().registerConfig(
            ModConfig.Type.CLIENT,
            KnockUBackConfig.SPEC
        );
    }
}