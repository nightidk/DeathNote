package ru.nightidk.utils;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.List;
import java.util.ListIterator;

public class ChatMessageUtil {
    public static void sendChatMessageToAll(List<ServerPlayerEntity> playerList, Text message) {
        ListIterator<ServerPlayerEntity> iterator = playerList.listIterator();
        while(true)
        {
            if(iterator.hasNext())
            {
                ServerPlayerEntity TmpPlayer = iterator.next();
                TmpPlayer.sendMessage(message);
            }
            else break;
        }
    }

    public static void sendChatMessageToAll(List<ServerPlayerEntity> source, String message) {
        sendChatMessageToAll(source, Text.literal(message));
    }

    public static void sendChatMessageToPlayer(ServerPlayerEntity serverPlayer, String message) {
        serverPlayer.sendMessage(Text.literal(message));
    }

    public static void sendChatMessageToPlayer(ServerPlayerEntity serverPlayer, Text message) {
        serverPlayer.sendMessage(message);
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

    public static MutableText getStyledComponent(String message, Style format) {
        return Text.literal(message).setStyle(format);
    }
}
