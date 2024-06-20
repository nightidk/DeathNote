package ru.nightidk.deathnote.register;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import ru.nightidk.deathnote.DeathNote;
import ru.nightidk.deathnote.blocks.craft.InfinityCraftingTable;

public class BlockRegister {
    public static final InfinityCraftingTable INFINITY_CRAFTING_TABLE = new InfinityCraftingTable();

    public static void register() {
        Registry.register(Registries.BLOCK, new Identifier(DeathNote.MOD_ID, "infinity_crafting_table"), INFINITY_CRAFTING_TABLE);
    }
}
