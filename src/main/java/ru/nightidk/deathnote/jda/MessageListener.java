package ru.nightidk.deathnote.jda;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.nightidk.deathnote.DeathNote;
import ru.nightidk.deathnote.utils.TextStyleUtil;

import java.util.List;

import static ru.nightidk.deathnote.utils.ChatMessageUtil.*;
import static ru.nightidk.deathnote.utils.JDAMessageUtil.sendChatMessage;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getContentStripped().isEmpty()) return;
        if (!event.getChannel().getId().equals("1250613189658411009")) return;
        if (DeathNote.server == null) return;
        if (event.getMessage().getContentStripped().startsWith("/")) {
            String content = event.getMessage().getContentStripped();
            String[] command = content.strip().substring(1).split(" ");
            if (command.length == 0) return;
            switch (command[0]) {
                case "list" -> {
                    List<ServerPlayerEntity> entityList = DeathNote.server.getPlayerManager().getPlayerList();
                    sendChatMessage(
                            event.getChannel().getId(),
                            new MessageCreateBuilder()
                                    .setContent("[Command] В данный момент на сервере %d %s: ".formatted(
                                            entityList.size(), getWordInDeclension(entityList.size(), List.of("игрок", "игрока", "игроков"))
                                    ) + String.join(", ", entityList.stream().map(s -> s.getDisplayName().getString()).toList()))
                                    .build()
                    );
                }
                case "info" -> {
                    // pass
                }
                default -> {
                    sendChatMessage(
                            event.getChannel().getId(),
                            new MessageCreateBuilder()
                                    .setContent("[Command] Команда не распознана.")
                                    .build()
                    );
                }
            }
            return;
        }
        if (!event.getMessage().getContentStripped().isEmpty() && !event.getAuthor().isBot() && event.getMember() != null && event.getChannel().getId().equals("1250613189658411009")) {
            String nickname = event.getMember().getNickname() != null ? event.getMember().getNickname() : event.getAuthor().getEffectiveName();
            sendChatMessageToAll(
                    DeathNote.server.getPlayerManager().getPlayerList(),
                    getStyledComponent("[Discord]", TextStyleUtil.DARK_AQUA.getStyle())
                            .append(getStyledComponent(" %s".formatted(nickname), TextStyleUtil.DARK_AQUA.getStyle())
                                    .append(getStyledComponent(": %s".formatted(event.getMessage().getContentStripped()), TextStyleUtil.WHITE.getStyle()))
                            ),
                    MessageType.DISCORD
            );
        }
    }
}
