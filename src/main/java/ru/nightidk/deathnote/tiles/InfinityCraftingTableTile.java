package ru.nightidk.deathnote.tiles;

import lombok.Getter;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import ru.nightidk.deathnote.api.crafting.ISpecialRecipe;
import ru.nightidk.deathnote.menu.InfinityCraftingScreenHandler;
import ru.nightidk.deathnote.register.TileRegister;

import java.util.Optional;

public class InfinityCraftingTableTile extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(82, ItemStack.EMPTY);

    public InfinityCraftingTableTile(BlockPos pos, BlockState state) {
        super(TileRegister.INFINITY_TABLE_TILE_TYPE, pos, state);
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new InfinityCraftingScreenHandler(syncId, playerInventory, this, new ArrayPropertyDelegate(2));
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("tile.deathnote.infinity_table");
    }

    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
    }

    @Override
    public void markDirty() {
        assert world != null;
        world.updateListeners(pos, getCachedState(), getCachedState(), 3);
        super.markDirty();
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient()) return;

        this.craftItem();
    }

    private void craftItem() {
        Optional<ISpecialRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) {
            this.setStack(81, ItemStack.EMPTY);
            return;
        }
        if (this.getStack(81) == ItemStack.EMPTY)
            this.setStack(81, new ItemStack(recipe.get().getOutput(null).getItem()));
    }

//    public void removeOnCraft() {
//        for (int i = 0; i < 81; i++)
//            if (this.getStack(i).getCount() == 1)
//                this.setStack(i, ItemStack.EMPTY);
//            else
//                this.getStack(i).decrement(1);
//    }

    private Optional<ISpecialRecipe> getCurrentRecipe() {
        SimpleInventory inv = new SimpleInventory(this.size() - 1);
        for(int i = 0; i < this.size() - 1; i++) {
            inv.setStack(i, this.getStack(i));
        }

        if (getWorld() == null) return Optional.empty();

        return getWorld().getRecipeManager().getFirstMatch(ISpecialRecipe.Type.INSTANCE, inv, getWorld());
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
