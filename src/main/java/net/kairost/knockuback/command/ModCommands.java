package net.kairost.knockuback.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import me.shedaniel.autoconfig.AutoConfig;
import net.kairost.knockuback.config.ModConfig;


public class ModCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess, environment) -> registerCommands(dispatcher)
        );
    }

    private static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("knockuback")
                .then(
                    Commands.literal("enabled")
                        .then(
                            Commands.literal("set")
                                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                                // OP-only
                                .then(
                                    Commands.argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            boolean value = BoolArgumentType.getBool(ctx, "value");
                                            return setEnabled(ctx.getSource(), value);
                                        })
                                )
                        )
                        .then(
                            Commands.literal("get")
                                .executes(ctx -> getEnabled(ctx.getSource()))
                        )
                )
                .then(
                    Commands.literal("allowCombo")
                        .then(
                            Commands.literal("set")
                                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                                .then(
                                    Commands.argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            boolean value = BoolArgumentType.getBool(ctx, "value");
                                            return setCombo(ctx.getSource(), value);
                                        })
                                )
                        )
                        .then(
                            Commands.literal("get")
                                .executes(ctx -> getCombo(ctx.getSource()))
                        )
                )
                .then(
                    Commands.literal("allowAirHit")
                        .then(
                            Commands.literal("set")
                                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                                .then(
                                    Commands.argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            boolean value = BoolArgumentType.getBool(ctx, "value");
                                            return setAirHit(ctx.getSource(), value);
                                        })
                                )
                        )
                        .then(
                            Commands.literal("get")
                                .executes(ctx -> getAirHit(ctx.getSource()))
                        )
                )
                .then(
                    Commands.literal("comboTick")
                        .then(
                            Commands.literal("set")
                                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                                .then(
                                    Commands.argument("value", IntegerArgumentType.integer(0))
                                        .executes(ctx -> {
                                            int value = IntegerArgumentType.getInteger(ctx, "value");
                                            return setComboTick(ctx.getSource(), value);
                                        })
                                )
                        )
                        .then(
                            Commands.literal("get")
                                .executes(ctx -> getComboTick(ctx.getSource()))
                        )
                )
                .then(
                    Commands.literal("damageArmor")
                        .then(
                            Commands.literal("set")
                                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                                .then(
                                    Commands.argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            boolean value = BoolArgumentType.getBool(ctx, "value");
                                            return setDamageArmor(ctx.getSource(), value);
                                        })
                                )
                        )
                        .then(
                            Commands.literal("get")
                                .executes(ctx -> getDamageArmor(ctx.getSource()))
                        )
                )
                .then(
                    Commands.literal("causeAggro")
                        .then(
                            Commands.literal("set")
                                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                                .then(
                                    Commands.argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            boolean value = BoolArgumentType.getBool(ctx, "value");
                                            return setAggro(ctx.getSource(), value);
                                        })
                                )
                        )
                        .then(
                            Commands.literal("get")
                                .executes(ctx -> getAggro(ctx.getSource()))
                        )
                )
        );
    }

    private static int setEnabled(CommandSourceStack source, boolean value) {
        ModConfig.INSTANCE.enabled = value;
        AutoConfig.getConfigHolder(ModConfig.class).save();

        source.sendSuccess(
            () -> Component.literal("KnockUBack enabled " + value),
            true
        );
        return 1;
    }

    private static int getEnabled(CommandSourceStack source) {
        boolean value = ModConfig.INSTANCE.enabled;

        source.sendSuccess(
            () -> Component.literal("KnockUBack enabled " + value),
            false
        );
        return 1;
    }

    private static int setCombo(CommandSourceStack source, boolean value) {
        ModConfig.INSTANCE.allowCombo = value;
        AutoConfig.getConfigHolder(ModConfig.class).save();

        source.sendSuccess(
            () -> Component.literal("KnockUBack allow Combo " + value),
            true
        );
        return 1;
    }

    private static int getCombo(CommandSourceStack source) {
        boolean value = ModConfig.INSTANCE.allowCombo;

        source.sendSuccess(
            () -> Component.literal("KnockUBack allow Combo " + value),
            false
        );
        return 1;
    }

    private static int setAirHit(CommandSourceStack source, boolean value) {
        ModConfig.INSTANCE.allowAirHit = value;
        AutoConfig.getConfigHolder(ModConfig.class).save();

        source.sendSuccess(
            () -> Component.literal("KnockUBack allow Air Hit " + value),
            true
        );
        return 1;
    }

    private static int getAirHit(CommandSourceStack source) {
        boolean value = ModConfig.INSTANCE.allowAirHit;

        source.sendSuccess(
            () -> Component.literal("KnockUBack allow Air Hit " + value),
            false
        );
        return 1;
    }

    private static int setComboTick(CommandSourceStack source, int value) {

        ModConfig.INSTANCE.comboTick = value;
        AutoConfig.getConfigHolder(ModConfig.class).save();

        source.sendSuccess(
            () -> Component.literal("KnockUBack Combo Tick " + value),
            true
        );
        return 1;
    }

    private static int getComboTick(CommandSourceStack source) {
        int value = ModConfig.INSTANCE.comboTick;

        source.sendSuccess(
            () -> Component.literal("KnockUBack Combo Tick " + value),
            false
        );
        return 1;
    }

    private static int setDamageArmor(CommandSourceStack source, boolean value) {
        ModConfig.INSTANCE.damageArmor = value;
        AutoConfig.getConfigHolder(ModConfig.class).save();

        source.sendSuccess(
            () -> Component.literal("KnockUBack damage Armor " + value),
            true
        );
        return 1;
    }

    private static int getDamageArmor(CommandSourceStack source) {
        boolean value = ModConfig.INSTANCE.damageArmor;

        source.sendSuccess(
            () -> Component.literal("KnockUBack damage Armor " + value),
            false
        );
        return 1;
    }

    private static int setAggro(CommandSourceStack source, boolean value) {
        ModConfig.INSTANCE.causeAggro = value;
        AutoConfig.getConfigHolder(ModConfig.class).save();

        source.sendSuccess(
            () -> Component.literal("KnockUBack cause Aggro " + value),
            true
        );
        return 1;
    }

    private static int getAggro(CommandSourceStack source) {
        boolean value = ModConfig.INSTANCE.causeAggro;

        source.sendSuccess(
            () -> Component.literal("KnockUBack cause Aggro " + value),
            false
        );
        return 1;
    }
}
