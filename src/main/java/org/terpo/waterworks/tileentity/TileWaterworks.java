package org.terpo.waterworks.tileentity;

import java.util.Random;

import org.terpo.waterworks.fluid.WaterworksTank;
import org.terpo.waterworks.helper.GeneralItemStackHandler;
import org.terpo.waterworks.network.TankPacket;
import org.terpo.waterworks.network.WaterworksPacketHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileWaterworks extends BaseTileEntity implements ITickable {
	protected boolean isDirty = false;

	private static final Random random = new Random();
	private int currentTick = random.nextInt(256);
	protected WaterworksTank fluidTank;

	protected int INVSIZE;
	protected int TANKSIZE;

	// This item handler will hold our two inventory slots
	protected GeneralItemStackHandler itemStackHandler;
	// TileEntity
	public TileWaterworks(int inventorySize, int tankSize) {
		super();
		this.INVSIZE = inventorySize;
		this.TANKSIZE = tankSize;

		this.fluidTank = new WaterworksTank(this.TANKSIZE) {
			@Override
			protected void onContentsChanged() {
				// We need to tell the tile entity that something has changed so
				// that the chest contents is persisted
				TileWaterworks.this.markDirty();
				// TileWaterworks.this.sendUpdatePacket();
			}
		};
	}

	protected void sendUpdatePacket() {
		WaterworksPacketHandler.sendToAllAround(new TankPacket(this), this);
	}

	public TileWaterworks() {
		this(2, 8000);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("items", this.itemStackHandler.serializeNBT());
		this.fluidTank.writeToNBT(compound);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("items")) {
			this.itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
		}
		this.fluidTank = (WaterworksTank) this.fluidTank.readFromNBT(compound);

	}

	public boolean canInteractWith(EntityPlayer playerIn) {
		// If we are too far away from this tile entity you cannot use it
		return !isInvalid() && playerIn.getDistanceSq(this.pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) this.itemStackHandler;
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T) this.fluidTank;
		}
		return super.getCapability(capability, facing);
	}

	// TileEntity End

	// ITackable
	@Override
	public final void update() {
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
		// to be implemented
	}

	protected boolean needsUpdate(int tickInterval) {
		return this.currentTick % tickInterval == 0;
	}
	// ITickable End

	public int getINVSIZE() {
		return this.INVSIZE;
	}

	public void setINVSIZE(int iNVSIZE) {
		this.INVSIZE = iNVSIZE;
	}

	public int getTANKSIZE() {
		return this.TANKSIZE;
	}

	public void setTANKSIZE(int tANKSIZE) {
		this.TANKSIZE = tANKSIZE;
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

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}

	// FluidStuff
	protected boolean fillFluid() {
		final int internalFluidAmount = this.fluidTank.getFluidAmount();
		if (internalFluidAmount > 0) {
			final ItemStack stackInput = this.itemStackHandler.getStackInSlot(0);
			if (!stackInput.isEmpty()) {
				if (stackInput.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {

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
						final ItemStack outputStack = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM),
								PotionTypes.WATER);
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
		return new FluidStack(FluidRegistry.WATER, amount);
	}

	public int getCurrentTick() {
		return this.currentTick;
	}

}
