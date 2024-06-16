package ru.nightidk.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.EffectCommand;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import ru.nightidk.items.base.InfinityArmor;

import static ru.nightidk.register.MaterialRegister.INFINITY_MATERIAL;

public class InfinityHelmet extends InfinityArmor {
    public InfinityHelmet() {
        super(INFINITY_MATERIAL, Type.HELMET, new Settings().maxCount(1).rarity(Rarity.EPIC));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient()) {
            if (!(entity instanceof PlayerEntity player)) return;
            boolean wear = wearThisItem(player, this);
            if (!wear) {
                player.removeStatusEffect(StatusEffects.NIGHT_VISION);
                return;
            }
            StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.NIGHT_VISION, -1, 0, false, false, false);
            StatusEffectInstance statusEffect = player.getStatusEffect(StatusEffects.NIGHT_VISION);
            if (statusEffect == null)
                player.addStatusEffect(statusEffectInstance, null);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
