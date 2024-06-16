package ru.nightidk.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import ru.nightidk.items.base.InfinityArmor;

import java.util.Objects;

import static ru.nightidk.register.MaterialRegister.INFINITY_MATERIAL;

public class InfinityLeggings extends InfinityArmor {
    public InfinityLeggings() {
        super(INFINITY_MATERIAL, Type.LEGGINGS, new Settings().maxCount(1).rarity(Rarity.EPIC));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient()) {
            if (!(entity instanceof PlayerEntity player)) return;
            EntityAttributeInstance genericMovementSpeed = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);

            boolean wear = wearThisItem(player, this);
            if (!wear) {
                player.getAbilities().setFlySpeed(0.05F);
                if (genericMovementSpeed != null)
                    genericMovementSpeed.clearModifiers();
                return;
            }
            EntityAttributeModifier modifier = new EntityAttributeModifier("add", 0.3F, EntityAttributeModifier.Operation.ADDITION);
            player.getAbilities().setFlySpeed(0.15F);
            if (genericMovementSpeed != null && genericMovementSpeed.getModifiers(EntityAttributeModifier.Operation.ADDITION).stream().filter(s -> Objects.equals(s.getName(), "add")).findFirst().isEmpty())
                genericMovementSpeed.addTemporaryModifier(modifier);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
