package org.terpo.waterworks.tileentity;

import org.terpo.waterworks.helper.GeneralItemStackHandler;
import org.terpo.waterworks.init.WaterworksConfig;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityRainTankWood extends TileWaterworks {

	protected FluidStack fluidResource = null;
	private static final int INVENTORY_SLOT_COUNT = 2;

	public TileEntityRainTankWood() {
		this(WaterworksConfig.rainCollection.woodenRainTankFillrate,
				WaterworksConfig.rainCollection.woodenRainTankCapacity);
	}
	public TileEntityRainTankWood(int fillrate, int capacity) {
		super(INVENTORY_SLOT_COUNT, capacity);
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

		if (needsUpdate(20)) {
			if (isRefilling()) {
				this.isDirty = true;
			}
		}
		super.updateServerSide();
	}

	@Override
	public boolean shouldRefresh(World worldIn, BlockPos posIn, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	public int getStateLevel() {
		return Math.round((this.fluidTank.getFluidAmount() * 4.0f / this.fluidTank.getCapacity()));
	}

	protected boolean isRefilling() {
		final BlockPos position = getPos().up();

		if (isRainingAtPosition(position)) {
			this.fluidTank.fillInternal(this.fluidResource, true);
			return true;
		}
		return false;
	}

	// TODO use the official version and not the copy as Tough as nails should be fixed by
	// now
	protected boolean isRainingAtPosition(BlockPos posi) {
		// copy of isRainingAt in World.class
		if (!this.world.isRaining()) {
			return false;
		} else if (!this.world.canBlockSeeSky(posi)) {
			return false;
		} else if (this.world.getPrecipitationHeight(posi).getY() > posi.getY()) {
			return false;
		} else {
			final Biome biome = this.world.getBiome(posi);
			return biome.getEnableSnow() ? false : (this.world.canSnowAt(posi, false) ? false : biome.canRain());
		}
	}
}
