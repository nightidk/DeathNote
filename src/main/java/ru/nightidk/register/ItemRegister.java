package ru.nightidk.register;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import ru.nightidk.DeathNote;
import ru.nightidk.items.*;

public class ItemRegister {
    public static final Item INFINITY_INGOT = new InfinityIngot();
    public static final Item INFINITY_HELMET = new InfinityHelmet();
    public static final Item INFINITY_CHESTPLATE = new InfinityChestplate();
    public static final Item INFINITY_LEGGINGS = new InfinityLeggings();
    public static final Item INFINITY_BOOTS = new InfinityBoots();

    public static void register() {
        Registry.register(Registries.ITEM, new Identifier(DeathNote.MOD_ID, "infinity_ingot"), INFINITY_INGOT);
        Registry.register(Registries.ITEM, new Identifier(DeathNote.MOD_ID, "infinity_helmet"), INFINITY_HELMET);
        Registry.register(Registries.ITEM, new Identifier(DeathNote.MOD_ID, "infinity_chestplate"), INFINITY_CHESTPLATE);
        Registry.register(Registries.ITEM, new Identifier(DeathNote.MOD_ID, "infinity_leggings"), INFINITY_LEGGINGS);
        Registry.register(Registries.ITEM, new Identifier(DeathNote.MOD_ID, "infinity_boots"), INFINITY_BOOTS);
    }
}
