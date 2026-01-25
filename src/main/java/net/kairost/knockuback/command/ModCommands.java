package net.kairost.knockuback.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.kairost.knockuback.config.KnockUBackConfig;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class ModCommands {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();

        dispatcher.register(
            Commands.literal("knockuback")
                .then(
                    Commands.literal("enabled")
                        .then(
                            Commands.literal("set")
                                // Permission level 2 = moderator / function-op level (adjust as needed)
                                .requires(source -> source.hasPermission(2))
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
                                .requires(source -> source.hasPermission(2)) // OP-only
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
                    Commands.literal("comboTick")
                        .then(
                            Commands.literal("set")
                                .requires(source -> source.hasPermission(2)) // OP-only
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
        );
    }


    private static int setEnabled(CommandSourceStack source, boolean value) {
        KnockUBackConfig.CONFIG.enabled.set(value);
        KnockUBackConfig.SPEC.save();

        source.sendSuccess(
            () -> Component.literal("KnockUBack enabled " + value),
            true
        );
        return 1;
    }

    private static int getEnabled(CommandSourceStack source) {
        boolean value = KnockUBackConfig.CONFIG.enabled.get();

        source.sendSuccess(
            () -> Component.literal("KnockUBack enabled " + value),
            false
        );
        return 1;
    }

    private static int setCombo(CommandSourceStack source, boolean value) {
        KnockUBackConfig.CONFIG.allowCombo.set(value);
        KnockUBackConfig.SPEC.save();

        source.sendSuccess(
            () -> Component.literal("KnockUBack allow Combo " + value),
            true
        );
        return 1;
    }

    private static int getCombo(CommandSourceStack source) {
        boolean value = KnockUBackConfig.CONFIG.allowCombo.get();

        source.sendSuccess(
            () -> Component.literal("KnockUBack allow Combo " + value),
            false
        );
        return 1;
    }

    private static int setComboTick(CommandSourceStack source, int value) {

        KnockUBackConfig.CONFIG.comboTick.set(value);
        KnockUBackConfig.SPEC.save();

        source.sendSuccess(
            () -> Component.literal("KnockUBack Combo Tick " + value),
            true
        );
        return 1;
    }

    private static int getComboTick(CommandSourceStack source) {
        int value = KnockUBackConfig.CONFIG.comboTick.get();

        source.sendSuccess(
            () -> Component.literal("KnockUBack Combo Tick " + value),
            false
        );
        return 1;
    }
}
