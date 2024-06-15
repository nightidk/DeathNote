package ru.nightidk.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import ru.nightidk.DeathNote;
import ru.nightidk.configuration.ConfigVariables;
import ru.nightidk.jda.BaseEmbed;
import ru.nightidk.utils.ChatMessageUtil;
import ru.nightidk.utils.ConfigUtils;
import ru.nightidk.utils.TextStyleUtil;

import java.awt.*;
import java.io.IOException;

import static ru.nightidk.utils.JDAMessageUtil.editStatusMessage;
import static ru.nightidk.utils.ChatMessageUtil.getStyledComponent;
import static ru.nightidk.utils.ChatMessageUtil.sendChatMessageToAll;

public class MaintanceCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("deathnote")
                        .then(
                                CommandManager.literal("maintains")
                                        .requires(s -> s.hasPermissionLevel(4))
                                        .then(
                                                CommandManager.literal("on")
                                                        .executes(context -> {
                                                            ConfigVariables.MAINTANCE = true;
                                                            try {
                                                                ConfigUtils.saveConfig(DeathNote.configFile);
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                            DeathNote.LOGGER.info("[DeathNote] Maintains on.");
                                                            sendChatMessageToAll(
                                                                    context.getSource().getServer().getPlayerManager().getPlayerList(),
                                                                    getStyledComponent("[Оповещение]", TextStyleUtil.DARK_AQUA.getStyle())
                                                                            .append(getStyledComponent(" Включён режим технических работ.", TextStyleUtil.RED.getStyle())),
                                                                    ChatMessageUtil.MessageType.NOTIFY
                                                            );
                                                            context.getSource().getServer().getPlayerManager().getPlayerList().forEach(s -> {
                                                                if (!s.hasPermissionLevel(4))
                                                                    s.networkHandler.disconnect(getStyledComponent("На сервере ведутся технические работы.", TextStyleUtil.RED.getStyle()));
                                                            });

                                                            editStatusMessage(
                                                                new MessageEditBuilder()
                                                                    .setEmbeds(
                                                                            new BaseEmbed(
                                                                                    "Информация об сервере",
                                                                                    "Статус: Тех.работы",
                                                                                    new Color(255, 136, 0).getRGB(),
                                                                                    new MessageEmbed.AuthorInfo("Империя \"von\"", null, null, null)
                                                                            )
                                                                    ).build()
                                                            );
                                                            return 1;
                                                        })
                                        )
                                        .then(
                                                CommandManager.literal("off")
                                                        .executes(context -> {
                                                            ConfigVariables.MAINTANCE = false;
                                                            try {
                                                                ConfigUtils.saveConfig(DeathNote.configFile);
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                            sendChatMessageToAll(
                                                                    context.getSource().getServer().getPlayerManager().getPlayerList(),
                                                                    getStyledComponent("[Оповещение]", TextStyleUtil.DARK_AQUA.getStyle())
                                                                            .append(getStyledComponent(" Отключён режим технических работ.", TextStyleUtil.RED.getStyle())),
                                                                    ChatMessageUtil.MessageType.NOTIFY
                                                            );
                                                            DeathNote.LOGGER.info("[DeathNote] Maintains off.");
                                                            editStatusMessage(
                                                                new MessageEditBuilder()
                                                                    .setEmbeds(
                                                                            new BaseEmbed(
                                                                                    "Информация об сервере",
                                                                                    "Статус: Работает",
                                                                                    new Color(0, 255, 0).getRGB(),
                                                                                    new MessageEmbed.AuthorInfo("Империя \"von\"", null, null, null)
                                                                            )
                                                                    ).build()
                                                            );
                                                            return 1;
                                                        })
                                        )
                        )
        );
    }
}
