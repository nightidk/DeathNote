package ru.nightidk.deathnote.jda;

import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class BaseEmbed extends MessageEmbed {
    public BaseEmbed(String title, String description, int color, AuthorInfo author) {
        super(null, title, description, EmbedType.UNKNOWN, null, color, null, null, author, null, null, null, null);
    }
}
