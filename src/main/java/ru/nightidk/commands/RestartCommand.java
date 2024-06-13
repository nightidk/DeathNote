package ru.nightidk.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import ru.nightidk.DeathNote;
import ru.nightidk.configuration.ConfigVariables;
import ru.nightidk.listeners.ModEventListener;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.nightidk.utils.ChatMessageUtil.*;

public class RestartCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("deathnote")
                        .then(Commands.literal("cancelRestart")
                                .requires(r -> r.hasPermission(4))
                                .executes(context -> {
                                    sendChatMessageToPlayer(context.getSource().getPlayer(), "[DeathNote] Restart canceled");
                                    ModEventListener.setTickRestart(ModEventListener.tickForPlannedRestart);
                                    sendChatMessageToPlayer(context.getSource().getPlayer(),
                                            getStyledComponent("[Оповещение]", ChatFormatting.DARK_AQUA)
                                                    .append(getStyledComponent(" Перезагрузка, назначенная администратором, отменена. Следующая плановая перезагрузка через %s.".formatted(fromTicksToStringTime(ModEventListener.tickForPlannedRestart)), ChatFormatting.WHITE)));
                                    ModEventListener.tickForPlannedRestart = 0;
                                    return -1;
                                })
                        )
                        .then(Commands.literal("instantRestart")
                                .requires(r -> r.hasPermission(4))
                                .executes(context -> {
                                    sendChatMessageToPlayer(context.getSource().getPlayer(), "[DeathNote] Restarting server...");
                                    sendChatMessageToAll(
                                            context.getSource().getServer().getPlayerList(),
                                            getStyledComponent("[Оповещение]", ChatFormatting.DARK_AQUA)
                                                    .append(getStyledComponent(" Перезагрузка сервера...", ChatFormatting.WHITE))
                                    );
                                    context.getSource().getServer().getPlayerList().getPlayers().forEach(serverPlayer -> serverPlayer.connection.disconnect(getStyledComponent("Перезагрузка сервера...", ChatFormatting.DARK_AQUA)));
                                    context.getSource().getServer().halt(false);
                                    return 1;
                                })
                        )
                        .then(Commands.literal("restart")
                                .requires(r -> r.hasPermission(4))
                                .then(Commands.argument("time", StringArgumentType.string())
                                        .executes(context -> {
                                            String time = context.getArgument("time", String.class);
                                            int ticks = getTimeFromString(time);
                                            if (ticks == -1)
                                                sendChatMessageToPlayer(context.getSource().getPlayer(), getStyledComponent("[DeathNote] Something goes wrong.", ChatFormatting.RED));
                                            else {
                                                sendChatMessageToPlayer(context.getSource().getPlayer(), "[DeathNote] Restarting server planned in %s.".formatted(fromTicksToStringTime(ticks)));
                                                sendChatMessageToPlayer(context.getSource().getPlayer(),
                                                        getStyledComponent("[Оповещение]", ChatFormatting.DARK_AQUA)
                                                            .append(getStyledComponent(" Администратором назначена перезагрузка сервера через %s.".formatted(fromTicksToStringTime(ticks)), ChatFormatting.WHITE)));
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
