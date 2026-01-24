package net.kairost.knockuback.config;

import net.neoforged.neoforge.common.ModConfigSpec;


public final class KnockUBackConfig {
    public static final KnockUBackConfig CONFIG;
    public static final ModConfigSpec SPEC;

    public final ModConfigSpec.BooleanValue enabled;
    public final ModConfigSpec.BooleanValue allowCombo;
    public final ModConfigSpec.IntValue comboTick;

    static {
        var pair = new ModConfigSpec.Builder().configure(KnockUBackConfig::new);
        CONFIG = pair.getLeft();
        SPEC = pair.getRight();
    }

    KnockUBackConfig(ModConfigSpec.Builder builder) {
        enabled = builder
            .comment("Enable KnockUBack mod")
            .translation("text.autoconfig.knockuback.option.enabled") // Optional: for localization
            .define("enabled", true);

        allowCombo = builder
            .comment("Allow combo knockback (multiple hits)")
            .translation("text.autoconfig.knockuback.option.allowCombo")
            .define("allowCombo", true);

        comboTick = builder
            .comment("Combo tick window (0 = disabled/unlimited)")
            .translation("text.autoconfig.knockuback.option.comboTick")
            .defineInRange("comboTick", 0, 0, 100); // 0-100 ticks range
    }
}