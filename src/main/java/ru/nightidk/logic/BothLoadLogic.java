package ru.nightidk.logic;

import static ru.nightidk.DeathNote.LOGGER;

public class BothLoadLogic {
    public static void onClientOrServerLoad() {
        LOGGER.info("[DeathNote] Client and server logic initialized.");
    }
}
