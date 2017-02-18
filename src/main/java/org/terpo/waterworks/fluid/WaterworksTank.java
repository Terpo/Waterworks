package org.terpo.waterworks.fluid;

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

}
