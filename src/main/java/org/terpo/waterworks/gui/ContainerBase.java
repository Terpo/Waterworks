package org.terpo.waterworks.gui;

import org.terpo.waterworks.inventory.FluidSlotItemHandler;
import org.terpo.waterworks.inventory.SlotDefinition;
import org.terpo.waterworks.tileentity.TileEntityRainTankWood;

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

		final SlotItemHandler input = new FluidSlotItemHandler(itemHandler, 0, 44, 35, SlotDefinition.I);
		final SlotItemHandler output = new FluidSlotItemHandler(itemHandler, 1, 116, 35, SlotDefinition.O);

		addSlotToContainer(input);
		addSlotToContainer(output);
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
		ItemStack itemstack = ItemStack.EMPTY;
		final Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			final ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			final int invsize = this.te.getINVSIZE();
			if (index < invsize) {
				if (!this.mergeItemStack(itemstack1, invsize, this.inventorySlots.size(), true)) {
					return null;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, invsize, false)) {
				return null;
			}

			if (itemstack1.isEmpty()) {
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
