package ru.nightidk.deathnote.logic;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import ru.nightidk.deathnote.items.abilities.InfinityAbilities;
import ru.nightidk.deathnote.register.*;

import static ru.nightidk.deathnote.DeathNote.LOGGER;

public class BothLoadLogic {
    public static void onClientOrServerLoad() {
        InfinityAbilities.init();
        LOGGER.info("[DeathNote] Abilities initialized.");
        ScreenHandlerTypesRegister.register();
        LOGGER.info("[DeathNote] Registered screen handlers.");
        BlockRegister.register();
        LOGGER.info("[DeathNote] Registered blocks.");
        TileRegister.register();
        LOGGER.info("[DeathNote] Registered tiles.");
        ItemRegister.register();
        LOGGER.info("[DeathNote] Registered items.");
        CreativeTabRegister.register();
        LOGGER.info("[DeathNote] Registered creative tab.");
        SerializerRegister.register();
        LOGGER.info("[DeathNote] Registered recipe serializers.");
        RecipeRegister.register();
        LOGGER.info("[DeathNote] Registered recipes.");

        ItemGroupEvents.modifyEntriesEvent(CreativeTabRegister.INFINITY_MOD_GROUP_KEY).register(content -> {
            content.add(ItemRegister.INFINITY_INGOT);
            content.add(ItemRegister.INFINITY_HELMET);
            content.add(ItemRegister.INFINITY_CHESTPLATE);
            content.add(ItemRegister.INFINITY_LEGGINGS);
            content.add(ItemRegister.INFINITY_BOOTS);
            content.add(ItemRegister.INFINITY_CRAFTING_TABLE_ITEM);
        });
        LOGGER.info("[DeathNote] Added items to creative tab.");

        LOGGER.info("[DeathNote] Client and server logic initialized.");
    }
}
