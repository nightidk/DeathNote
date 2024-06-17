package ru.nightidk.items.abilities;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.NbtCompound;

@Setter
@Getter
public class InfinityAbilitiesProps {
    private boolean nightVision = false;
    private boolean speedUp = false;

    public InfinityAbilitiesProps() {
    }

    public void writeNbt(NbtCompound nbt) {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putBoolean("nightVision", this.nightVision);
        nbtCompound.putBoolean("speedUp", this.speedUp);
        nbt.put("infAbilities", nbtCompound);
    }

    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("infAbilities", 10)) {
            NbtCompound nbtCompound = nbt.getCompound("abilities");
            this.nightVision = nbtCompound.getBoolean("nightVision");
            this.speedUp = nbtCompound.getBoolean("speedUp");
        }
    }

}
