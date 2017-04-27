package org.terpo.waterworks.tileentity;

import org.terpo.waterworks.block.BlockRainTankWood;
import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.network.TankPacket;
import org.terpo.waterworks.network.WaterworksPacketHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

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
//			final ImmutableMap<IProperty<?>, Comparable<?>> properties = iblockstate.getProperties();
//			properties.forEach((p, v) -> {
//				Waterworks.LOGGER.info(p.getName());
//				Waterworks.LOGGER.info(getStateLevel());
//				if (p.getName().equals("level")) {
//					this.world.setBlockState(this.pos,
//							iblockstate.withProperty(p, Integer.valueOf(getStateLevel()), 2));
//				}
//			});
			if (iblockstate.getBlock() instanceof BlockRainTankWood) {
				// Waterworks.LOGGER.info(getStateLevel());
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

	private boolean fillFluid() {
		final int internalFluidAmount = this.fluidTank.getFluidAmount();
		if (internalFluidAmount > 0) {
			final ItemStack stackInput = this.itemStackHandler.getStackInSlot(0);
			if (!stackInput.isEmpty()
					&& (stackInput.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))) {

				final FluidActionResult filledSimulated = FluidUtil.tryFillContainer(stackInput, this.fluidTank,
						Integer.MAX_VALUE, null, false);
				if (filledSimulated.isSuccess()) {
					final ItemStack simResult = filledSimulated.getResult();
					final ItemStack outputSlot = this.itemStackHandler.getStackInSlot(1);
					final boolean isItemIdentical = outputSlot.getItem().equals(simResult.getItem());
					final boolean hasSpace = outputSlot.getCount() < outputSlot.getMaxStackSize();
					if (outputSlot.isEmpty() || (isItemIdentical) && (hasSpace)) {
						final FluidActionResult fillResult = FluidUtil.tryFillContainer(stackInput, this.fluidTank,
								Integer.MAX_VALUE, null, true);
						if (fillResult.isSuccess()) {
							final ItemStack realResult = fillResult.getResult();
							if (outputSlot.isEmpty()) {
								this.itemStackHandler.setStackInSlot(1, realResult);
							} else {
								outputSlot.grow(1);
							}
							if (stackInput.getCount() > 1) {
								stackInput.shrink(1);
							} else {
								this.itemStackHandler.setStackInSlot(0, ItemStack.EMPTY);
							}
							return true;
						}
					}
				}

			}
		}
		return false;
	}
	protected static FluidStack getWaterFluidStack(int amount) {
		return new FluidStack(FluidRegistry.WATER, amount);
	}

	private void markAsDirty(final BlockPos position) {
		this.markDirty();

		if (this.world != null) {
			final IBlockState state = this.world.getBlockState(position);
			this.world.notifyBlockUpdate(position, state, state, 3);
		}
	}

	@Override
	protected void sendUpdatePacket() {
		WaterworksPacketHandler.sendToAllAround(new TankPacket(this), this);
	}
}
