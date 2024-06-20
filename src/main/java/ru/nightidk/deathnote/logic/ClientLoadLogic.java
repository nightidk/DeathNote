package ru.nightidk.deathnote.logic;

import ru.nightidk.deathnote.register.ScreenRegister;

import static ru.nightidk.deathnote.DeathNote.LOGGER;

public class ClientLoadLogic {
    public static void onClientLoad() {
        ScreenRegister.register();
        LOGGER.info("[DeathNote] Registered screens.");
        LOGGER.info("[DeathNote] Client logic initialized.");
    }
}
