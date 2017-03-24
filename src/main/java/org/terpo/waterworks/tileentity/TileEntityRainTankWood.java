package org.terpo.waterworks.tileentity;

import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.network.TankPacket;
import org.terpo.waterworks.network.WaterworksPacketHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

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
			this.world.updateComparatorOutputLevel(this.pos, getBlockType());
			markAsDirty(getPos());
			this.isDirty = false;
		}
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
					&& (stackInput.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
							|| stackInput.getItem() == Items.BUCKET)) {
				// Buckets
				if (stackInput.getItem() == Items.BUCKET) {
					if (internalFluidAmount >= 1000 && this.itemStackHandler.getStackInSlot(1).isEmpty()) {
						if (stackInput.getCount() > 1) {
							stackInput.shrink(1);
						} else {
							this.itemStackHandler.setStackInSlot(0, ItemStack.EMPTY);
						}
						this.fluidTank.drain(new FluidStack(FluidRegistry.WATER, 1000), true);
						this.itemStackHandler.setStackInSlot(1, new ItemStack(Items.WATER_BUCKET));
						return true;
					}
					return false;
				}
				// Other containers with a current stackSize of 1
				final IFluidHandler capability = stackInput
						.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
				if (stackInput.getCount() == 1 && this.itemStackHandler.getStackInSlot(1) == null) {
					final IFluidTankProperties[] properties = capability.getTankProperties();
					if (properties.length > 0) {
						for (final IFluidTankProperties property : properties) {
							final int fluidCapacity = property.getCapacity();
							if (fluidCapacity > 0 && property.canFill()) {
								final FluidStack content = property.getContents();
								if (content == null) {
									fillCustomFluidContainer(internalFluidAmount, capability, fluidCapacity,
											getWaterFluidStack(0));
									return true;
								}
								if (content.getFluid() == FluidRegistry.WATER) {
									fillCustomFluidContainer(internalFluidAmount, capability, fluidCapacity, content);
									return true;
								}
							}
						}
					}
				}

			}
		}
		return false;
	}

	private void fillCustomFluidContainer(final int internalFluidAmount, final IFluidHandler capability,
			final int fluidCapacity, final FluidStack content) {
		this.itemStackHandler.setStackInSlot(1, this.itemStackHandler.getStackInSlot(0));
		this.itemStackHandler.setStackInSlot(0, ItemStack.EMPTY);
		final int fillAmount = fluidCapacity - content.amount >= internalFluidAmount
				? internalFluidAmount
				: fluidCapacity - content.amount;
		final FluidStack filledWaterstack = getWaterFluidStack(fillAmount);
		capability.fill(filledWaterstack, true);
		this.fluidTank.drain(filledWaterstack, true);
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
