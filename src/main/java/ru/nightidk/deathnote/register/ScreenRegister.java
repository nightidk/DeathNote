package ru.nightidk.deathnote.register;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import ru.nightidk.deathnote.screen.InfinityTableScreen;

public class ScreenRegister {
    public static void register() {
        HandledScreens.register(ScreenHandlerTypesRegister.INFINITY_HANDLER_TYPE, InfinityTableScreen::new);
    }
}
