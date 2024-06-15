package ru.nightidk.logic;

import dev.architectury.platform.Platform;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.*;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import ru.nightidk.commands.AuthCommand;
import ru.nightidk.commands.ConfigReload;
import ru.nightidk.commands.MaintanceCommand;
import ru.nightidk.commands.RestartCommand;
import ru.nightidk.configuration.ConfigVariables;
import ru.nightidk.jda.BaseEmbed;
import ru.nightidk.jda.MessageListener;
import ru.nightidk.listeners.AuthEventListener;
import ru.nightidk.listeners.ModEventListener;

import java.awt.*;
import java.io.File;

import static ru.nightidk.DeathNote.*;
import static ru.nightidk.utils.ConfigUtils.*;
import static ru.nightidk.utils.JDAMessageUtil.editStatusMessage;

public class ServerLoadLogic {

    public static void onServerLoad() {
        LOGGER.info("[DeathNote] Server logic initialized.");
        LOGGER.info("[DeathNote] Loading config file...");
        configFile = new File(Platform.getConfigFolder().toFile(), "deathnote.properties");
        loadConfig(configFile);
        LOGGER.info("[DeathNote] Finished loading config file.");

        LOGGER.info("[DeathNote] Loading auth file...");
        File directory = new File(Platform.getModsFolder().toFile() + "/deathnote");
        if (!directory.exists())
            if (!directory.mkdirs())
                throw new RuntimeException("[DeathNote] Error creating directory for auth config.");
        authFile = new File(Platform.getModsFolder().toFile() + "/deathnote/", "auth.json");
        loadAuth(authFile);
        LOGGER.info("[DeathNote] Finished loading auth file.");


        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ConfigReload.register(dispatcher);
            RestartCommand.register(dispatcher);
            MaintanceCommand.register(dispatcher);
            AuthCommand.register(dispatcher);
        });
        LOGGER.info("[DeathNote] Commands registered.");


        if (!ConfigVariables.DISCORD_AUTH.isEmpty()) {
            LOGGER.info("[DeathNote] Authorization in JDA...");
            jda = JDABuilder.createDefault(ConfigVariables.DISCORD_AUTH)
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
                    .build();

            try {
                jda.awaitReady();
                LOGGER.info("[DeathNote] Successfully authorized in JDA.");

            } catch (InterruptedException e) {
                e.printStackTrace();
                LOGGER.info("[DeathNote] Error while authorization in JDA.");
            }
            jda.addEventListener(new MessageListener());
        }

        ServerLifecycleEvents.SERVER_STARTING.register(server1 -> {
            editStatusMessage(
                    new MessageEditBuilder()
                            .setEmbeds(
                                    new BaseEmbed(
                                            "Информация об сервере",
                                            "Статус: " + (ConfigVariables.MAINTANCE ? "Тех. работы" : "Запускается..."),
                                            (ConfigVariables.MAINTANCE ? new Color(255, 136, 0).getRGB() : new Color(255, 219, 0).getRGB()),
                                            new MessageEmbed.AuthorInfo("Империя \"von\"", null, null, null)
                                    )
                            ).build()
            );
        });
        ServerLifecycleEvents.SERVER_STARTED.register(server1 -> {
            server = server1;
            ModEventListener.serverStartedEvent(server1);
        });
        ServerTickEvents.END_SERVER_TICK.register(ModEventListener::restartServerTickEvent);
        ServerTickEvents.END_SERVER_TICK.register(ModEventListener::cleanItemsTickEvent);
        ServerLifecycleEvents.SERVER_STOPPING.register(ModEventListener::serverStoppingEvent);
        ServerLifecycleEvents.SERVER_STOPPED.register(s -> {
            try {
                ModEventListener.serverStoppedEvent(s);
                LOGGER.info("[DeathNote] JDA shutdown.");
            } catch (InterruptedException e) {
                e.printStackTrace();
                LOGGER.info("[DeathNote] JDA error while shutdown, forcing...");
                jda.shutdownNow();
            }
        });
        ServerMessageEvents.CHAT_MESSAGE.register(ModEventListener::onMessage);
        ServerPlayConnectionEvents.INIT.register(ModEventListener::joinServerEvent);
        ServerPlayConnectionEvents.JOIN.register(ModEventListener::joinedServerEvent);
        ServerPlayConnectionEvents.DISCONNECT.register(ModEventListener::disconnectServerEvent);

        // AUTH EVENTS
        UseEntityCallback.EVENT.register(AuthEventListener::useEntityEvent);
        PlayerBlockBreakEvents.BEFORE.register(AuthEventListener::breakBlockEvent);
        UseBlockCallback.EVENT.register(AuthEventListener::useBlockEvent);
        AttackEntityCallback.EVENT.register(AuthEventListener::attackEntityEvent);
        UseItemCallback.EVENT.register(AuthEventListener::useItemEvent);
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(ModEventListener::allowChatMessage);

        ModEventListener.setTickClean(ConfigVariables.TICK_FOR_CLEAN);
        ModEventListener.setTickRestart(ConfigVariables.RESTART_TIME);
        LOGGER.info("[DeathNote] Server tick event registered.");
    }

}
