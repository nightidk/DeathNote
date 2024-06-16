package ru.nightidk.listeners.both;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

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
}
