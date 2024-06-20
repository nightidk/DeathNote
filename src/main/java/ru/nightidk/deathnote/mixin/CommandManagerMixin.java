package ru.nightidk.deathnote.mixin;

import com.mojang.brigadier.ParseResults;
import net.fabricmc.api.EnvType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.nightidk.deathnote.DeathNote;
import ru.nightidk.deathnote.listeners.serverside.AuthEventListener;

@Mixin(CommandManager.class)
public class CommandManagerMixin {
    @Inject(method = "execute", at = @At("HEAD"))
    private void checkCanUseCommands(ParseResults<ServerCommandSource> parseResults, String command, CallbackInfoReturnable<Integer> cir) {
        if (DeathNote.getEnvType() == EnvType.CLIENT) return;
        ActionResult result = AuthEventListener.onPlayerCommand(parseResults.getContext().getSource().getPlayer(), command);
        if (result == ActionResult.FAIL) {
            cir.cancel();
        }
    }
}
