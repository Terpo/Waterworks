package org.terpo.waterworks.inventory;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class FilteredFluidSlotItemHandler extends FluidSlotItemHandler {

	ArrayList<Item> filter = new ArrayList<>();

	public FilteredFluidSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition,
			SlotDefinition slotDefinition) {
		super(itemHandler, index, xPosition, yPosition, slotDefinition);
	}

	@Override
	protected boolean additionalFilters(ItemStack stack) {
		final Item item = stack.getItem();
		if (!this.filter.isEmpty()) {
			return this.filter.stream().filter(filterItem -> filterItem.equals(item)).findFirst().isPresent();
		}
		return false;
	}

	public void addItemToFilter(Item item) {
		if (!(this.filter.stream().filter(filterItem -> filterItem.equals(item)).findFirst().isPresent())) {
			this.filter.add(item);
		}

	}

}
