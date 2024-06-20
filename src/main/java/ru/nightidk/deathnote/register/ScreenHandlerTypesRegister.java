package ru.nightidk.deathnote.register;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import ru.nightidk.deathnote.DeathNote;
import ru.nightidk.deathnote.menu.InfinityCraftingScreenHandler;

public class ScreenHandlerTypesRegister {
    public static ScreenHandlerType<InfinityCraftingScreenHandler> INFINITY_HANDLER_TYPE;

    public static void register() {
        INFINITY_HANDLER_TYPE = Registry.register(Registries.SCREEN_HANDLER, new Identifier(DeathNote.MOD_ID, "infinity_type"), new ExtendedScreenHandlerType<>(InfinityCraftingScreenHandler::new));
    }
}
