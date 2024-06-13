package ru.nightidk.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import ru.nightidk.DeathNote;
import ru.nightidk.configuration.ConfigVariables;
import ru.nightidk.jda.BaseEmbed;

import java.awt.*;
import java.io.IOException;
import java.util.stream.Collectors;

import static ru.nightidk.jda.MessageUtil.editStatusMessage;
import static ru.nightidk.utils.ChatMessageUtil.getStyledComponent;
import static ru.nightidk.utils.ChatMessageUtil.sendChatMessageToAll;

public class MaintanceCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("deathnote")
                        .then(
                                Commands.literal("maintains")
                                        .requires(s -> s.hasPermission(4))
                                        .then(
                                                Commands.literal("on")
                                                        .executes(context -> {
                                                            ConfigVariables.MAINTANCE = true;
                                                            try {
                                                                DeathNote.saveConfig(DeathNote.configFile);
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                            DeathNote.LOGGER.info("[DeathNote] Maintains on.");
                                                            sendChatMessageToAll(
                                                                    context.getSource().getServer().getPlayerList(),
                                                                    getStyledComponent("[Оповещение]", ChatFormatting.DARK_AQUA)
                                                                            .append(getStyledComponent(" Включён режим технических работ.", ChatFormatting.RED))
                                                            );
                                                            context.getSource().getServer().getPlayerList().getPlayers().forEach(s -> {
                                                                if (!s.hasPermissions(4))
                                                                    s.connection.disconnect(getStyledComponent("На сервере ведутся технические работы.", ChatFormatting.RED));
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
                                                Commands.literal("off")
                                                        .executes(context -> {
                                                            ConfigVariables.MAINTANCE = false;
                                                            try {
                                                                DeathNote.saveConfig(DeathNote.configFile);
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                            sendChatMessageToAll(
                                                                    context.getSource().getServer().getPlayerList(),
                                                                    getStyledComponent("[Оповещение]", ChatFormatting.DARK_AQUA)
                                                                            .append(getStyledComponent(" Отключён режим технических работ.", ChatFormatting.RED))
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
