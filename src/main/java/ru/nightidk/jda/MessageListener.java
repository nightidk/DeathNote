package ru.nightidk.jda;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.ChatFormatting;
import ru.nightidk.DeathNote;

import static ru.nightidk.utils.ChatMessageUtil.getStyledComponent;
import static ru.nightidk.utils.ChatMessageUtil.sendChatMessageToAll;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getMessage().getContentStripped().isEmpty() && !event.getAuthor().isBot() && event.getMember() != null && event.getChannel().getId().equals("1250613189658411009")) {
            String nickname = event.getMember().getNickname() != null ? event.getMember().getNickname() : event.getAuthor().getEffectiveName();
            sendChatMessageToAll(
                    DeathNote.server.getPlayerList(),
                    getStyledComponent("[Discord]", ChatFormatting.DARK_AQUA)
                            .append(getStyledComponent(" %s".formatted(nickname), ChatFormatting.DARK_AQUA)
                                    .append(getStyledComponent(": %s".formatted(event.getMessage().getContentStripped()), ChatFormatting.WHITE))
                            )
            );
        }
    }
}
