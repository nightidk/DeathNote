package ru.nightidk.listeners;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.restaction.WebhookAction;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.entity.EntityTypeTest;
import org.apache.commons.lang3.StringUtils;
import ru.nightidk.DeathNote;
import ru.nightidk.configuration.ConfigVariables;
import ru.nightidk.jda.BaseEmbed;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static ru.nightidk.jda.MessageUtil.editStatusMessage;
import static ru.nightidk.jda.MessageUtil.sendChatMessage;
import static ru.nightidk.utils.ChatMessageUtil.*;

public class ModEventListener {
    @Setter
    protected static int tickClean = 36000;
    @Getter
    @Setter
    protected static int tickRestart = -1;
    public static int tickForPlannedRestart = 0;

    @SuppressWarnings("unchecked")
    public static void cleanItemsTickEvent(MinecraftServer server) {

        tickClean--;
        if (tickClean == 6000) sendChatMessageToAll(server.getPlayerList(), getStyledComponent("[Оповещение]", ChatFormatting.DARK_AQUA).append(getStyledComponent(" 5 минут до очистки предметов.", ChatFormatting.WHITE)));
        if (tickClean == 1200) sendChatMessageToAll(server.getPlayerList(), getStyledComponent("[Оповещение]", ChatFormatting.DARK_AQUA).append(getStyledComponent(" 1 минута до очистки предметов.", ChatFormatting.WHITE)));
        if (tickClean == 600) sendChatMessageToAll(server.getPlayerList(), getStyledComponent("[Оповещение]", ChatFormatting.DARK_AQUA).append(getStyledComponent(" 30 секунд до очистки предметов.", ChatFormatting.WHITE)));
        if (tickClean == 200) sendChatMessageToAll(server.getPlayerList(), getStyledComponent("[Оповещение]", ChatFormatting.DARK_AQUA).append(getStyledComponent(" 10 секунд до очистки предметов.", ChatFormatting.WHITE)));
        if (tickClean <= 100 && tickClean != 0 && tickClean % 20 == 0)
            sendChatMessageToAll(
                    server.getPlayerList(),
                    getStyledComponent("[Оповещение]", ChatFormatting.DARK_AQUA)
                            .append(getStyledComponent(" " + tickClean / 20 + " " + getWordInDeclension(tickClean / 20, List.of("секунда", "секунды", "секунд")) + " до очистки предметов.", ChatFormatting.WHITE))
            );
        if (tickClean > 0) return;
        ServerLevel level = server.overworld();
        List<ItemEntity> entityList = (List<ItemEntity>) level.getEntities(EntityTypeTest.forClass(ItemEntity.class), (i) -> true);
        sendChatMessageToAll(
                server.getPlayerList(),
                getStyledComponent("[Оповещение]", ChatFormatting.DARK_AQUA)
                        .append(getStyledComponent(" Очищено " + entityList.size() + " " + getWordInDeclension(entityList.size(), List.of("предмет", "предмета", "предметов")) + ".", ChatFormatting.WHITE))
        );
        DeathNote.LOGGER.info("[DeathNote] Очищено {} {}.", entityList.size(), getWordInDeclension(entityList.size(), List.of("предмет", "предмета", "предметов")));
        entityList.forEach(Entity::discard);
        tickClean = ConfigVariables.TICK_FOR_CLEAN;
    }

    public static void restartServerTickEvent(MinecraftServer server) {
        if (tickRestart == -1) return;



        tickRestart--;


        int seconds = tickRestart / 20;
        int minutes = seconds / 60;
        int hours = minutes / 60;

        if (hours != 0) return;

        if (minutes > 0 && (minutes % 10 == 0 || minutes == 1 || minutes == 5) && tickRestart % 20 == 0) {
            sendChatMessageToAll(
                    server.getPlayerList(),
                    getStyledComponent("[Оповещение]", ChatFormatting.DARK_AQUA)
                            .append(getStyledComponent(" Перезагрузка сервера через " + minutes + " " + getWordInDeclension(minutes, List.of("минута", "минуты", "минут")) + ".", ChatFormatting.WHITE))
            );
            return;
        }
        if (seconds == 30 && tickRestart % 20 == 0) {
            sendChatMessageToAll(
                    server.getPlayerList(),
                    getStyledComponent("[Оповещение]", ChatFormatting.DARK_AQUA)
                            .append(getStyledComponent(" Перезагрузка сервера через 30 секунд.", ChatFormatting.WHITE))
            );
            return;
        }
        if (seconds <= 10 && seconds % 5 == 0 && tickRestart % 20 == 0 && seconds > 5) {
            sendChatMessageToAll(
                    server.getPlayerList(),
                    getStyledComponent("[Оповещение]", ChatFormatting.DARK_AQUA)
                            .append(getStyledComponent(" Перезагрузка сервера через " + seconds + " " + getWordInDeclension(seconds, List.of("секунду", "секунды", "секунд")) + ".", ChatFormatting.WHITE))
            );
            return;
        }

        if (seconds <= 5 && seconds != 0 && tickRestart % 20 == 0) {
            sendChatMessageToAll(
                    server.getPlayerList(),
                    getStyledComponent("[Оповещение]", ChatFormatting.DARK_AQUA)
                            .append(getStyledComponent(" Перезагрузка сервера через " + seconds + " " + getWordInDeclension(seconds, List.of("секунду", "секунды", "секунд")) + ".", ChatFormatting.WHITE))
            );
            return;
        }

        if (tickRestart == 0) {
            tickRestart = -1;
            server.getPlayerList().getPlayers().forEach(serverPlayer -> serverPlayer.connection.disconnect(getStyledComponent("Перезагрузка сервера...", ChatFormatting.DARK_AQUA)));
            server.halt(false);
        }
    }

    public static void serverStoppingEvent(MinecraftServer server) {
        TextChannel textChannel = DeathNote.jda.getTextChannelById("1250591961988206652");
        if (textChannel != null) {
            textChannel.editMessageById(ConfigVariables.DISCORD_STATUS_MESSAGE, new MessageEditBuilder()
                    .setEmbeds(
                            new BaseEmbed(
                                    "Информация об сервере",
                                    "Статус: Выключается",
                                    new Color(255, 0, 0).getRGB(),
                                    new MessageEmbed.AuthorInfo("Империя \"von\"", null, null, null)
                            )
                    ).build()
            ).queue();
        }
    }

    public static void serverStoppedEvent(MinecraftServer server) throws InterruptedException {
        DeathNote.LOGGER.info( "[DeathNote] Restarting server..." );
        editStatusMessage(
            new MessageEditBuilder()
                .setEmbeds(
                        new BaseEmbed(
                                "Информация об сервере",
                                "Статус: Отключён",
                                new Color(255, 0, 0).getRGB(),
                                new MessageEmbed.AuthorInfo("Империя \"von\"", null, null, null)
                        )
                ).build()
        );
        DeathNote.LOGGER.info( "[DeathNote] JDA logout." );
        DeathNote.jda.shutdown();
        DeathNote.jda.awaitShutdown();
    }

    public static void joinServerEvent(ServerGamePacketListenerImpl serverGamePacketListener, MinecraftServer server) {
        if (ConfigVariables.MAINTANCE && !serverGamePacketListener.player.hasPermissions(4))
            serverGamePacketListener.disconnect(getStyledComponent("На сервере ведутся технические работы.", ChatFormatting.RED));
    }

    public static void onMessage(PlayerChatMessage playerChatMessage, ServerPlayer serverPlayer, ChatType.Bound bound) {
        if (!(serverPlayer instanceof ServerPlayer)) return;
        sendChatMessage("1250613189658411009", new MessageCreateBuilder().setContent(
                serverPlayer.getDisplayName().getString() + ": " + playerChatMessage.signedContent()
        ).build());
    }

    public static void serverStartedEvent(MinecraftServer server1) {
        editStatusMessage(
            new MessageEditBuilder()
                .setEmbeds(
                        new BaseEmbed(
                                "Информация об сервере",
                                "Статус: " + (ConfigVariables.MAINTANCE ? "Тех. работы" : "Работает"),
                                (ConfigVariables.MAINTANCE ? new Color(255, 136, 0).getRGB() : new Color(0, 255, 0).getRGB()),
                                new MessageEmbed.AuthorInfo("Империя \"von\"", null, null, null)
                        )
                ).build()
        );
        sendChatMessage("1250613189658411009", new MessageCreateBuilder().setContent(
                "[SYSTEM] Сервер запущен."
        ).build());
    }

    public static void joinedServerEvent(ServerGamePacketListenerImpl serverGamePacketListener, PacketSender packetSender, MinecraftServer server) {
        if (ConfigVariables.MAINTANCE) return;
        ServerPlayer serverPlayer = serverGamePacketListener.getPlayer();
        sendChatMessage("1250613189658411009", new MessageCreateBuilder().setContent(
                "[SYSTEM] " + serverPlayer.getName().getString() + " зашёл на сервер."
        ).build());
    }

    public static void disconnectServerEvent(ServerGamePacketListenerImpl serverGamePacketListener, MinecraftServer server) {
        if (ConfigVariables.MAINTANCE) return;
        ServerPlayer serverPlayer = serverGamePacketListener.getPlayer();
        sendChatMessage("1250613189658411009", new MessageCreateBuilder().setContent(
                "[SYSTEM] " + serverPlayer.getName().getString() + " вышел с сервера."
        ).build());
    }
}
