package ru.nightidk.listeners;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypeFilter;
import net.minecraft.world.World;
import ru.nightidk.DeathNote;
import ru.nightidk.configuration.ConfigVariables;
import ru.nightidk.jda.BaseEmbed;
import ru.nightidk.utils.ChatMessageUtil;
import ru.nightidk.utils.TextStyleUtil;

import java.awt.*;
import java.util.List;

import static ru.nightidk.utils.AuthUtil.*;
import static ru.nightidk.utils.JDAMessageUtil.editStatusMessage;
import static ru.nightidk.utils.JDAMessageUtil.sendChatMessage;
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
        if (tickClean == 6000) sendChatMessageToAll(server.getPlayerManager().getPlayerList(), getStyledComponent("[Оповещение]", TextStyleUtil.DARK_AQUA.getStyle()).append(getStyledComponent(" 5 минут до очистки предметов.", TextStyleUtil.WHITE.getStyle())), ChatMessageUtil.MessageType.CLEAN);
        if (tickClean == 1200) sendChatMessageToAll(server.getPlayerManager().getPlayerList(), getStyledComponent("[Оповещение]", TextStyleUtil.DARK_AQUA.getStyle()).append(getStyledComponent(" 1 минута до очистки предметов.", TextStyleUtil.WHITE.getStyle())), ChatMessageUtil.MessageType.CLEAN);
        if (tickClean == 600) sendChatMessageToAll(server.getPlayerManager().getPlayerList(), getStyledComponent("[Оповещение]", TextStyleUtil.DARK_AQUA.getStyle()).append(getStyledComponent(" 30 секунд до очистки предметов.", TextStyleUtil.WHITE.getStyle())), ChatMessageUtil.MessageType.CLEAN);
        if (tickClean == 200) sendChatMessageToAll(server.getPlayerManager().getPlayerList(), getStyledComponent("[Оповещение]", TextStyleUtil.DARK_AQUA.getStyle()).append(getStyledComponent(" 10 секунд до очистки предметов.", TextStyleUtil.WHITE.getStyle())), ChatMessageUtil.MessageType.CLEAN);
        if (tickClean <= 100 && tickClean != 0 && tickClean % 20 == 0)
            sendChatMessageToAll(
                    server.getPlayerManager().getPlayerList(),
                    getStyledComponent("[Оповещение]", TextStyleUtil.DARK_AQUA.getStyle())
                            .append(getStyledComponent(" " + tickClean / 20 + " " + getWordInDeclension(tickClean / 20, List.of("секунда", "секунды", "секунд")) + " до очистки предметов.", TextStyleUtil.WHITE.getStyle())),
                    ChatMessageUtil.MessageType.CLEAN
            );
        if (tickClean > 0) return;
        ServerWorld level = server.getWorld(World.OVERWORLD);
        if (level == null) return;
        List<ItemEntity> entityList = (List<ItemEntity>) level.getEntitiesByType(TypeFilter.instanceOf(ItemEntity.class), (i) -> true);
        sendChatMessageToAll(
                server.getPlayerManager().getPlayerList(),
                getStyledComponent("[Оповещение]", TextStyleUtil.DARK_AQUA.getStyle())
                        .append(getStyledComponent(" Очищено " + entityList.size() + " " + getWordInDeclension(entityList.size(), List.of("предмет", "предмета", "предметов")) + ".", TextStyleUtil.WHITE.getStyle())),
                ChatMessageUtil.MessageType.CLEAN
        );
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

        if (minutes > 0 && (minutes % 10 == 0 || minutes == 1 || minutes == 5) && tickRestart % 20 == 0 && seconds >= 60 && seconds % 60 == 0) {
            sendChatMessageToAll(
                    server.getPlayerManager().getPlayerList(),
                    getStyledComponent("[Оповещение]", TextStyleUtil.DARK_AQUA.getStyle())
                            .append(getStyledComponent(" Перезагрузка сервера через " + minutes + " " + getWordInDeclension(minutes, List.of("минуту", "минуты", "минут")) + ".", TextStyleUtil.WHITE.getStyle())),
                    ChatMessageUtil.MessageType.RESTART
            );
            return;
        }

        if (seconds == 30 && tickRestart % 20 == 0) {
            sendChatMessageToAll(
                    server.getPlayerManager().getPlayerList(),
                    getStyledComponent("[Оповещение]", TextStyleUtil.DARK_AQUA.getStyle())
                            .append(getStyledComponent(" Перезагрузка сервера через 30 секунд.", TextStyleUtil.WHITE.getStyle())),
                    ChatMessageUtil.MessageType.RESTART
            );
            return;
        }
        if (seconds <= 10 && seconds % 5 == 0 && tickRestart % 20 == 0 && seconds > 5) {
            sendChatMessageToAll(
                    server.getPlayerManager().getPlayerList(),
                    getStyledComponent("[Оповещение]", TextStyleUtil.DARK_AQUA.getStyle())
                            .append(getStyledComponent(" Перезагрузка сервера через " + seconds + " " + getWordInDeclension(seconds, List.of("секунду", "секунды", "секунд")) + ".", TextStyleUtil.WHITE.getStyle())),
                    ChatMessageUtil.MessageType.RESTART
            );
            return;
        }

        if (seconds <= 5 && seconds != 0 && tickRestart % 20 == 0) {
            sendChatMessageToAll(
                    server.getPlayerManager().getPlayerList(),
                    getStyledComponent("[Оповещение]", TextStyleUtil.DARK_AQUA.getStyle())
                            .append(getStyledComponent(" Перезагрузка сервера через " + seconds + " " + getWordInDeclension(seconds, List.of("секунду", "секунды", "секунд")) + ".", TextStyleUtil.WHITE.getStyle())),
                    ChatMessageUtil.MessageType.RESTART
            );
            return;
        }

        if (tickRestart == 0) {
            tickRestart = -1;
            sendChatMessageToAll(
                    server.getPlayerManager().getPlayerList(),
                    getStyledComponent("[Оповещение]", TextStyleUtil.DARK_AQUA.getStyle())
                            .append(getStyledComponent(" Перезагрузка сервера...", TextStyleUtil.WHITE.getStyle())),
                    ChatMessageUtil.MessageType.NOTIFY
            );
            server.getPlayerManager().getPlayerList().forEach(serverPlayer -> serverPlayer.networkHandler.disconnect(getStyledComponent("Перезагрузка сервера...", TextStyleUtil.DARK_AQUA.getStyle())));
            server.stop(false);
        }
    }

    public static void serverStoppingEvent(MinecraftServer server) {
        if (DeathNote.jda == null) return;
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
        if (DeathNote.jda == null) return;
        DeathNote.LOGGER.info( "[DeathNote] JDA logout." );
        DeathNote.jda.shutdown();
        DeathNote.jda.awaitShutdown();
    }

    public static void joinServerEvent(ServerPlayNetworkHandler serverGamePacketListener, MinecraftServer server) {
        if (ConfigVariables.MAINTANCE && !serverGamePacketListener.player.hasPermissionLevel(4))
            serverGamePacketListener.disconnect(getStyledComponent("На сервере ведутся технические работы.", TextStyleUtil.RED.getStyle()));
    }

    public static void onMessage(SignedMessage playerChatMessage, ServerPlayerEntity serverPlayer, MessageType.Parameters params) {
        if (!(serverPlayer instanceof ServerPlayerEntity)) return;
        sendChatMessage("1250613189658411009", new MessageCreateBuilder().setContent(
                "**" + serverPlayer.getDisplayName().getString() + "**: " + playerChatMessage.getSignedContent()
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

    public static void joinedServerEvent(ServerPlayNetworkHandler serverGamePacketListener, PacketSender packetSender, MinecraftServer server) {
        ServerPlayerEntity serverPlayer = serverGamePacketListener.getPlayer();
        if (ConfigVariables.MAINTANCE) {
            setAuthorized(serverPlayer, true);
            return;
        }

        setAuthorized(serverPlayer, false);
        sendChatMessageToPlayer(serverPlayer, getAuthMessage(serverPlayer));

        serverGamePacketListener.getPlayer().setInvisible(true);
        serverGamePacketListener.getPlayer().setInvulnerable(true);

        sendChatMessage("1250613189658411009", new MessageCreateBuilder().setContent(
                "[SYSTEM] " + serverPlayer.getName().getString() + " зашёл на сервер."
        ).build());
    }

    public static void disconnectServerEvent(ServerPlayNetworkHandler serverGamePacketListener, MinecraftServer server) {
        if (ConfigVariables.MAINTANCE) return;
        ServerPlayerEntity serverPlayer = serverGamePacketListener.getPlayer();
        sendChatMessage("1250613189658411009", new MessageCreateBuilder().setContent(
                "[SYSTEM] " + serverPlayer.getName().getString() + " вышел с сервера."
        ).build());
        setAuthorized(serverPlayer, false);
    }

    public static boolean allowChatMessage(SignedMessage playerChatMessage, ServerPlayerEntity player, MessageType.Parameters params) {
        if (player == null) return true;
        if (!isAuthorized(player)) {
            return false;
        }
        return true;
    }

    public static ActionResult dropItemEvent(PlayerEntity player) {
        if (player.getWorld().getServer() == null) return ActionResult.PASS;
        ServerPlayerEntity serverPlayer = player.getWorld().getServer().getPlayerManager().getPlayer(player.getName().getString());
        if (serverPlayer == null) return ActionResult.PASS;
        if (!isAuthorized((ServerPlayerEntity) player)) {
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }

    public static ActionResult pickUpItemPre(PlayerEntity player) {
        if (player.getWorld().getServer() == null) return ActionResult.PASS;
        ServerPlayerEntity serverPlayer = player.getWorld().getServer().getPlayerManager().getPlayer(player.getName().getString());
        if (serverPlayer == null) return ActionResult.PASS;
        if (!isAuthorized((ServerPlayerEntity) player)) {
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }
}
