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

}
