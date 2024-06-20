package ru.nightidk.deathnote;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nightidk.deathnote.logic.BothLoadLogic;
import ru.nightidk.deathnote.logic.ClientLoadLogic;
import ru.nightidk.deathnote.logic.ServerLoadLogic;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.xplat.BotaniaConfig;

import java.io.*;

public class DeathNote implements ModInitializer {

	public static final String MOD_ID = "deathnote";

	@Getter
	@Setter
	protected static EnvType envType;

    public static final Logger LOGGER = LoggerFactory.getLogger("deathnote");
	public static MinecraftServer server;
	public static File configFile;
	public static JDA jda;
	public static File authFile;

	@SneakyThrows
    @Override
	public void onInitialize() {
		setEnvType(FabricLoader.getInstance().getEnvironmentType());
		if (getEnvType() == EnvType.SERVER)
			ServerLoadLogic.onServerLoad();
//		else
//			ClientLoadLogic.onClientLoad();
//		BothLoadLogic.onClientOrServerLoad();

        LOGGER.info("[DeathNote] Initialized.");
	}

}