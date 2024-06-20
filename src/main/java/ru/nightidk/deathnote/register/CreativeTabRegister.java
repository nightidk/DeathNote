package ru.nightidk.deathnote.register;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import ru.nightidk.deathnote.DeathNote;

public class CreativeTabRegister {

    public static RegistryKey<ItemGroup> INFINITY_MOD_GROUP_KEY;

    public static ItemGroup INFINITY_MOD_GROUP = FabricItemGroup.builder()
            .displayName(Text.translatable("deathnote.root"))
            .icon(ItemRegister.INFINITY_INGOT::getDefaultStack)
            .build();

    public static void register() {
        Registry.register(Registries.ITEM_GROUP, new Identifier(DeathNote.MOD_ID, "deathnote_root"), INFINITY_MOD_GROUP);
        INFINITY_MOD_GROUP_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(DeathNote.MOD_ID, "deathnote_root"));

    }
}
