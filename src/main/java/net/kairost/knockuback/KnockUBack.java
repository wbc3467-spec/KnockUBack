package net.kairost.knockuback;

import net.kairost.knockuback.command.ModCommands;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.kairost.knockuback.config.KnockUBackConfig;
import net.neoforged.neoforge.common.NeoForge;

@Mod(KnockUBack.MODID)
public class KnockUBack {
    public static final String MODID = "knockuback";

    public KnockUBack(IEventBus modBus, ModContainer container) {
        container.registerConfig(
            ModConfig.Type.CLIENT,
            KnockUBackConfig.SPEC
        );
        NeoForge.EVENT_BUS.addListener(ModCommands::registerCommands);
    }
}