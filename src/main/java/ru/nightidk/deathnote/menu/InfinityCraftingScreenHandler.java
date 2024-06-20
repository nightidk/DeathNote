package ru.nightidk.deathnote.menu;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import ru.nightidk.deathnote.register.ScreenHandlerTypesRegister;
import ru.nightidk.deathnote.slot.InfinityTableCraftingSlot;
import ru.nightidk.deathnote.tiles.InfinityCraftingTableTile;

public class InfinityCraftingScreenHandler extends ScreenHandler {
    private final InfinityCraftingTableTile tile;
    private final Inventory inventory;


    public InfinityCraftingScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, playerInventory.player.getWorld().getBlockEntity(buf.readBlockPos()), new ArrayPropertyDelegate(2));
    }

    public InfinityCraftingScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity, ArrayPropertyDelegate arrayPropertyDelegate) {
        super(ScreenHandlerTypesRegister.INFINITY_HANDLER_TYPE, syncId);
        this.inventory = ((Inventory) blockEntity);
        inventory.onOpen(playerInventory.player);
        this.tile = (InfinityCraftingTableTile) blockEntity;


        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(tile, j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }

        this.addSlot(new InfinityTableCraftingSlot(tile,81, 206, 89));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 39 + j * 18, 196 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int j = 0; j < 9; j++) {
            this.addSlot(new Slot(playerInventory, j, 39 + j * 18, 254));
        }
    }


    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < tile.size()) {
                if (!this.insertItem(originalStack, tile.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, tile.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
        super.onSlotClick(slotIndex, button, actionType, player);
    }
}