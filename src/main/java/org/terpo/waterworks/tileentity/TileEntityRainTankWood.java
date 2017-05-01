package org.terpo.waterworks.tileentity;

import org.terpo.waterworks.block.BlockRainTankWood;
import org.terpo.waterworks.helper.GeneralItemStackHandler;
import org.terpo.waterworks.init.WaterworksConfig;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityRainTankWood extends TileWaterworks {

	protected FluidStack RESOURCE_WATER = null;
	private static final int invSlots = 2;

	public TileEntityRainTankWood() {
		this(WaterworksConfig.RAIN_TANK_SIMPLE_FILLRATE, WaterworksConfig.RAIN_TANK_SIMPLE_CAPACITY);
	}
	public TileEntityRainTankWood(int fillrate, int capacity) {
		super(invSlots, capacity);
		this.RESOURCE_WATER = new FluidStack(FluidRegistry.WATER, fillrate);

		this.fluidTank.setCanFill(false);
		this.fluidTank.setTileEntity(this);

		this.itemStackHandler = new GeneralItemStackHandler(this.INVSIZE) {
			@Override
			protected void onContentsChanged(int slot) {
				// We need to tell the tile entity that something has changed so
				// that the chest contents is persisted
				TileEntityRainTankWood.this.markDirty();
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
			this.isDirty = isRefilling();
		}

		if (this.isDirty) {
			final IBlockState iblockstate = this.world.getBlockState(this.pos);
			if (iblockstate.getBlock() instanceof BlockRainTankWood) {
				this.world.setBlockState(this.pos,
						iblockstate.withProperty(BlockRainTankWood.LEVEL, Integer.valueOf(getStateLevel())));
			}
			this.world.updateComparatorOutputLevel(this.pos, getBlockType());
			markAsDirty(getPos());
			this.isDirty = false;
		}
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
			this.fluidTank.fillInternal(this.RESOURCE_WATER, true);
			return true;
		}
		return false;
	}

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

	private void markAsDirty(final BlockPos position) {
		this.markDirty();

		if (this.world != null) {
			final IBlockState state = this.world.getBlockState(position);
			this.world.notifyBlockUpdate(position, state, state, 3);
		}
	}
}
