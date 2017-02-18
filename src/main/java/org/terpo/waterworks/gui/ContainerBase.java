package org.terpo.waterworks.gui;

import org.terpo.waterworks.tileentity.TileEntityRainTankWood;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBase extends Container {

	private final TileEntityRainTankWood te;

	public ContainerBase(IInventory playerInv, TileEntityRainTankWood te) {
		this.te = te;

		// This container references items out of our own inventory (the 9 slots we hold
		// ourselves)
		// as well as the slots from the player inventory so that the user can transfer
		// items between
		// both inventories. The two calls below make sure that slots are defined for both
		// inventories.
		addOwnSlots();
		addPlayerSlots(playerInv);
	}

	private void addOwnSlots() {
		final IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		// Tile Entity, Slot 0-1, Slot IDs 0-1
		// 0 - Input
		// 1 - Output
		addSlotToContainer(new SlotItemHandler(itemHandler, 0, 44, 35));
		addSlotToContainer(new SlotItemHandler(itemHandler, 1, 116, 35));
	}

	private void addPlayerSlots(IInventory playerInv) {

		final int SLOTWIDTH = 18;
		// Slots for the main inventory
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				final int x = 8 + col * SLOTWIDTH;
				final int y = row * SLOTWIDTH + 84;
				this.addSlotToContainer(new Slot(playerInv, col + row * 9 + 9, x, y));
			}
		}

		// Slots for the hotbar
		for (int row = 0; row < 9; ++row) {
			final int x = 8 + row * 18;
			final int y = 142;
			this.addSlotToContainer(new Slot(playerInv, row, x, y));
		}

	}

	// @Nullable
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = null;
		final Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			final ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < TileWaterworks.SIZE) {
				if (!this.mergeItemStack(itemstack1, TileWaterworks.SIZE, this.inventorySlots.size(), true)) {
					return null;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, TileWaterworks.SIZE, false)) {
				return null;
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.te.canInteractWith(playerIn);
	}

}
