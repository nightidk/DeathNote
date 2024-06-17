package ru.nightidk.logic;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import ru.nightidk.items.abilities.InfinityAbilities;
import ru.nightidk.register.CreativeTabRegister;
import ru.nightidk.register.ItemRegister;

import static ru.nightidk.DeathNote.LOGGER;

public class BothLoadLogic {
    public static void onClientOrServerLoad() {
        InfinityAbilities.init();
        LOGGER.info("[DeathNote] Abilities initialized.");
        ItemRegister.register();
        LOGGER.info("[DeathNote] Registered items.");
        CreativeTabRegister.register();
        LOGGER.info("[DeathNote] Registered creative tab.");

        ItemGroupEvents.modifyEntriesEvent(CreativeTabRegister.INFINITY_MOD_GROUP_KEY).register(content -> {
            content.add(ItemRegister.INFINITY_INGOT);
            content.add(ItemRegister.INFINITY_HELMET);
            content.add(ItemRegister.INFINITY_CHESTPLATE);
            content.add(ItemRegister.INFINITY_LEGGINGS);
            content.add(ItemRegister.INFINITY_BOOTS);
        });
        LOGGER.info("[DeathNote] Added items to creative tab.");

        LOGGER.info("[DeathNote] Client and server logic initialized.");
    }
}
