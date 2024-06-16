package ru.nightidk.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import ru.nightidk.listeners.serverside.ModEventListener;
import ru.nightidk.utils.ChatMessageUtil;
import ru.nightidk.utils.TextStyleUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.nightidk.utils.ChatMessageUtil.*;

public class RestartCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("deathnote")
                        .then(CommandManager.literal("cancelRestart")
                                .requires(r -> r.hasPermissionLevel(4))
                                .executes(context -> {
                                    if (context.getSource().getPlayer() == null) return -1;
                                    sendChatMessageToPlayer(context.getSource().getPlayer(), "[DeathNote] Restart canceled");
                                    ModEventListener.setTickRestart(ModEventListener.tickForPlannedRestart);
                                    sendChatMessageToAll(context.getSource().getServer().getPlayerManager().getPlayerList(),
                                            getStyledComponent("[Оповещение]", TextStyleUtil.DARK_AQUA.getStyle())
                                                    .append(getStyledComponent(" Перезагрузка, назначенная администратором, отменена. Следующая плановая перезагрузка через %s.".formatted(fromTicksToStringTime(ModEventListener.tickForPlannedRestart)), TextStyleUtil.WHITE.getStyle())),
                                            MessageType.NOTIFY
                                    );
                                    ModEventListener.tickForPlannedRestart = 0;
                                    return 1;
                                })
                        )
                        .then(CommandManager.literal("instantRestart")
                                .requires(r -> r.hasPermissionLevel(4))
                                .executes(context -> {
                                    if (context.getSource().getPlayer() == null) return -1;
                                    sendChatMessageToPlayer(context.getSource().getPlayer(), "[DeathNote] Restarting server...");
                                    sendChatMessageToAll(
                                            context.getSource().getServer().getPlayerManager().getPlayerList(),
                                            getStyledComponent("[Оповещение]", TextStyleUtil.DARK_AQUA.getStyle())
                                                    .append(getStyledComponent(" Перезагрузка сервера...", TextStyleUtil.WHITE.getStyle())),
                                            ChatMessageUtil.MessageType.NOTIFY
                                    );
                                    context.getSource().getServer().getPlayerManager().getPlayerList().forEach(serverPlayer -> serverPlayer.networkHandler.disconnect(getStyledComponent("Перезагрузка сервера...", TextStyleUtil.DARK_AQUA.getStyle())));
                                    context.getSource().getServer().stop(false);
                                    return 1;
                                })
                        )
                        .then(CommandManager.literal("restart")
                                .requires(r -> r.hasPermissionLevel(4))
                                .then(CommandManager.argument("time", StringArgumentType.string())
                                        .executes(context -> {
                                            String time = context.getArgument("time", String.class);
                                            int ticks = getTimeFromString(time);
                                            if (context.getSource().getPlayer() == null) return -1;
                                            if (ticks == -1)
                                                sendChatMessageToPlayer(context.getSource().getPlayer(), getStyledComponent("[DeathNote] Something goes wrong.", TextStyleUtil.RED.getStyle()));
                                            else {
                                                sendChatMessageToPlayer(context.getSource().getPlayer(), "[DeathNote] Restarting server planned in %s.".formatted(fromTicksToStringTime(ticks)));
                                                sendChatMessageToAll(context.getSource().getServer().getPlayerManager().getPlayerList(),
                                                        getStyledComponent("[Оповещение]", TextStyleUtil.DARK_AQUA.getStyle())
                                                            .append(getStyledComponent(" Администратором назначена перезагрузка сервера через %s.".formatted(fromTicksToStringTime(ticks)), TextStyleUtil.WHITE.getStyle())),
                                                        MessageType.NOTIFY
                                                );
                                                ModEventListener.tickForPlannedRestart = ModEventListener.getTickRestart();
                                                ModEventListener.setTickRestart(ticks);
                                            }
                                            return 1;
                                        })
                                )
                        )
        );
    }

    protected static int getTimeFromString(String time) {
        String regex = "(?:(?<hours>\\d+)d)?(?:(?<minutes>\\d+)m)?(?:(?<seconds>\\d+)s)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(time);

        if (matcher.matches()) {
            int hours = 0;
            int minutes = 0;
            int seconds = 0;
            if (matcher.group("hours") != null)
                 hours = Integer.parseInt(matcher.group("hours"));
            if (matcher.group("minutes") != null)
                minutes = Integer.parseInt(matcher.group("minutes"));
            if (matcher.group("seconds") != null)
                seconds = Integer.parseInt(matcher.group("seconds"));

            int ticks = 0;

            ticks += hours * 60 * 60 * 20;
            ticks += minutes * 60 * 20;
            ticks += seconds * 20;

            return ticks;
        } else {
            return -1;
        }
    }

    protected static String fromTicksToStringTime(int ticks) {
        int hours = ticks / (60 * 60 * 20);
        ticks -= hours * 60 * 60 * 20;
        int minutes = ticks / (60 * 20);
        ticks -= minutes * 60 * 20;
        int seconds = ticks / 20;

        return (hours != 0 ? "%d %s".formatted(hours, getWordInDeclension(hours, List.of("час", "часа", "часов"))) : "") +
                (hours != 0 && (minutes != 0 || seconds != 0) ? " " : "") +
                (minutes != 0 ? "%d %s".formatted(minutes, getWordInDeclension(hours, List.of("минуту", "минуты", "минут"))) : "") +
                (minutes != 0 && seconds != 0 ? " " : "") +
                (seconds != 0 ? "%d %s".formatted(seconds, getWordInDeclension(hours, List.of("секунду", "секунды", "секунд"))) : "");
    }
}
