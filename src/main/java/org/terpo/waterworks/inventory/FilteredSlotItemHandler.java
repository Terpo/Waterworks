package org.terpo.waterworks.inventory;

import org.terpo.waterworks.setup.Registration;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class FilteredSlotItemHandler extends FluidSlotItemHandler {

	public FilteredSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition, SlotDefinition slotDefinition) {
		super(itemHandler, index, xPosition, yPosition, slotDefinition);
	}

	@Override
	protected boolean isFilteredItemValid(ItemStack stack) {
		return Registration.waterPipeBlockItem.get().equals(stack.getItem());
	}

}
