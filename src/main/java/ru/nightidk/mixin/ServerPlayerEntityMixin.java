package ru.nightidk.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.nightidk.configuration.AuthVariables;
import ru.nightidk.listeners.ModEventListener;
import ru.nightidk.utils.Location;
import ru.nightidk.utils.Pair;
import ru.nightidk.utils.TextStyleUtil;

import java.util.Map;
import java.util.Optional;

import static ru.nightidk.utils.AuthUtil.getAuthMessage;
import static ru.nightidk.utils.AuthUtil.isAuthorized;
import static ru.nightidk.utils.ChatMessageUtil.getStyledComponent;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Unique
    private final ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
    @Unique
    private int kickTimer = 300 * 20;

    @Inject(method = "playerTick()V", at = @At("HEAD"), cancellable = true)
    private void playerTick(CallbackInfo ci) {
        if (!isAuthorized(player)) {
            if (kickTimer <= 0 && player.networkHandler.isConnectionOpen()) {
                player.networkHandler.disconnect(getStyledComponent("Время на авторизацию вышло.", TextStyleUtil.RED.getStyle()));
            } else {
                if ((kickTimer % 200 == 0))
                    player.sendMessage(getAuthMessage(player));
                --kickTimer;
            }
            Optional<Pair<String, Location>> location = AuthVariables.nonAuthPlayer.stream().filter(s -> s.getKey().equals(player.getName().getString())).findAny();
            location.ifPresent(stringLocationEntry -> player.teleport(stringLocationEntry.getValue().getX(), stringLocationEntry.getValue().getY(), stringLocationEntry.getValue().getZ()));
            ci.cancel();
        }
    }

    @Inject(method = "dropSelectedItem(Z)Z", at = @At("HEAD"), cancellable = true)
    private void dropSelectedItem(boolean dropEntireStack, CallbackInfoReturnable<Boolean> cir) {
        ActionResult result = ModEventListener.dropItemEvent(player);

        if (result == ActionResult.FAIL) {
            cir.setReturnValue(false);
        }
    }
}
