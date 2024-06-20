package ru.nightidk.deathnote;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.nightidk.deathnote.logic.ClientLoadLogic;

@Environment(EnvType.CLIENT)
public class DeathNoteClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientLoadLogic.onClientLoad();
    }
}
