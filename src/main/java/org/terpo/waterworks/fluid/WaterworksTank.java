package org.terpo.waterworks.fluid;

import org.terpo.waterworks.tileentity.BaseTileEntity;

import net.minecraftforge.fluids.FluidTank;

public class WaterworksTank extends FluidTank {

	public WaterworksTank(int capacity) {
		super(capacity);
	}

	public boolean isEmpty() {
		return this.getFluidAmount() == 0;
	}

	public boolean isFull() {
		return this.getFluidAmount() >= this.getCapacity();
	}
	@Override
	protected void onContentsChanged() {
		if (this.tile instanceof BaseTileEntity) {
			((BaseTileEntity) this.tile).setDirty(true);
		}
	}

}
