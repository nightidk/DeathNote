package ru.nightidk.mixin;


import net.fabricmc.api.EnvType;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nightidk.DeathNote;
import ru.nightidk.listeners.serverside.AuthEventListener;

import static net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Shadow public ServerPlayerEntity player;

    @Inject(
            method = "onPlayerAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/server/world/ServerWorld;)V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void onPlayerAction(PlayerActionC2SPacket packet, CallbackInfo ci) {
        if (DeathNote.getEnvType() == EnvType.CLIENT) return;
        if (packet.getAction() == SWAP_ITEM_WITH_OFFHAND) {
            ActionResult result = AuthEventListener.onPlayerAction(this.player);
            if (result == ActionResult.FAIL)
                ci.cancel();
        }
    }

    @Inject(
            method = "onPlayerMove",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/server/world/ServerWorld;)V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void onPlayerMove(PlayerMoveC2SPacket packet, CallbackInfo ci) {
        if (DeathNote.getEnvType() == EnvType.CLIENT) return;
        ActionResult result = AuthEventListener.onPlayerMove(this.player);
        if (result == ActionResult.FAIL)
            ci.cancel();
    }
    @Inject(
            method = "onCreativeInventoryAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/server/world/ServerWorld;)V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void onSetCreateMoveSlot(CreativeInventoryActionC2SPacket packet, CallbackInfo ci) {
        if (DeathNote.getEnvType() == EnvType.CLIENT) return;
        ActionResult result = AuthEventListener.onPlayerTakeItem(this.player);
        if (result == ActionResult.FAIL)
            ci.cancel();
    }

    @Inject(
            method = "onChatMessage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;validateMessage(Ljava/lang/String;Ljava/time/Instant;Lnet/minecraft/network/message/LastSeenMessageList$Acknowledgment;)Ljava/util/Optional;",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    private void onChatMessage(ChatMessageC2SPacket packet, CallbackInfo ci) {
        if (DeathNote.getEnvType() == EnvType.CLIENT) return;
        ActionResult result = AuthEventListener.onPlayerChat(this.player);
        if (result == ActionResult.FAIL)
            ci.cancel();
    }
}
