package ru.nightidk.deathnote.configuration;

import com.google.gson.JsonObject;
import ru.nightidk.deathnote.utils.Location;
import ru.nightidk.deathnote.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class AuthVariables {
    public static JsonObject JSON_AUTH = new JsonObject();
    public static List<String> playerList = new ArrayList<>();
    public static List<Pair<String, Location>> nonAuthPlayer = new ArrayList<>();
}
