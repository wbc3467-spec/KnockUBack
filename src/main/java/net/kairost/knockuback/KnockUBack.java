package net.kairost.knockuback;

import net.fabricmc.api.ModInitializer;
import net.kairost.knockuback.command.ModCommands;
import net.kairost.knockuback.config.ModConfig;

public class KnockUBack implements ModInitializer {
    public static final String MODID = "knockuback";

    @Override
    public void onInitialize() {
        ModConfig.init();
        ModCommands.register();
    }
}