package ru.nightidk.deathnote.items;

import io.github.ladysnake.pal.AbilitySource;
import io.github.ladysnake.pal.Pal;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import ru.nightidk.deathnote.DeathNote;
import ru.nightidk.deathnote.items.abilities.InfinityAbilities;
import ru.nightidk.deathnote.items.base.InfinityArmor;

import java.util.Objects;

import static ru.nightidk.deathnote.register.MaterialRegister.INFINITY_MATERIAL;

public class InfinityLeggings extends InfinityArmor {
    private static final Identifier INFINITY_LEGGINGS_ABILITY_SOURCE_ID = new Identifier(DeathNote.MOD_ID, "infinity_leggings");
    private static final AbilitySource INFINITY_LEGGINGS_ABILITY_SOURCE = Pal.getAbilitySource(INFINITY_LEGGINGS_ABILITY_SOURCE_ID);

    public InfinityLeggings() {
        super(INFINITY_MATERIAL, Type.LEGGINGS, new Settings().maxCount(1).rarity(Rarity.EPIC));
    }

    public static boolean isAllowingSpeedUp(PlayerEntity player) {
        return INFINITY_LEGGINGS_ABILITY_SOURCE.grants(player, InfinityAbilities.SPEED_UP) && INFINITY_LEGGINGS_ABILITY_SOURCE.isActivelyGranting(player, InfinityAbilities.SPEED_UP);
    }

    public static void allowSpeedUp(PlayerEntity playerEntity) {
        if (!playerEntity.getWorld().isClient()) {
            INFINITY_LEGGINGS_ABILITY_SOURCE.grantTo(playerEntity, InfinityAbilities.SPEED_UP);

            EntityAttributeInstance genericMovementSpeed = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            EntityAttributeInstance genericFlyingSpeed = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_FLYING_SPEED);

            EntityAttributeModifier modifierWalkSpeed = new EntityAttributeModifier("addWalk", 0.3F, EntityAttributeModifier.Operation.ADDITION);
            EntityAttributeModifier modifierFlySpeed = new EntityAttributeModifier("addFly", 0.15F, EntityAttributeModifier.Operation.ADDITION);

            if (genericFlyingSpeed != null && genericFlyingSpeed.getModifiers(EntityAttributeModifier.Operation.ADDITION).stream().filter(s -> Objects.equals(s.getName(), modifierFlySpeed.getName())).findFirst().isEmpty())
                genericFlyingSpeed.addTemporaryModifier(modifierFlySpeed);
            if (genericMovementSpeed != null && genericMovementSpeed.getModifiers(EntityAttributeModifier.Operation.ADDITION).stream().filter(s -> Objects.equals(s.getName(), modifierWalkSpeed.getName())).findFirst().isEmpty())
                genericMovementSpeed.addTemporaryModifier(modifierWalkSpeed);
        }
    }

    public static void dontAllowSpeedUp(PlayerEntity playerEntity) {
        if (!playerEntity.getWorld().isClient()) {
            INFINITY_LEGGINGS_ABILITY_SOURCE.revokeFrom(playerEntity, InfinityAbilities.SPEED_UP);

            EntityAttributeInstance genericMovementSpeed = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            EntityAttributeInstance genericFlyingSpeed = playerEntity.getAttributeInstance(EntityAttributes.GENERIC_FLYING_SPEED);
            if (genericFlyingSpeed != null)
                genericFlyingSpeed.clearModifiers();
            if (genericMovementSpeed != null)
                genericMovementSpeed.clearModifiers();
        }
    }
}
