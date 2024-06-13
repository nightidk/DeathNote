package ru.nightidk.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import ru.nightidk.DeathNote;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class PlayerArgumentType implements ArgumentType<ServerPlayer> {
    private static final DynamicCommandExceptionType ERROR_PLAYER_NOT_FOUND = new DynamicCommandExceptionType((id) ->
            Component.literal("Игрок " + id + " не найден.").withStyle(ChatFormatting.RED));

    public static PlayerArgumentType reference() { return new PlayerArgumentType(); }

    @Override
    public ServerPlayer parse(StringReader reader) throws CommandSyntaxException {
        String name = reader.readString();
        Optional<ServerPlayer> player = DeathNote.server.getPlayerList().getPlayers().stream().filter(s -> s.getName().getString().equals(name)).findFirst();

        if (player.isEmpty())
            throw ERROR_PLAYER_NOT_FOUND.create(name);
        return player.get();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(DeathNote.server.getPlayerList().getPlayers().stream().map(s -> s.getName().getString()).collect(Collectors.toList()), builder);
    }
}
