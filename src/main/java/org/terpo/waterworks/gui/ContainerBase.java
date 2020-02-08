package org.terpo.waterworks.gui;

import javax.annotation.Nullable;

import org.terpo.waterworks.inventory.FilteredFluidSlotItemHandler;
import org.terpo.waterworks.inventory.SlotDefinition;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBase extends Container {

	private final TileWaterworks tileWaterworks;

	public ContainerBase(@Nullable ContainerType<?> type, int windowId, IInventory playerInv, TileEntity te) {
		super(type, windowId);
		this.tileWaterworks = (TileWaterworks) te;
		// both inventories. The two calls below make sure that slots are defined for both
		// inventories.
		this.tileWaterworks.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(this::addOwnSlots);
		addPlayerSlots(playerInv);
	}

	protected void addOwnSlots(IItemHandler handler) {
		// Tile Entity, Slot 0-1, Slot IDs 0-1
		// 0 - Input
		// 1 - Output

		final SlotItemHandler input = new FilteredFluidSlotItemHandler(handler, 0, 44, 35, SlotDefinition.I);
		final SlotItemHandler output = new FilteredFluidSlotItemHandler(handler, 1, 116, 35, SlotDefinition.O);

		((FilteredFluidSlotItemHandler) input).addItemToFilter(Items.GLASS_BOTTLE);
		((FilteredFluidSlotItemHandler) input).addItemToFilter(Items.POTION);

		addSlot(input);
		addSlot(output);
	}

	private void addPlayerSlots(IInventory playerInv) {
		final int SLOTWIDTH = 18;
		// Slots for the main inventory
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				final int x = 8 + col * SLOTWIDTH;
				final int y = row * SLOTWIDTH + 84;
				this.addSlot(new Slot(playerInv, col + row * 9 + 9, x, y));
			}
		}

		// Slots for the hotbar
		for (int row = 0; row < 9; ++row) {
			final int x = 8 + row * 18;
			final int y = 142;
			this.addSlot(new Slot(playerInv, row, x, y));
		}

	}

	// @Nullable
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		final Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			final ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			final int invsize = this.tileWaterworks.getInventorySize();
			if (index < invsize) {
				if (!this.mergeItemStack(itemstack1, invsize, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, invsize, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(IWorldPosCallable.of(this.tileWaterworks.getWorld(), this.tileWaterworks.getPos()), playerIn,
				this.tileWaterworks.getBlockState().getBlock());
	}
	public TileWaterworks getTileWaterworks() {
		return this.tileWaterworks;
	}
}
