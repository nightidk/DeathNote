package ru.nightidk.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.nightidk.listeners.AuthEventListener;
import ru.nightidk.listeners.ModEventListener;

@Mixin(Slot.class)
public abstract class SlotMixin {
    @Inject(method = "canTakeItems(Lnet/minecraft/entity/player/PlayerEntity;)Z", at = @At(value = "HEAD"), cancellable = true)
    private void canTakeItems(PlayerEntity playerEntity, CallbackInfoReturnable<Boolean> cir) {
        if (playerEntity.getWorld().getServer() == null) return;
        ServerPlayerEntity serverPlayer = playerEntity.getWorld().getServer().getPlayerManager().getPlayer(playerEntity.getName().getString());
        if (serverPlayer == null) return;
        ActionResult result = ModEventListener.pickUpItemPre(playerEntity);

        if (result == ActionResult.FAIL) {
            // Canceling the item taking
            serverPlayer.networkHandler.sendPacket(
                    new ScreenHandlerSlotUpdateS2CPacket(
                            -2,
                            0,
                            serverPlayer.getInventory().selectedSlot,
                            serverPlayer.getInventory().getStack(serverPlayer.getInventory().selectedSlot))
            );
            cir.setReturnValue(false);
        }
    }
}
