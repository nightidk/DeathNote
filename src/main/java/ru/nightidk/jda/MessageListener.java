package ru.nightidk.jda;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ru.nightidk.DeathNote;
import ru.nightidk.utils.TextStyleUtil;

import static ru.nightidk.utils.ChatMessageUtil.getStyledComponent;
import static ru.nightidk.utils.ChatMessageUtil.sendChatMessageToAll;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getMessage().getContentStripped().isEmpty() && !event.getAuthor().isBot() && event.getMember() != null && event.getChannel().getId().equals("1250613189658411009")) {
            String nickname = event.getMember().getNickname() != null ? event.getMember().getNickname() : event.getAuthor().getEffectiveName();
            sendChatMessageToAll(
                    DeathNote.server.getPlayerManager().getPlayerList(),
                    getStyledComponent("[Discord]", TextStyleUtil.DARK_AQUA.getStyle())
                            .append(getStyledComponent(" %s".formatted(nickname), TextStyleUtil.DARK_AQUA.getStyle())
                                    .append(getStyledComponent(": %s".formatted(event.getMessage().getContentStripped()), TextStyleUtil.WHITE.getStyle()))
                            )
            );
        }
    }
}
