package ru.nightidk.deathnote.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import ru.nightidk.deathnote.menu.inventory.InfinityCraftingInventory;
import ru.nightidk.deathnote.register.RecipeRegister;
import ru.nightidk.deathnote.tiles.ImplementedInventory;
import ru.nightidk.deathnote.tiles.InfinityCraftingTableTile;

public class InfinityTableCraftingSlot extends Slot {
    private final InfinityCraftingTableTile craftingInventory;


    public InfinityTableCraftingSlot(InfinityCraftingTableTile craftingInventory, int index, int x, int y) {
        super(craftingInventory, index, x, y);
        this.craftingInventory = craftingInventory;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    protected void onCrafted(ItemStack stack) {
        super.onCrafted(stack);
//        this.craftingInventory.removeOnCraft();
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        DefaultedList<ItemStack> remaining = player.getWorld().getRecipeManager().getRemainingStacks(RecipeRegister.INFINITY_TABLE_RECIPE, this.craftingInventory, player.getWorld());

        for (int i = 0; i < remaining.size(); i++) {
            var slotStack = this.craftingInventory.getStack(i);
            var remainingStack = remaining.get(i);

            if (!slotStack.isEmpty()) {
                this.craftingInventory.removeStack(i, 1);
                slotStack = this.craftingInventory.getStack(i);
            }

            if (!remainingStack.isEmpty()) {
                if (slotStack.isEmpty()) {
                    this.craftingInventory.setStack(i, remainingStack);
                } else if (ItemStack.areItemsEqual(slotStack, remainingStack) && ItemStack.canCombine(slotStack, remainingStack)) {
                    remainingStack.increment(slotStack.getCount());
                    this.inventory.setStack(i, remainingStack);
                } else if (!player.getInventory().insertStack(remainingStack)) {
                    player.dropItem(remainingStack, false);
                }
            }
        }
        markDirty();
    }
}
