package org.terpo.waterworks.tileentity;

import org.terpo.waterworks.helper.PumpItemStackHandler;
import org.terpo.waterworks.init.WaterworksBlocks;
import org.terpo.waterworks.init.WaterworksConfig;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
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

		this.itemStackHandler = new PumpItemStackHandler(this.INVSIZE) {
			@Override
			protected void onContentsChanged(int slot) {
				// We need to tell the tile entity that something has changed so
				// that the chest contents is persisted
				TileEntityGroundwaterPump.this.markDirty();
			}
		};

		// FluidItems Slots
		this.itemStackHandler.setInputFlagForIndex(0, true);
		this.itemStackHandler.setOutputFlagForIndex(1, true);

		// WaterPipe Slots
		this.itemStackHandler.setInputFlagForIndex(2, true);
		this.itemStackHandler.setInputFlagForIndex(3, true);
		this.itemStackHandler.setInputFlagForIndex(4, true);
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

	public int countConnectedWaterPipes() {
		final int x = this.pos.getX();
		int y = this.pos.getY() - 1;
		final int z = this.pos.getZ();
		int count = 0;
		while (y > 0) {
			final IBlockState state = this.world.getBlockState(new BlockPos(x, y, z));
			if (state.getBlock().equals(WaterworksBlocks.water_pipe)) {
				count++;
				y--;
			} else {
				return count;
			}
		}
		return count;
	}
}
