package ru.nightidk.configuration;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.nightidk.utils.Location;
import ru.nightidk.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AuthVariables {
    public static JsonObject JSON_AUTH = new JsonObject();
    public static List<String> playerList = new ArrayList<>();
    public static List<Pair<String, Location>> nonAuthPlayer = new ArrayList<>();
}
