package ru.nightidk.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ru.nightidk.configuration.AuthVariables;
import ru.nightidk.configuration.ConfigVariables;

import java.io.*;
import java.util.Properties;

import static ru.nightidk.DeathNote.LOGGER;

public class ConfigUtils {
    public static void loadAuth(File file) {
        try {
            if (!file.exists() || !file.canRead())
                saveAuth(file);
            FileReader fis = new FileReader(file);;
            AuthVariables.JSON_AUTH = (JsonObject) JsonParser.parseReader(fis);
            fis.close();
        }  catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("[DeathNote] Something goes wrong when loading auth file.");
        }
    }

    public static void saveAuth(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file, false);
        if (AuthVariables.JSON_AUTH.size() == 0)
            AuthVariables.JSON_AUTH.add("users", new JsonArray());
        fos.write(AuthVariables.JSON_AUTH.toString().getBytes());
        fos.close();
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
