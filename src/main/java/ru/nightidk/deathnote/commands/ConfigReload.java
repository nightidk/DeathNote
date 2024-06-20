package ru.nightidk.deathnote.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import ru.nightidk.deathnote.DeathNote;
import ru.nightidk.deathnote.utils.ConfigUtils;

public class ConfigReload {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("deathnote")
                        .then(
                                CommandManager.literal("reload")
                                        .requires(commandSourceStack -> commandSourceStack.hasPermissionLevel(4))
                                        .executes(context -> {
                                            ConfigUtils.loadConfig(DeathNote.configFile);
                                            context.getSource().sendMessage(Text.literal("[DeathNote] Config reloaded."));
                                            return 1;
                                        })
                        )
        );
    }
}
