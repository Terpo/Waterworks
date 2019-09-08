package org.terpo.waterworks.fluid;

import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraftforge.fluids.capability.templates.FluidTank;

public class WaterworksTank extends FluidTank {

	private final TileWaterworks tile;
	public WaterworksTank(int capacity, TileWaterworks tileWaterworks) {
		super(capacity);
		this.tile = tileWaterworks;
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

}
