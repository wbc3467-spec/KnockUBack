package net.kairost.knockuback.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.DefaultPermissions;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import me.shedaniel.autoconfig.AutoConfig;
import net.kairost.knockuback.config.ModConfig;


public class ModCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess, environment) -> registerCommands(dispatcher)
        );
    }

    private static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("knockuback")
                .then(
                    CommandManager.literal("enabled")
                        .then(
                            CommandManager.literal("set")
                                .requires(source -> source.getPermissions().hasPermission(DefaultPermissions.MODERATORS))
                                // OP-only
                                .then(
                                    CommandManager.argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            boolean value = BoolArgumentType.getBool(ctx, "value");
                                            return setEnabled(ctx.getSource(), value);
                                        })
                                )
                        )
                        .then(
                            CommandManager.literal("get")
                                .executes(ctx -> getEnabled(ctx.getSource()))
                        )
                )
                .then(
                    CommandManager.literal("allowCombo")
                        .then(
                            CommandManager.literal("set")
                                .requires(source -> source.getPermissions().hasPermission(DefaultPermissions.MODERATORS))
                                .then(
                                    CommandManager.argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            boolean value = BoolArgumentType.getBool(ctx, "value");
                                            return setCombo(ctx.getSource(), value);
                                        })
                                )
                        )
                        .then(
                            CommandManager.literal("get")
                                .executes(ctx -> getCombo(ctx.getSource()))
                        )
                )
                .then(
                    CommandManager.literal("allowAirHit")
                        .then(
                            CommandManager.literal("set")
                                .requires(source -> source.getPermissions().hasPermission(DefaultPermissions.MODERATORS))
                                .then(
                                    CommandManager.argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            boolean value = BoolArgumentType.getBool(ctx, "value");
                                            return setAirHit(ctx.getSource(), value);
                                        })
                                )
                        )
                        .then(
                            CommandManager.literal("get")
                                .executes(ctx -> getAirHit(ctx.getSource()))
                        )
                )
                .then(
                    CommandManager.literal("comboTick")
                        .then(
                            CommandManager.literal("set")
                                .requires(source -> source.getPermissions().hasPermission(DefaultPermissions.MODERATORS))
                                .then(
                                    CommandManager.argument("value", IntegerArgumentType.integer(0))
                                        .executes(ctx -> {
                                            int value = IntegerArgumentType.getInteger(ctx, "value");
                                            return setComboTick(ctx.getSource(), value);
                                        })
                                )
                        )
                        .then(
                            CommandManager.literal("get")
                                .executes(ctx -> getComboTick(ctx.getSource()))
                        )
                )
                .then(
                    CommandManager.literal("damageArmor")
                        .then(
                            CommandManager.literal("set")
                                .requires(source -> source.getPermissions().hasPermission(DefaultPermissions.MODERATORS))
                                .then(
                                    CommandManager.argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            boolean value = BoolArgumentType.getBool(ctx, "value");
                                            return setDamageArmor(ctx.getSource(), value);
                                        })
                                )
                        )
                        .then(
                            CommandManager.literal("get")
                                .executes(ctx -> getDamageArmor(ctx.getSource()))
                        )
                )
                .then(
                    CommandManager.literal("causeAggro")
                        .then(
                            CommandManager.literal("set")
                                .requires(source -> source.getPermissions().hasPermission(DefaultPermissions.MODERATORS))
                                .then(
                                    CommandManager.argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            boolean value = BoolArgumentType.getBool(ctx, "value");
                                            return setAggro(ctx.getSource(), value);
                                        })
                                )
                        )
                        .then(
                            CommandManager.literal("get")
                                .executes(ctx -> getAggro(ctx.getSource()))
                        )
                )
        );
    }

    private static int setEnabled(ServerCommandSource source, boolean value) {
        ModConfig.INSTANCE.enabled = value;
        AutoConfig.getConfigHolder(ModConfig.class).save();

        source.sendFeedback(
            () -> Text.literal("KnockUBack enabled " + value),
            true
        );
        return 1;
    }

    private static int getEnabled(ServerCommandSource source) {
        boolean value = ModConfig.INSTANCE.enabled;

        source.sendFeedback(
            () -> Text.literal("KnockUBack enabled " + value),
            false
        );
        return 1;
    }

    private static int setCombo(ServerCommandSource source, boolean value) {
        ModConfig.INSTANCE.allowCombo = value;
        AutoConfig.getConfigHolder(ModConfig.class).save();

        source.sendFeedback(
            () -> Text.literal("KnockUBack allow Combo " + value),
            true
        );
        return 1;
    }

    private static int getCombo(ServerCommandSource source) {
        boolean value = ModConfig.INSTANCE.allowCombo;

        source.sendFeedback(
            () -> Text.literal("KnockUBack allow Combo " + value),
            false
        );
        return 1;
    }

    private static int setAirHit(ServerCommandSource source, boolean value) {
        ModConfig.INSTANCE.allowAirHit = value;
        AutoConfig.getConfigHolder(ModConfig.class).save();

        source.sendFeedback(
            () -> Text.literal("KnockUBack allow Air Hit " + value),
            true
        );
        return 1;
    }

    private static int getAirHit(ServerCommandSource source) {
        boolean value = ModConfig.INSTANCE.allowAirHit;

        source.sendFeedback(
            () -> Text.literal("KnockUBack allow Air Hit " + value),
            false
        );
        return 1;
    }

    private static int setComboTick(ServerCommandSource source, int value) {

        ModConfig.INSTANCE.comboTick = value;
        AutoConfig.getConfigHolder(ModConfig.class).save();

        source.sendFeedback(
            () -> Text.literal("KnockUBack Combo Tick " + value),
            true
        );
        return 1;
    }

    private static int getComboTick(ServerCommandSource source) {
        int value = ModConfig.INSTANCE.comboTick;

        source.sendFeedback(
            () -> Text.literal("KnockUBack Combo Tick " + value),
            false
        );
        return 1;
    }

    private static int setDamageArmor(ServerCommandSource source, boolean value) {
        ModConfig.INSTANCE.damageArmor = value;
        AutoConfig.getConfigHolder(ModConfig.class).save();

        source.sendFeedback(
            () -> Text.literal("KnockUBack damage Armor " + value),
            true
        );
        return 1;
    }

    private static int getDamageArmor(ServerCommandSource source) {
        boolean value = ModConfig.INSTANCE.damageArmor;

        source.sendFeedback(
            () -> Text.literal("KnockUBack damage Armor " + value),
            false
        );
        return 1;
    }

    private static int setAggro(ServerCommandSource source, boolean value) {
        ModConfig.INSTANCE.causeAggro = value;
        AutoConfig.getConfigHolder(ModConfig.class).save();

        source.sendFeedback(
            () -> Text.literal("KnockUBack cause Aggro " + value),
            true
        );
        return 1;
    }

    private static int getAggro(ServerCommandSource source) {
        boolean value = ModConfig.INSTANCE.causeAggro;

        source.sendFeedback(
            () -> Text.literal("KnockUBack cause Aggro " + value),
            false
        );
        return 1;
    }
}
