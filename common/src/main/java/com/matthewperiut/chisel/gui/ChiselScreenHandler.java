package com.matthewperiut.chisel.gui;

import com.matthewperiut.chisel.Chisel;
import com.matthewperiut.chisel.inventory.ChiselInventory;
import com.matthewperiut.chisel.inventory.InventoryNbtUtil;
import com.matthewperiut.chisel.item.ChiselItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class ChiselScreenHandler extends ScreenHandler {
    private final ChiselInventory inventory;
    private final NbtCompound nbtInventory;

    public ChiselScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new ChiselInventory());
    }

    public ChiselScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        this(syncId, playerInventory, inventory, new NbtCompound());
    }

    public ChiselScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, NbtCompound compound) {
        super(Chisel.CHISEL_SCREEN_HANDLER.get(), syncId);
        checkSize(inventory, 61);
        this.inventory = (ChiselInventory) inventory;
        nbtInventory = compound;

        // input slot
        this.addSlot(new Slot(inventory, 0, 8, 145));

        // output slots
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 6; y++) {
                this.addSlot(new SlotChiselOutput(inventory, 1 + x + 10 * y, 62 + 18 * x - 54, 8 + 18 * y + 7));
            }
        }

        // inv
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 3; y++) {
                this.addSlot(new Slot(playerInventory, 9 + x + 9 * y, 71 + 18 * x - 45, 120 + 18 * y + 7));
            }
        }

        // hotbar
        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(playerInventory, x, 71 + 18 * x - 45, 178 + 7));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    // Shift + Player Inv Slot
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack(); // stack in slot clicked
            newStack = originalStack.copy();
            if (originalStack.getItem().equals(Chisel.chiselSupplier.get())) {
                return ItemStack.EMPTY;
            }
            if (invSlot < this.inventory.size()) {
                // slot clicked was in chisel inv
                if (invSlot != 0) {
                    originalStack.setCount(this.slots.get(0).getStack().getCount());
                    ChiselItem.chiselSound(player.getWorld(), player.getBlockPos());
                }
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    this.inventory.refresh(this.slots.get(0).getStack().getItem());
                    return ItemStack.EMPTY;
                } else {
                    this.inventory.clear();
                }

            } else if (!this.insertItem(originalStack, 0, 1, false)) {
                // slot clicked was not in chisel inv
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
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        ItemStack hand = player.getHandItems().iterator().next();
        if (!hand.isOf(Chisel.chiselSupplier.get()))
        {
            ItemStack itemStack = ItemStack.EMPTY;
            // Prevents duplication
            for (int i = 0; i < player.getInventory().size(); i++)
            {
                if(player.getInventory().getStack(i).isOf(Chisel.chiselSupplier.get()))
                {
                    itemStack = player.getInventory().getStack(i);
                    player.getInventory().removeStack(i);
                }
            }
            player.getInventory().setStack(0, itemStack);
        }
        hand.getOrCreateNbt().copyFrom(InventoryNbtUtil.createCompound(inventory));
    }

    @Override
    public void onSlotClick(int i, int j, SlotActionType actionType, PlayerEntity playerEntity) {
        if (i >= 0 && i < this.slots.size()) {
            Slot slot = this.slots.get(i);
            if (slot.getStack().getItem() instanceof ChiselItem) {
                return;
            }
        }
        super.onSlotClick(i, j, actionType, playerEntity);
    }

    private static class SlotChiselOutput extends Slot {
        public SlotChiselOutput(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public void onTakeItem(PlayerEntity player, ItemStack stack) {
            this.inventory.clear();
            super.onTakeItem(player, stack);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return false;
        }

        @Override
        public int getMaxItemCount() {
            return 1;
        }

    }

}