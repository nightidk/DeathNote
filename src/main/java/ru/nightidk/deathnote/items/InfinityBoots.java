package ru.nightidk.deathnote.items;

import net.minecraft.util.Rarity;
import ru.nightidk.deathnote.items.base.InfinityArmor;

import static ru.nightidk.deathnote.register.MaterialRegister.INFINITY_MATERIAL;

public class InfinityBoots extends InfinityArmor {

    public static float JUMP_MODIFIER = 0.5f;

    public InfinityBoots() {
        super(INFINITY_MATERIAL, Type.BOOTS, new Settings().maxCount(1).rarity(Rarity.EPIC));
    }

}
