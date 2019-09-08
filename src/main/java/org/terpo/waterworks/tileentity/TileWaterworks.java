package org.terpo.waterworks.tileentity;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.terpo.waterworks.fluid.WaterworksTank;
import org.terpo.waterworks.helper.GeneralItemStackHandler;
import org.terpo.waterworks.network.TankPacket;
import org.terpo.waterworks.network.WaterworksPacketHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileWaterworks extends BaseTileEntity implements ITickableTileEntity {

	private static final Random random = new Random();
	private int currentTick = random.nextInt(256);
	protected WaterworksTank fluidTank;

	protected int inventorySize;
	protected int tankSize;

	// This item handler will hold our two inventory slots
	protected GeneralItemStackHandler itemStackHandler;
	// TileEntity
	public TileWaterworks(TileEntityType<?> tileEntityTypeIn, int inventorySize, int tankSize) {
		super(tileEntityTypeIn);
		this.inventorySize = inventorySize;
		this.tankSize = tankSize;

		this.fluidTank = new WaterworksTank(this.tankSize);
	}

	protected void sendUpdatePacket() {
		WaterworksPacketHandler.sendToAllAround(new TankPacket(this), this);
	}

	public TileWaterworks(TileEntityType<?> tileEntityTypeIn) {
		this(tileEntityTypeIn, 2, 8000);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put("items", this.itemStackHandler.serializeNBT());
		this.fluidTank.writeToNBT(compound);
		return compound;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		if (compound.hasUniqueId("items")) {
			this.itemStackHandler.deserializeNBT(compound.getCompound("items"));
		}
		this.fluidTank = (WaterworksTank) this.fluidTank.readFromNBT(compound);

	}

	public boolean canInteractWith(PlayerEntity playerIn) {
		// If we are too far away from this tile entity you cannot use it
		return !isInvalid() && playerIn.getDistanceSq(this.pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return LazyOptional.of(() -> (T) this.itemStackHandler);
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return LazyOptional.of(() -> (T) this.fluidTank);

		}
		return super.getCapability(capability, side);
	}

	// TileEntity End

	// ITackable
	@Override
	public final void tick() {
		this.currentTick++;

		if (!this.world.isRemote) {
			updateServerSide();
		} else {
			updateClientSide();
		}
	}

	protected void updateClientSide() {
		// to be implemented
	}

	protected void updateServerSide() {
		if (this.isDirty) {
			this.markDirty();
			this.sendUpdatePacket();
			this.isDirty = false;
		}
	}

	protected boolean needsUpdate(int tickInterval) {
		return this.currentTick % tickInterval == 0;
	}
	// ITickable End

	public int getINVSIZE() {
		return this.inventorySize;
	}

	public void setINVSIZE(int iNVSIZE) {
		this.inventorySize = iNVSIZE;
	}

	public int getTANKSIZE() {
		return this.tankSize;
	}

	public void setTANKSIZE(int tANKSIZE) {
		this.tankSize = tANKSIZE;
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

	public boolean isDirty() {
		return this.isDirty;
	}

	// FluidStuff
	protected boolean fillFluid() {
		final int internalFluidAmount = this.fluidTank.getFluidAmount();
		if (internalFluidAmount > 0) {
			final ItemStack stackInput = this.itemStackHandler.getStackInSlot(0);
			if (!stackInput.isEmpty()) {
				final LazyOptional<IFluidHandlerItem> capability = stackInput
						.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
				if (capability.isPresent()) {

					final FluidActionResult filledSimulated = FluidUtil.tryFillContainer(stackInput, this.fluidTank,
							Integer.MAX_VALUE, null, false);
					if (filledSimulated.isSuccess()) {
						final ItemStack simResult = filledSimulated.getResult();
						final ItemStack outputSlot = this.itemStackHandler.getStackInSlot(1);
						if (checkOutputSlot(outputSlot, simResult)) {
							final FluidActionResult fillResult = FluidUtil.tryFillContainer(stackInput, this.fluidTank,
									Integer.MAX_VALUE, null, true);
							if (fillResult.isSuccess()) {
								final ItemStack realResult = fillResult.getResult();
								moveFilledInputToOutput(stackInput, outputSlot, realResult);
								return true;
							}
						}
					}
				}
				if (stackInput.getItem().equals(Items.GLASS_BOTTLE)) {
					if (internalFluidAmount >= 1000) {
						final ItemStack outputSlot = this.itemStackHandler.getStackInSlot(1);
						final ItemStack outputStack = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION),
								Potions.WATER);
						if (checkOutputSlot(outputSlot, outputStack)) {
							this.fluidTank.drainInternal(1000, true);
							moveFilledInputToOutput(stackInput, outputSlot, outputStack);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private static boolean checkOutputSlot(ItemStack outputSlot, ItemStack stack) {
		final boolean isItemIdentical = outputSlot.getItem().equals(stack.getItem());
		final boolean hasSpace = outputSlot.getCount() < outputSlot.getMaxStackSize();
		return (outputSlot.isEmpty() || (isItemIdentical) && (hasSpace));
	}

	private void moveFilledInputToOutput(final ItemStack stackInput, final ItemStack outputSlot,
			final ItemStack realResult) {
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
	}
	protected static FluidStack getWaterFluidStack(int amount) {
		return new FluidStack(null, amount);
	}

	public int getCurrentTick() {
		return this.currentTick;
	}

}
