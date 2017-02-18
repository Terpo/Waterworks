package org.terpo.waterworks.tileentity;

import org.terpo.waterworks.fluid.WaterworksTank;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityRainTankWood extends TileWaterworks {

	private WaterworksTank fluidTank;
	private static final int fillrate = 100;
	private static final FluidStack RESOURCE_WATER = new FluidStack(FluidRegistry.WATER, fillrate);

	private final int capacity = 16000;

	public TileEntityRainTankWood() {
		super();
		this.fluidTank = new WaterworksTank(this.capacity);
		this.fluidTank.setCanFill(false);
		this.fluidTank.setTileEntity(this);
	}

	@Override
	protected void updateServerSide() {
		if (needsUpdate(20)) {
			final BlockPos position = getPos().up();
			final Biome biome = this.worldObj.getBiome(position);

			if (biome.canRain() && this.worldObj.canBlockSeeSky(position) && this.worldObj.isRainingAt(position)) {
				this.fluidTank.fillInternal(RESOURCE_WATER, true);
				this.markDirty();

				if (this.worldObj != null) {
					final IBlockState state = this.worldObj.getBlockState(getPos());
					this.worldObj.notifyBlockUpdate(getPos(), state, state, 3);
				}
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		this.fluidTank.writeToNBT(compound);
		return compound;
	}
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.fluidTank = (WaterworksTank) this.fluidTank.readFromNBT(compound);

	}

	public int getComparatorOutput() {
		if (this.fluidTank.isEmpty()) {
			return 0;
		}

		return (int) (1 + ((double) this.fluidTank.getFluidAmount() / (double) this.fluidTank.getCapacity()) * 14);
	}

	public int getDebugInfo() {
		return this.fluidTank.getFluidAmount();

	}

	public WaterworksTank getFluidTank() {
		return this.fluidTank;
	}
}
