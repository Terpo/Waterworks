package org.terpo.waterworks.tileentity;

import java.util.Random;

import org.terpo.waterworks.fluid.WaterworksTank;
import org.terpo.waterworks.helper.FluidItemStackHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileWaterworks extends BaseTileEntity implements ITickable {
	protected boolean isDirty = false;

	private static final Random random = new Random();
	private int currentTick = random.nextInt(256);
	protected WaterworksTank fluidTank;

	protected int INVSIZE;
	protected int TANKSIZE;

	// This item handler will hold our nine inventory slots
	protected final FluidItemStackHandler itemStackHandler;
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
				TileWaterworks.this.sendUpdatePacket();
			}
		};
		this.itemStackHandler = new FluidItemStackHandler(this.INVSIZE) {
			@Override
			protected void onContentsChanged(int slot) {
				// We need to tell the tile entity that something has changed so
				// that the chest contents is persisted
				TileWaterworks.this.markDirty();
			}
		};
	}

	protected void sendUpdatePacket() {
		// do nothing
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
}
