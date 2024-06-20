package ru.nightidk.deathnote.api.crafting;

import lombok.Getter;
import net.minecraft.item.ItemStack;
import ru.nightidk.deathnote.utils.Pair;

@Getter
public class Pattern {
    private final String patternCode;
    private final ItemStack itemStack;
    public Pattern(Pair<String, ItemStack> pair) {
        this.patternCode = pair.getKey();
        this.itemStack = pair.getValue();
    }
}