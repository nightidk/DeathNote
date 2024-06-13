package ru.nightidk.jda;

import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.time.OffsetDateTime;
import java.util.List;

public class BaseEmbed extends MessageEmbed {
    public BaseEmbed(String title, String description, int color, AuthorInfo author) {
        super(null, title, description, EmbedType.UNKNOWN, null, color, null, null, author, null, null, null, null);
    }
}
