package ru.nightidk.deathnote.items.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;

public class InfinityArmor extends ArmorItem {
    public InfinityArmor(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    public <T extends InfinityArmor> boolean wearThisItem(PlayerEntity player, T armorPart) {
        return player.getEquippedStack(this.type.getEquipmentSlot()).getItem() == armorPart;
    }
}
