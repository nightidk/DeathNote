package ru.nightidk.utils;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import ru.nightidk.DeathNote;
import ru.nightidk.configuration.ConfigVariables;

public class JDAMessageUtil {
    public static void editStatusMessage(MessageEditData data) {
        if (DeathNote.jda == null) return;
        TextChannel textChannel = DeathNote.jda.getTextChannelById("1250591961988206652");
        if (textChannel == null) return;
        textChannel.editMessageById(ConfigVariables.DISCORD_STATUS_MESSAGE, data).queue();
    }

    public static void sendChatMessage(String channelId, MessageCreateData data) {
        if (DeathNote.jda == null) return;
        TextChannel textChannel = DeathNote.jda.getTextChannelById(channelId);
        if (textChannel == null) return;
        textChannel.sendMessage(data).queue();
    }
}
