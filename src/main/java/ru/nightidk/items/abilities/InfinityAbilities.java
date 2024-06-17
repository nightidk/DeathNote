package ru.nightidk.items.abilities;

import io.github.ladysnake.pal.Pal;
import io.github.ladysnake.pal.PlayerAbility;

public class InfinityAbilities {
    public static PlayerAbility NIGHT_VISION;

    public static PlayerAbility SPEED_UP;

    InfinityAbilities() {}

    public static void init() {
        SPEED_UP = Pal.registerAbility("deathnote", "speed_up",
                (ability, player) -> new InfinityAbilityTracker(ability, player, InfinityAbilitiesProps::setNightVision, InfinityAbilitiesProps::isNightVision));
        NIGHT_VISION = Pal.registerAbility("deathnote", "night_vision",
                (ability, player) -> new InfinityAbilityTracker(ability, player, InfinityAbilitiesProps::setNightVision, InfinityAbilitiesProps::isNightVision));
    }
}
