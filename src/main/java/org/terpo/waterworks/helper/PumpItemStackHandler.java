package org.terpo.waterworks.helper;

import org.terpo.waterworks.init.WaterworksBlocks;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class PumpItemStackHandler extends GeneralItemStackHandler {

	public PumpItemStackHandler() {
		super(1);
	}
	public PumpItemStackHandler(int size) {
		super(size);
	}
	@Override
	protected boolean isValidItemStack(ItemStack stack, int slot) {
		if (slot > 1 && stack.getItem().equals(Item.getItemFromBlock(WaterworksBlocks.water_pipe))) {
			return true;
		}
		return super.isValidItemStack(stack, slot);
	}
}
