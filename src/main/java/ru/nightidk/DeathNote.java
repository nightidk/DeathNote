package ru.nightidk;

import dev.architectury.platform.Platform;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nightidk.commands.ConfigReload;
import ru.nightidk.commands.MaintanceCommand;
import ru.nightidk.commands.RestartCommand;
import ru.nightidk.configuration.ConfigVariables;
import ru.nightidk.jda.BaseEmbed;
import ru.nightidk.jda.MessageListener;
import ru.nightidk.listeners.ModEventListener;

import java.awt.*;
import java.io.*;
import java.util.Properties;

import static ru.nightidk.jda.MessageUtil.editStatusMessage;

public class DeathNote implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("deathnote");
	public static MinecraftServer server;
	public static File configFile;
	public static JDA jda;

	@SneakyThrows
    @Override
	public void onInitialize() {
//		ServerLifecycleEvents.SERVER_STARTED.register(server1 -> {
//			server = server1;
//		});
//		ArgumentTypeRegistry.registerArgumentType(
//				new ResourceLocation("deathnote", "playerarg"),
//				PlayerArgumentType.class, SingletonArgumentInfo.contextFree(PlayerArgumentType::reference));
		LOGGER.info("[DeathNote] Loading config file...");
		configFile = new File(Platform.getConfigFolder().toFile(), "deathnote.properties");
		loadConfig(configFile);
		LOGGER.info("[DeathNote] Finished loading config file.");

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			ConfigReload.register(dispatcher);
			RestartCommand.register(dispatcher);
			MaintanceCommand.register(dispatcher);
		});
		LOGGER.info("[DeathNote] Commands registered.");

		if (!ConfigVariables.DISCORD_AUTH.isEmpty()) {
			LOGGER.info("[DeathNote] Authorization in JDA...");
			jda = JDABuilder.createDefault(ConfigVariables.DISCORD_AUTH)
					.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
					.build();

			try {
				jda.awaitReady();
				LOGGER.info("[DeathNote] Successfully authorized in JDA");

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
		ModEventListener.setTickClean(ConfigVariables.TICK_FOR_CLEAN);
		ModEventListener.setTickRestart(ConfigVariables.RESTART_TIME);
		LOGGER.info("[DeathNote] Server tick event registered.");

        LOGGER.info("[DeathNote] Initialized.");
	}

	public static void loadConfig(File file) {
		try {
			if (!file.exists() || !file.canRead())
				saveConfig(file);
			FileInputStream fis = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fis);
			fis.close();
			ConfigVariables.TICK_FOR_CLEAN = Integer.parseInt((String) properties.computeIfAbsent("tick_for_clean", vr -> "36000"));
			ConfigVariables.MAINTANCE = Boolean.parseBoolean((String) properties.computeIfAbsent("maintains", vr -> "false"));
			ConfigVariables.RESTART_TIME = Integer.parseInt((String) properties.computeIfAbsent("restart_time", vr -> "432000"));
			ConfigVariables.DISCORD_AUTH = (String) properties.computeIfAbsent("discord", vr -> "");
			ConfigVariables.DISCORD_STATUS_MESSAGE = (String) properties.computeIfAbsent("status_message", vr -> "");
		} catch (Exception e) {
			e.printStackTrace();
			ConfigVariables.TICK_FOR_CLEAN = 36000;
			ConfigVariables.MAINTANCE = false;
			ConfigVariables.RESTART_TIME = 432000;
			ConfigVariables.DISCORD_AUTH = "";
			ConfigVariables.DISCORD_STATUS_MESSAGE = "";
		}
	}

	public static void saveConfig(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file, false);
		fos.write("# DeathNote Config".getBytes());
		fos.write("\n".getBytes());
		fos.write(("tick_for_clean=" + ConfigVariables.TICK_FOR_CLEAN).getBytes());
		fos.write("\n".getBytes());
		fos.write(("maintains=" + ConfigVariables.MAINTANCE).getBytes());
		fos.write("\n".getBytes());
		fos.write(("restart_time=" + ConfigVariables.RESTART_TIME).getBytes());
		fos.write("\n".getBytes());
		fos.write(("discord=" + ConfigVariables.DISCORD_AUTH).getBytes());
		fos.write("\n".getBytes());
		fos.write(("status_message=" + ConfigVariables.DISCORD_STATUS_MESSAGE).getBytes());
		fos.close();
	}

}