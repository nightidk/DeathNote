package ru.nightidk.deathnote.register;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import ru.nightidk.deathnote.DeathNote;
import ru.nightidk.deathnote.tiles.InfinityCraftingTableTile;

import static ru.nightidk.deathnote.register.BlockRegister.INFINITY_CRAFTING_TABLE;

public class TileRegister {
    public static BlockEntityType<InfinityCraftingTableTile> INFINITY_TABLE_TILE_TYPE;

    public static void register() {
        INFINITY_TABLE_TILE_TYPE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(DeathNote.MOD_ID, "infinity_table_entity"),
                FabricBlockEntityTypeBuilder.create(InfinityCraftingTableTile::new, INFINITY_CRAFTING_TABLE).build()
        );
    }
}
