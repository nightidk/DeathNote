package ru.nightidk.deathnote.listeners.serverside;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static ru.nightidk.deathnote.utils.AuthUtil.isAuthorized;

public class AuthEventListener {
    public static ActionResult useEntityEvent(PlayerEntity player, World level, Hand interactionHand, Entity entity, EntityHitResult entityHitResult) {
        if (level.getServer() == null) return ActionResult.PASS;
        ServerPlayerEntity serverPlayer = level.getServer().getPlayerManager().getPlayer(player.getName().getString());
        if (serverPlayer == null) return ActionResult.PASS;
        if (!isAuthorized(serverPlayer)) {
//            sendChatMessageToPlayer(serverPlayer, getAuthMessage(serverPlayer));
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }

    public static boolean breakBlockEvent(World level, PlayerEntity player, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        if (level.getServer() == null) return true;
        ServerPlayerEntity serverPlayer = level.getServer().getPlayerManager().getPlayer(player.getName().getString());
        if (serverPlayer == null) return true;
        if (!isAuthorized(serverPlayer)) {
//            sendChatMessageToPlayer(serverPlayer, getAuthMessage(serverPlayer));
            return false;
        }
        return true;
    }

    public static ActionResult useBlockEvent(PlayerEntity player, World level, Hand interactionHand, BlockHitResult blockHitResult) {
        if (level.getServer() == null) return ActionResult.PASS;
        ServerPlayerEntity serverPlayer = level.getServer().getPlayerManager().getPlayer(player.getName().getString());
        if (serverPlayer == null) return ActionResult.PASS;
        if (!isAuthorized(serverPlayer)) {
//            sendChatMessageToPlayer(serverPlayer, getAuthMessage(serverPlayer));
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }

    public static ActionResult attackEntityEvent(PlayerEntity player, World level, Hand interactionHand, Entity entity, EntityHitResult entityHitResult) {
        if (level.getServer() == null) return ActionResult.PASS;
        ServerPlayerEntity serverPlayer = level.getServer().getPlayerManager().getPlayer(player.getName().getString());
        if (serverPlayer == null) return ActionResult.PASS;
        if (!isAuthorized(serverPlayer)) {
//            sendChatMessageToPlayer(serverPlayer, getAuthMessage(serverPlayer));
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }

    public static TypedActionResult<ItemStack> useItemEvent(PlayerEntity player, World level, Hand interactionHand) {
        if (level.getServer() == null) return TypedActionResult.pass(ItemStack.EMPTY);
        ServerPlayerEntity serverPlayer = level.getServer().getPlayerManager().getPlayer(player.getName().getString());
        if (serverPlayer == null) return TypedActionResult.pass(ItemStack.EMPTY);
        if (!isAuthorized(serverPlayer)) {
//            sendChatMessageToPlayer(serverPlayer, getAuthMessage(serverPlayer));
            return TypedActionResult.fail(ItemStack.EMPTY);
        }
        return TypedActionResult.pass(ItemStack.EMPTY);
    }

    public static ActionResult onPlayerCommand(ServerPlayerEntity player, String command) {
        if (player == null) return ActionResult.PASS;
        if (isAuthorized(player))
            return ActionResult.PASS;
        else {
            if (command.startsWith("login")
                || command.startsWith("register")
                    || command.startsWith("l ")
                    || command.startsWith("reg ")
            ) return ActionResult.PASS;
            else return ActionResult.FAIL;
        }
    }

    public static ActionResult onPlayerMove(ServerPlayerEntity player) {
        if (!isAuthorized(player)) {
//            sendChatMessageToPlayer(player, getAuthMessage(player));
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }

    public static ActionResult onPlayerChat(ServerPlayerEntity player) {
        if (!isAuthorized(player)) {
//            sendChatMessageToPlayer(player, getAuthMessage(player));
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }

    public static ActionResult onPlayerAction(ServerPlayerEntity player) {
        if (!isAuthorized(player)) {
//            sendChatMessageToPlayer(player, getAuthMessage(player));
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }

    public static ActionResult onPlayerTakeItem(ServerPlayerEntity player) {
        if (!isAuthorized(player)) {
//            sendChatMessageToPlayer(player, getAuthMessage(player));
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }
}
