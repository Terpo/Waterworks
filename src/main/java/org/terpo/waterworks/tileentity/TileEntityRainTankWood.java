package org.terpo.waterworks.tileentity;

import org.terpo.waterworks.helper.GeneralItemStackHandler;
import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.init.WaterworksTileEntities;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityRainTankWood extends TileWaterworks {

	protected FluidStack fluidResource = null;
	private static final int INVENTORY_SLOT_COUNT = 2;

	public TileEntityRainTankWood() {
		this(WaterworksTileEntities.rainTankWood, WaterworksConfig.rainCollection.woodenRainTankFillrate,
				WaterworksConfig.rainCollection.woodenRainTankCapacity);
	}

	public TileEntityRainTankWood(TileEntityType<?> tileEntityTypeIn) {
		this(tileEntityTypeIn, WaterworksConfig.rainCollection.woodenRainTankFillrate,
				WaterworksConfig.rainCollection.woodenRainTankCapacity);
	}
	public TileEntityRainTankWood(TileEntityType<?> tileEntityTypeIn, int fillrate, int capacity) {
		super(tileEntityTypeIn, INVENTORY_SLOT_COUNT, capacity);
		this.fluidResource = new FluidStack(FluidRegistry.WATER, fillrate);

		this.fluidTank.setCanFill(false);
		this.fluidTank.setTileEntity(this);

		this.itemStackHandler = new GeneralItemStackHandler(this.inventorySize, this);

		this.itemStackHandler.setInputFlagForIndex(0, true);
		this.itemStackHandler.setOutputFlagForIndex(1, true);
	}

	@Override
	protected void updateServerSide() {

		if (fillFluid()) {
			this.isDirty = true;
		}

		if (needsUpdate(20) && isRefilling()) {
			this.isDirty = true;
		}
		super.updateServerSide();
	}

	@Override
	public boolean shouldRefresh(World worldIn, BlockPos posIn, BlockState oldState, BlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	public int getStateLevel() {
		return Math.round((this.fluidTank.getFluidAmount() * 4.0f / this.fluidTank.getCapacity()));
	}

	protected boolean isRefilling() {
		final BlockPos position = getPos().up();

		if (this.world.isRainingAt(position)) {
			this.fluidTank.fillInternal(this.fluidResource, true);
			return true;
		}
		return false;
	}
}
