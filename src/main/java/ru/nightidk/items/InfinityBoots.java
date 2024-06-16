package ru.nightidk.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import ru.nightidk.items.base.InfinityArmor;

import static ru.nightidk.register.MaterialRegister.INFINITY_MATERIAL;

public class InfinityBoots extends InfinityArmor {

    public static float JUMP_MODIFIER = 0.5f;

    public InfinityBoots() {
        super(INFINITY_MATERIAL, Type.BOOTS, new Settings().maxCount(1).rarity(Rarity.EPIC));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {


        if (!world.isClient()) {
            if (!(entity instanceof ServerPlayerEntity player)) return;
            boolean wear = wearThisItem(player, this);
            if (!wear) {
                player.removeStatusEffect(StatusEffects.JUMP_BOOST);
                return;
            }

        } else {
            if (!(entity instanceof PlayerEntity player)) return;
            boolean wear = wearThisItem(player, this);
            if (!wear) player.setStepHeight(0.6F);
            else player.setStepHeight(1.0F);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
