package ru.nightidk.deathnote.menu.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import ru.nightidk.deathnote.tiles.ImplementedInventory;

public class InfinityCraftingInventory extends CraftingInventory {
    private final ImplementedInventory inventory;
    private final int size;
    private final ScreenHandler handler;

    public InfinityCraftingInventory(ScreenHandler handler, ImplementedInventory inventory, int size) {
        super(handler, 9, 9);
        this.inventory = inventory;
        this.size = size;
        this.handler = handler;
    }


    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.size; i++) {
            if (!this.inventory.getStack(i).isEmpty())
                return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return slot >= this.size() ? ItemStack.EMPTY : this.inventory.getStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack itemStack = Inventories.splitStack(this.inventory.getItems(), slot, amount);

        this.handler.onContentChanged(this);

        return itemStack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory.getItems(), slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.inventory.setStack(slot, stack);
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.inventory.size(); i++) {
            this.inventory.setStack(i, ItemStack.EMPTY);
        }
    }
}
