package ru.nightidk.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.ListIterator;

public class ChatMessageUtil {
    public static void sendChatMessageToAll(PlayerList playerList, Component message) {
        List<ServerPlayer> list1 = playerList.getPlayers();
        ListIterator<ServerPlayer> iterator = list1.listIterator();
        while(true)
        {
            if(iterator.hasNext())
            {
                Player TmpPlayer = iterator.next();
                TmpPlayer.sendSystemMessage(message);
            }
            else break;
        }
    }

    public static void sendChatMessageToAll(PlayerList playerList, String message) {
        sendChatMessageToAll(playerList, Component.literal(message));
    }

    public static void sendChatMessageToPlayer(ServerPlayer serverPlayer, String message) {
        ((Player) serverPlayer).sendSystemMessage(Component.literal(message));
    }

    public static void sendChatMessageToPlayer(ServerPlayer serverPlayer, Component message) {
        ((Player) serverPlayer).sendSystemMessage(message);
    }

    public static String getWordInDeclension(int value, List<String> variables) {
        int result = value % 100;
        if (result >= 10 && result <= 20) return variables.get(2);
        result = value % 10;
        if (result == 0 || result > 4) return variables.get(2);
        if (result > 1) return variables.get(1);
        if (result == 1) return variables.get(0);
        return null;
    }

    public static MutableComponent getStyledComponent(String message, ChatFormatting format) {
        return Component.literal(message).withStyle(format);
    }
}
