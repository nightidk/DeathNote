package ru.nightidk.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import ru.nightidk.items.base.InfinityArmor;

import static ru.nightidk.register.MaterialRegister.INFINITY_MATERIAL;

public class InfinityChestPlate extends InfinityArmor {
    public InfinityChestPlate() {
        super(INFINITY_MATERIAL, Type.CHESTPLATE, new Item.Settings().maxCount(1).rarity(Rarity.EPIC));
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        super.onCraft(stack, world, player);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient()) {
            if (!(entity instanceof PlayerEntity player)) return;
            boolean wear = wearThisItem(player, this);
            player.getAbilities().allowFlying = wear;
            if (!wear)
                player.getAbilities().flying = false;
            player.sendAbilitiesUpdate();
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

}
