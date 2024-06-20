package ru.nightidk.deathnote.api.rarity;

import net.minecraft.util.Rarity;

public interface IRarity {
    default Enum<?> getCustomRarity() {
        return null;
    }
}
