package ru.nightidk.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import ru.nightidk.DeathNote;

public class ConfigReload {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("deathnote")
                        .then(
                                CommandManager.literal("reload")
                                        .requires(commandSourceStack -> commandSourceStack.hasPermissionLevel(4))
                                        .executes(context -> {
                                            DeathNote.loadConfig(DeathNote.configFile);
                                            context.getSource().sendMessage(Text.literal("[DeathNote] Config reloaded."));
                                            return 1;
                                        })
                        )
        );
    }
}
