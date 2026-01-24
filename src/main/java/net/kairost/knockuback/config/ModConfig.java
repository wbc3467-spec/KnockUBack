package net.kairost.knockuback.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.kairost.knockuback.KnockUBack;


@Config(name = KnockUBack.MODID)
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Excluded
    public static ModConfig INSTANCE;

    public boolean enabled = true;

    public boolean allowCombo = false;

    public int comboTick = 0;

    public boolean damageArmor = false;

    public boolean causeAggro = true;

    public boolean allowAirHit = false;

    public static void init()
    {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}