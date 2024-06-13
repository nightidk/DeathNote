package ru.nightidk.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import ru.nightidk.DeathNote;

public class ConfigReload {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("deathnote")
                        .then(
                                Commands.literal("reload")
                                        .requires(commandSourceStack -> commandSourceStack.hasPermission(4))
                                        .executes(context -> {
                                            DeathNote.loadConfig(DeathNote.configFile);
                                            context.getSource().sendSystemMessage(Component.literal("[DeathNote] Config reloaded."));
                                            return 1;
                                        })
                        )
        );
    }
}
