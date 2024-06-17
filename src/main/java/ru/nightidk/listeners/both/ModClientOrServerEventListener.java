package ru.nightidk.listeners.both;

import io.github.ladysnake.pal.VanillaAbilities;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import ru.nightidk.items.InfinityBoots;
import ru.nightidk.items.InfinityChestplate;
import ru.nightidk.items.InfinityHelmet;
import ru.nightidk.items.InfinityLeggings;
import ru.nightidk.items.abilities.InfinityAbilities;

import static ru.nightidk.items.InfinityChestplate.*;
import static ru.nightidk.items.InfinityHelmet.*;
import static ru.nightidk.items.InfinityLeggings.*;
import static ru.nightidk.utils.ArmorUtils.hasFullInfinityArmor;
import static ru.nightidk.utils.AuthUtil.isAuthorized;

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
        } else {
            if (InfinityAbilities.SPEED_UP.isEnabledFor(player) && isAllowingSpeedUp(player))
                dontAllowSpeedUp(player);
        }
    }
}
