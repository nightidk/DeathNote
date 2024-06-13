package ru.nightidk.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.mixin.command.CommandManagerMixin;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import ru.nightidk.commands.arguments.PlayerArgumentType;

public class KillReasonCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, boolean dedicated) {
        dispatcher.register(
                Commands.literal("killreason")
                        .then(Commands.argument("at", PlayerArgumentType.reference())
                                .requires(commandSourceStack -> commandSourceStack.hasPermission(3))
                                .executes(cs -> {
                                    ServerPlayer player = cs.getArgument("at", ServerPlayer.class);
                                    player.kill();
                                    return 1;
                                })
                        )
        );
    }
}
