package org.terpo.waterworks.helper;

import org.terpo.waterworks.init.WaterworksBlocks;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class PumpItemStackHandler extends GeneralItemStackHandler {

	public PumpItemStackHandler() {
		super(1, null);
	}
	public PumpItemStackHandler(int size, TileEntity tE) {
		super(size, tE);
	}
	@Override
	protected boolean isValidItemStack(ItemStack stack, int slot) {
		if (slot > 1 && WaterworksBlocks.blockItemWaterPipe.equals(stack.getItem())) {
			return true;
		}
		return super.isValidItemStack(stack, slot);
	}
}
