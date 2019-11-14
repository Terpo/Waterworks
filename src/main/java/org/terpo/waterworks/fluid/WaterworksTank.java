package org.terpo.waterworks.fluid;

import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class WaterworksTank extends FluidTank {

	private final TileWaterworks tile;
	private boolean allowFilling;
	public WaterworksTank(int capacity, TileWaterworks tileWaterworks) {
		super(capacity);
		this.tile = tileWaterworks;
		this.allowFilling = false;
	}

	@Override
	public boolean isEmpty() {
		return this.getFluidAmount() == 0;
	}

	public boolean isFull() {
		return this.getFluidAmount() >= this.getCapacity();
	}
	@Override
	protected void onContentsChanged() {
		this.tile.setDirty(true);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		if (this.allowFilling) {
			return super.fill(resource, action);
		}
		return 0;
	}

	public int fillInternal(FluidStack resource, FluidAction action) {
		return super.fill(resource, action);
	}

	public WaterworksTank allowFilling(boolean newAllowFilling) {
		setAllowFilling(newAllowFilling);
		return this;
	}

	public boolean isAllowFilling() {
		return this.allowFilling;
	}

	public void setAllowFilling(boolean allowFilling) {
		this.allowFilling = allowFilling;
	}

}
