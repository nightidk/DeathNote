package ru.nightidk.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import ru.nightidk.DeathNote;
import ru.nightidk.configuration.AuthVariables;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static ru.nightidk.utils.ChatMessageUtil.getStyledComponent;

public class AuthUtil {
    public static boolean isRegistered(String nickname) {
        return AuthVariables.JSON_AUTH.get("users").getAsJsonArray().asList().stream().anyMatch(el -> {
            JsonObject jsonElement = el.getAsJsonObject();
            if (jsonElement.get("nickname") == null) return false;
            return Objects.equals(jsonElement.get("nickname").getAsString(), nickname);
        });
    }

    public static boolean passwordEquals(String nickname, String pass) throws NoSuchAlgorithmException {
        Optional<JsonElement> playerObject = AuthVariables.JSON_AUTH.get("users").getAsJsonArray().asList().stream().filter(el -> {
            JsonObject jsonElement = el.getAsJsonObject();
            if (jsonElement.get("nickname") == null) return false;
            return Objects.equals(jsonElement.get("nickname").getAsString(), nickname);
        }).findFirst();
        String md5Password = md5(pass);
        return playerObject.filter(jsonElement -> md5Password.equals(jsonElement.getAsJsonObject().get("password").getAsString())).isPresent();
    }

    public static void register(String nickname, String password, String uuid) throws IOException, NoSuchAlgorithmException {
        JsonObject element = new JsonObject();
        element.addProperty("nickname", nickname);
        element.addProperty("password", md5(password));
        element.addProperty("uuid", uuid);
        AuthVariables.JSON_AUTH.get("users").getAsJsonArray().add(element);
        DeathNote.saveAuth(DeathNote.authFile);
    }

    public static boolean isAuthorized(ServerPlayerEntity player) {
        return AuthVariables.playerList.contains(player.getName().getString());
    }

    public static void setAuthorized(ServerPlayerEntity player, boolean value) {
        if (value) {
            Optional<Pair<String, Location>> location = AuthVariables.nonAuthPlayer.stream().filter(s -> s.getKey().equals(player.getName().getString())).findFirst();
            location.ifPresent(stringLocationEntry -> AuthVariables.nonAuthPlayer.remove(stringLocationEntry));
            AuthVariables.playerList.add(player.getName().getString());
            player.setInvisible(false);
            player.setInvulnerable(false);
        } else {
            AuthVariables.nonAuthPlayer.add(new Pair<>(player.getName().getString(), new Location(player.getX(), player.getY(), player.getZ())));
            AuthVariables.playerList.remove(player.getName().getString());
            player.setInvisible(true);
            player.setInvulnerable(true);
        }
    }

    public static void setAuthorized(ServerPlayerEntity player) {
        setAuthorized(player, true);
    }

    public static MutableText getAuthMessage(ServerPlayerEntity player) {
        if (isRegistered(player.getName().getString()))
            return getStyledComponent("Авторизируйтесь: /login <password>", TextStyleUtil.YELLOW.getStyle());
        else
            return getStyledComponent("Зарегистрируйтесь: /register <password> <password>", TextStyleUtil.YELLOW.getStyle());
    }

    protected static String md5(String value) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(StandardCharsets.UTF_8.encode(value));
        return String.format("%032x", new BigInteger(1, md5.digest()));
    }
}
