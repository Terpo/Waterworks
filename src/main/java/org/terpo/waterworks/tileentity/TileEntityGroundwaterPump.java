package org.terpo.waterworks.tileentity;

import org.terpo.waterworks.helper.GeneralItemStackHandler;
import org.terpo.waterworks.init.WaterworksConfig;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityGroundwaterPump extends TileWaterworks {

	protected FluidStack RESOURCE_WATER = null;
	private static final int invSlots = 5;

	public TileEntityGroundwaterPump() {
		this(WaterworksConfig.GROUNDWATER_PUMP_FILLRATE, WaterworksConfig.GROUNDWATER_PUMP_CAPACITY);
	}
	public TileEntityGroundwaterPump(int fillrate, int capacity) {
		super(invSlots, capacity);
		this.RESOURCE_WATER = new FluidStack(FluidRegistry.WATER, fillrate);

		this.fluidTank.setCanFill(false);
		this.fluidTank.setTileEntity(this);
		this.fluidTank.setFluid(new FluidStack(FluidRegistry.WATER, 16000));

		this.itemStackHandler = new GeneralItemStackHandler(this.INVSIZE) {
			@Override
			protected void onContentsChanged(int slot) {
				// We need to tell the tile entity that something has changed so
				// that the chest contents is persisted
				TileEntityGroundwaterPump.this.markDirty();
			}
		};

		this.itemStackHandler.setInputFlagForIndex(0, true);
		this.itemStackHandler.setInputFlagForIndex(1, false);
		this.itemStackHandler.setOutputFlagForIndex(1, true);
	}

	@Override
	protected void updateServerSide() {

		this.isDirty = fillFluid();

		if (needsUpdate(20)) {
			// refill tank here
		}

		if (this.isDirty) {
			this.markDirty();
			this.isDirty = false;
		}
	}
}
