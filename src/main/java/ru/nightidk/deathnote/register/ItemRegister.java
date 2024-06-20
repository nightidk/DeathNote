package ru.nightidk.deathnote.register;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import ru.nightidk.deathnote.DeathNote;
import ru.nightidk.deathnote.items.*;
import ru.nightidk.deathnote.utils.ChatMessageUtil;
import ru.nightidk.deathnote.utils.TextStyleUtil;

public class ItemRegister {
    public static final Item INFINITY_INGOT = new InfinityIngot();
    public static final Item INFINITY_HELMET = new InfinityHelmet();
    public static final Item INFINITY_CHESTPLATE = new InfinityChestplate();
    public static final Item INFINITY_LEGGINGS = new InfinityLeggings();
    public static final Item INFINITY_BOOTS = new InfinityBoots();
    public static final BlockItem INFINITY_CRAFTING_TABLE_ITEM = new BlockItem(BlockRegister.INFINITY_CRAFTING_TABLE, new FabricItemSettings());
    public static final Item POISONED_DROP_OF_BLOOD = makeCustomColorName("Застывшая отравленная кровь", TextStyleUtil.GREEN.getStyle(), new FabricItemSettings());
    public static final Item POISONED_MATRIX_INGOT = makeCustomColorName("Отравленный кроваво-кристальный слиток", TextStyleUtil.GREEN.getStyle(), new FabricItemSettings());
    public static final Item CLEAN_MATRIX_INGOT = makeCustomColorName("Кроваво-кристальный слиток", TextStyleUtil.RED.getStyle(), new FabricItemSettings());

    public static void register() {
        Registry.register(Registries.ITEM, new Identifier(DeathNote.MOD_ID, "infinity_ingot"), INFINITY_INGOT);
        Registry.register(Registries.ITEM, new Identifier(DeathNote.MOD_ID, "infinity_helmet"), INFINITY_HELMET);
        Registry.register(Registries.ITEM, new Identifier(DeathNote.MOD_ID, "infinity_chestplate"), INFINITY_CHESTPLATE);
        Registry.register(Registries.ITEM, new Identifier(DeathNote.MOD_ID, "infinity_leggings"), INFINITY_LEGGINGS);
        Registry.register(Registries.ITEM, new Identifier(DeathNote.MOD_ID, "infinity_boots"), INFINITY_BOOTS);
        Registry.register(Registries.ITEM, new Identifier(DeathNote.MOD_ID, "poisoneddropofblood"), POISONED_DROP_OF_BLOOD);
        Registry.register(Registries.ITEM, new Identifier(DeathNote.MOD_ID, "poisoned_matrix_ingot"), POISONED_MATRIX_INGOT);
        Registry.register(Registries.ITEM, new Identifier(DeathNote.MOD_ID, "clean_matrix_ingot"), CLEAN_MATRIX_INGOT);
        Registry.register(Registries.ITEM, Identifier.of(DeathNote.MOD_ID, "infinity_crafting_table"), INFINITY_CRAFTING_TABLE_ITEM);
    }

    private static Item makeCustomColorName(String name, Style color, Item.Settings settings) {
        return new Item(settings) {
            @Override
            public Text getName(ItemStack stack) {
                return ChatMessageUtil.getStyledComponent(name, color);
            }

            @Override
            public Text getName() {
                return ChatMessageUtil.getStyledComponent(name, color);
            }
        };
    }
}
