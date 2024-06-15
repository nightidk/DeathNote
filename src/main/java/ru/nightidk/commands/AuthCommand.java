package ru.nightidk.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import ru.nightidk.utils.AuthUtil;
import ru.nightidk.utils.TextStyleUtil;

import java.util.Objects;

import static ru.nightidk.utils.AuthUtil.*;
import static ru.nightidk.utils.ChatMessageUtil.getStyledComponent;
import static ru.nightidk.utils.ChatMessageUtil.sendChatMessageToPlayer;

public class AuthCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        final LiteralCommandNode<ServerCommandSource> login = dispatcher.register(
                CommandManager.literal("login")
                        .then(CommandManager.argument("password", StringArgumentType.string())
                                .requires(s -> isRegistered(s.getName()) && s.isExecutedByPlayer())
                                .executes(context -> {
                                    String password = context.getArgument("password", String.class);
                                    if (context.getSource().getPlayer() == null) return -1;
                                    try {
                                        if (passwordEquals(context.getSource().getName(), password)) {
                                            sendChatMessageToPlayer(context.getSource().getPlayer(), getStyledComponent("Успешная авторизация.", TextStyleUtil.GREEN.getStyle()));
                                            setAuthorized(context.getSource().getPlayer());
                                        } else {
                                            context.getSource().getPlayer().networkHandler.disconnect(getStyledComponent("Неверный пароль", TextStyleUtil.RED.getStyle()));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        context.getSource().getPlayer().networkHandler.disconnect(getStyledComponent("Что-то пошло не так. Обратитесь к администрации.", TextStyleUtil.RED.getStyle()));
                                    }
                                    return 0;
                                })
                        )
        );

        final LiteralCommandNode<ServerCommandSource> register = dispatcher.register(
                CommandManager.literal("register")
                        .then(CommandManager.argument("password", StringArgumentType.string())
                                .then(CommandManager.argument("repeatPassword", StringArgumentType.string())
                                    .requires(s -> !isRegistered(s.getName()) && s.isExecutedByPlayer())
                                    .executes(context -> {
                                        String password = context.getArgument("password", String.class);
                                        String repeatPassword = context.getArgument("repeatPassword", String.class);
                                        if (context.getSource().getPlayer() == null) return -1;
                                        if (Objects.equals(password, repeatPassword)) {
                                            try {
                                                AuthUtil.register(context.getSource().getName(), password, context.getSource().getPlayer().getUuidAsString());
                                                sendChatMessageToPlayer(context.getSource().getPlayer(), getStyledComponent("Успешная регистрация.", TextStyleUtil.GREEN.getStyle()));
                                                setAuthorized(context.getSource().getPlayer());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                context.getSource().getPlayer().networkHandler.disconnect(getStyledComponent("Что-то пошло не так. Обратитесь к администрации.", TextStyleUtil.RED.getStyle()));
                                            }
                                        } else
                                            sendChatMessageToPlayer(context.getSource().getPlayer(), getStyledComponent("Пароли не совпадают.", TextStyleUtil.RED.getStyle()));
                                        return 0;
                                    })
                                )
                        )
        );

        dispatcher.register(CommandManager.literal("l").redirect(login));
        dispatcher.register(CommandManager.literal("reg").redirect(register));
    }
}
