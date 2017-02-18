package org.terpo.waterworks.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class FilteredSlot extends Slot {

	private final Class<?> clazz;
	public FilteredSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, Class<?> clazz) {
		super(inventoryIn, index, xPosition, yPosition);
		this.clazz = clazz;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		if (this.clazz.isInstance(stack)) {
			return true;
		}

		return false;
	}

}
