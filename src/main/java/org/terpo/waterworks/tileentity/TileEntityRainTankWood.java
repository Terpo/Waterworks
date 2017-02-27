package org.terpo.waterworks.tileentity;

import org.terpo.waterworks.network.TankPacket;
import org.terpo.waterworks.network.WaterworksPacketHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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

	private static final int fillrate = 100;
	private static final FluidStack RESOURCE_WATER = new FluidStack(FluidRegistry.WATER, fillrate);
	private static final int capacity = 8000;
	private static final int invSlots = 2;

	public TileEntityRainTankWood() {
		super(invSlots, capacity);
		this.fluidTank.setCanFill(false);
		this.fluidTank.setTileEntity(this);

		this.itemStackHandler.setInputFlagForIndex(0, true);
		this.itemStackHandler.setInputFlagForIndex(1, false);
		this.itemStackHandler.setOutputFlagForIndex(1, true);
	}

	@Override
	protected void updateServerSide() {
		boolean isDirty = false;
		isDirty = fillFluid();

		if (needsUpdate(20)) {
			isDirty = isRefilling();
		}

		if (isDirty) {
			this.worldObj.updateComparatorOutputLevel(this.pos, getBlockType());
			markAsDirty(getPos());
			isDirty = false;
		}
	}

	private boolean isRefilling() {
		final BlockPos position = getPos().up();

		if (isRainingAtPosition(position)) {
			this.fluidTank.fillInternal(RESOURCE_WATER, true);
			return true;
		}
		return false;
	}

	private boolean isRainingAtPosition(BlockPos posi) {
		// copy of isRainingAt in World.class
		if (!this.worldObj.isRaining()) {
			return false;
		} else if (!this.worldObj.canBlockSeeSky(posi)) {
			return false;
		} else if (this.worldObj.getPrecipitationHeight(posi).getY() > posi.getY()) {
			return false;
		} else {
			final Biome biome = this.worldObj.getBiome(posi);
			return biome.getEnableSnow() ? false : (this.worldObj.canSnowAt(posi, false) ? false : biome.canRain());
		}
	}

	private boolean fillFluid() {
		final int internalFluidAmount = this.fluidTank.getFluidAmount();
		if (internalFluidAmount > 0) {
			final ItemStack stackInput = this.itemStackHandler.getStackInSlot(0);
			if (stackInput != null && stackInput.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
				final IFluidHandler capability = stackInput
						.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
				// Buckets
				if (stackInput.getItem() == Items.BUCKET) {
					if (internalFluidAmount >= 1000 && this.itemStackHandler.getStackInSlot(1) == null) {
						if (stackInput.stackSize > 1) {
							stackInput.stackSize--;
						} else {
							this.itemStackHandler.setStackInSlot(0, null);
						}
						this.fluidTank.drain(new FluidStack(FluidRegistry.WATER, 1000), true);
						this.itemStackHandler.setStackInSlot(1, new ItemStack(Items.WATER_BUCKET));
						return true;
					}
					return false;
				}
				// Other containers with a current stackSize of 1
				if (stackInput.stackSize == 1 && this.itemStackHandler.getStackInSlot(1) == null) {
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

	public boolean onBlockActivated(EntityPlayer player, ItemStack itemStack, int slotIndex) {
		if (this.fluidTank.getFluidAmount() >= 1000) {
			if (itemStack.stackSize > 1) {
				itemStack.stackSize--;
				if (player.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET))) {
					this.fluidTank.drain(new FluidStack(FluidRegistry.WATER, 1000), true);
					return true;
				}
				return false;
			}
			player.inventory.setInventorySlotContents(slotIndex, new ItemStack(Items.WATER_BUCKET));
			this.fluidTank.drain(new FluidStack(FluidRegistry.WATER, 1000), true);
			return true;
		}
		return false;
	}

	private void fillCustomFluidContainer(final int internalFluidAmount, final IFluidHandler capability,
			final int fluidCapacity, final FluidStack content) {
		this.itemStackHandler.setStackInSlot(1, this.itemStackHandler.getStackInSlot(0));
		this.itemStackHandler.setStackInSlot(0, null);
		final int fillAmount = fluidCapacity - content.amount >= internalFluidAmount
				? internalFluidAmount
				: fluidCapacity - content.amount;
		final FluidStack filledWaterstack = getWaterFluidStack(fillAmount);
		capability.fill(filledWaterstack, true);
		this.fluidTank.drain(filledWaterstack, true);
	}

	private static FluidStack getWaterFluidStack(int amount) {
		return new FluidStack(FluidRegistry.WATER, amount);
	}

	private void markAsDirty(final BlockPos position) {
		this.markDirty();

		if (this.worldObj != null) {
			final IBlockState state = this.worldObj.getBlockState(position);
			this.worldObj.notifyBlockUpdate(position, state, state, 3);
		}
	}

	@Override
	protected void sendUpdatePacket() {
		WaterworksPacketHandler.sendToAllAround(new TankPacket(this), this);
	}
}
