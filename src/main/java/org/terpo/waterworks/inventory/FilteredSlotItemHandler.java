package org.terpo.waterworks.inventory;

import org.terpo.waterworks.init.WaterworksBlocks;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class FilteredSlotItemHandler extends FluidSlotItemHandler {

	public FilteredSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition,
			SlotDefinition slotDefinition) {
		super(itemHandler, index, xPosition, yPosition, slotDefinition);
	}

	@Override
	protected boolean isFilteredItemValid(ItemStack stack) {
		return (stack.getItem().equals(Item.getItemFromBlock(WaterworksBlocks.water_pipe)));
	}

}
