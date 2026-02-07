package net.kairost.knockuback.config;

import net.neoforged.neoforge.common.ModConfigSpec;


public final class KnockUBackConfig {
    public static final KnockUBackConfig CONFIG;
    public static final ModConfigSpec SPEC;

    public final ModConfigSpec.BooleanValue enabled;
    public final ModConfigSpec.BooleanValue allowCombo;
    public final ModConfigSpec.IntValue comboTick;
    public final ModConfigSpec.BooleanValue damageArmor;
    public final ModConfigSpec.BooleanValue causeAggro;
    public final ModConfigSpec.BooleanValue allowAirHit;


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
            .define("allowCombo", false);

        comboTick = builder
            .comment("Combo tick window (0 = unlimited)")
            .translation("text.autoconfig.knockuback.option.comboTick")
            .defineInRange("comboTick", 0, 0, 100); // 0-100 ticks range

        damageArmor = builder
            .comment("Allow snowball hits damage armor")
            .translation("text.autoconfig.knockuback.option.damageArmor")
            .define("damageArmor", false);

        causeAggro = builder
            .comment("Snowball hits can aggro dogs of the player")
            .translation("text.autoconfig.knockuback.option.causeAggro")
            .define("causeAggro", true);

        allowAirHit = builder
            .comment("Players will update their speed in Y when hit in air by snowBalls")
            .translation("text.autoconfig.knockuback.option.allowAirHit")
            .define("allowAirHit", false);
    }
}