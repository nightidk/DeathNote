package ru.nightidk.mixin;

import net.fabricmc.api.EnvType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.nightidk.DeathNote;
import ru.nightidk.configuration.AuthVariables;
import ru.nightidk.items.InfinityBoots;
import ru.nightidk.items.InfinityHelmet;
import ru.nightidk.items.base.InfinityArmor;
import ru.nightidk.listeners.both.ModClientOrServerEventListener;
import ru.nightidk.listeners.serverside.ModEventListener;
import ru.nightidk.utils.Location;
import ru.nightidk.utils.Pair;
import ru.nightidk.utils.TextStyleUtil;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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
        ModClientOrServerEventListener.serverArmorCheck(player);
        ActionResult result = ModClientOrServerEventListener.playerInFire(player);
        if (player.isOnFire())
            if (result == ActionResult.FAIL)
                player.setOnFire(false);

        if (DeathNote.getEnvType() == EnvType.CLIENT) return;

        if (isAuthorized(player)) return;
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

    @Inject(method = "dropSelectedItem(Z)Z", at = @At("HEAD"), cancellable = true)
    private void dropSelectedItem(boolean dropEntireStack, CallbackInfoReturnable<Boolean> cir) {
        if (DeathNote.getEnvType() == EnvType.CLIENT) return;
        ActionResult result = ModEventListener.dropItemEvent(player);

        if (result == ActionResult.FAIL) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ActionResult result = ModClientOrServerEventListener.damagePlayer(player, source, amount, DeathNote.getEnvType() == EnvType.SERVER);
        if (result == ActionResult.FAIL) {
            cir.setReturnValue(false);
            return;
        }

        if (player.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof InfinityBoots && source.isIn(DamageTypeTags.IS_FALL)) {
            cir.setReturnValue(false);
            return;
        }

        AtomicInteger countReduce = new AtomicInteger();
        player.getInventory().armor.forEach(i -> {
            if (i.getItem() instanceof InfinityArmor) countReduce.getAndIncrement();
        });

        if (countReduce.get() == 0) return;
        cir.setReturnValue(false);
        if (countReduce.get() == 4) return;
        amount = amount * (1.0F - (0.25F * countReduce.get()));
        player.setHealth(player.getHealth() - amount);
    }
}
