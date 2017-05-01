package org.terpo.waterworks.helper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class PumpItemStackHandler extends GeneralItemStackHandler {

	public PumpItemStackHandler() {
		super(1);
	}
	PumpItemStackHandler(int size) {
		super(size);
	}
	@Override
	protected boolean isValidItemStack(ItemStack stack, int slot) {
		if (slot <= 1) {
			return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		}
		return true;

	}
}
