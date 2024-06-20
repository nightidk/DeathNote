package ru.nightidk.deathnote.listeners.both;

import io.github.ladysnake.pal.VanillaAbilities;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import ru.nightidk.deathnote.items.InfinityBoots;
import ru.nightidk.deathnote.items.InfinityChestplate;
import ru.nightidk.deathnote.items.InfinityHelmet;
import ru.nightidk.deathnote.items.InfinityLeggings;
import ru.nightidk.deathnote.items.abilities.InfinityAbilities;

import static ru.nightidk.deathnote.items.InfinityChestplate.*;
import static ru.nightidk.deathnote.items.InfinityHelmet.*;
import static ru.nightidk.deathnote.items.InfinityLeggings.*;
import static ru.nightidk.deathnote.utils.ArmorUtils.hasFullInfinityArmor;
import static ru.nightidk.deathnote.utils.AuthUtil.isAuthorized;

public class ModClientOrServerEventListener {
    public static ActionResult damagePlayer(ServerPlayerEntity player, DamageSource source, float damage, boolean checkAuth) {
        if (checkAuth)
            if (!isAuthorized(player)) return ActionResult.FAIL;
        return ActionResult.PASS;
    }

    public static ActionResult playerInFire(ServerPlayerEntity player) {
        if (hasFullInfinityArmor(player)) return ActionResult.FAIL;
        return ActionResult.PASS;
    }


    public static <T extends PlayerEntity> void clientArmorCheck(T player) {
        Item boots = player.getEquippedStack(EquipmentSlot.FEET).getItem();
        if (boots instanceof InfinityBoots)
            player.setStepHeight(1.0F);
        else
            player.setStepHeight(0.6F);

    }

    public static void serverArmorCheck(ServerPlayerEntity player) {
        Item chestplate = player.getEquippedStack(EquipmentSlot.CHEST).getItem();
        Item helmet = player.getEquippedStack(EquipmentSlot.HEAD).getItem();
        Item leggings = player.getEquippedStack(EquipmentSlot.LEGS).getItem();
        if (helmet instanceof InfinityHelmet) {
            if (!InfinityAbilities.NIGHT_VISION.isEnabledFor(player) || !isAllowingNightVision(player))
                allowNightVision(player);
            if (InfinityAbilities.NIGHT_VISION.isEnabledFor(player) && isAllowingNightVision(player))
                if (!player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
                    StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.NIGHT_VISION, -1, 0, false, false, false);
                    StatusEffectInstance statusEffect = player.getStatusEffect(StatusEffects.NIGHT_VISION);
                    if (statusEffect == null)
                        player.addStatusEffect(statusEffectInstance, null);
                }
            player.getHungerManager().setFoodLevel(20);
        } else {
            if (InfinityAbilities.NIGHT_VISION.isEnabledFor(player) && isAllowingNightVision(player))
                dontAllowNightVision(player);
        }
        if (chestplate instanceof InfinityChestplate) {
            if (!hasIndirectFlight(player)) {
                if (!VanillaAbilities.FLYING.isEnabledFor(player) || !isAllowingFlight(player))
                    allowFlying(player);
            }
        } else {
            if (!hasIndirectFlight(player)) {
                if (VanillaAbilities.FLYING.isEnabledFor(player) & isAllowingFlight(player))
                    dontAllowFlying(player);
            }
        }
        if (leggings instanceof InfinityLeggings) {
            if (!InfinityAbilities.SPEED_UP.isEnabledFor(player) || !isAllowingSpeedUp(player))
                allowSpeedUp(player);
            if (InfinityAbilities.SPEED_UP.isEnabledFor(player) && isAllowingSpeedUp(player)) {
                EntityAttributeInstance genericMovementSpeed = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                EntityAttributeInstance genericFlyingSpeed = player.getAttributeInstance(EntityAttributes.GENERIC_FLYING_SPEED);
                EntityAttributeModifier modifierWalkSpeed = new EntityAttributeModifier("addWalk", 0.3F, EntityAttributeModifier.Operation.ADDITION);
                EntityAttributeModifier modifierFlySpeed = new EntityAttributeModifier("addFly", 0.15F, EntityAttributeModifier.Operation.ADDITION);
                if (genericMovementSpeed != null && !genericMovementSpeed.hasModifier(modifierWalkSpeed))
                    genericMovementSpeed.addTemporaryModifier(modifierWalkSpeed);
                if (genericFlyingSpeed != null && !genericFlyingSpeed.hasModifier(modifierFlySpeed))
                    genericFlyingSpeed.addTemporaryModifier(modifierFlySpeed);
            }
        } else {
            if (InfinityAbilities.SPEED_UP.isEnabledFor(player) && isAllowingSpeedUp(player))
                dontAllowSpeedUp(player);
        }
    }
}
