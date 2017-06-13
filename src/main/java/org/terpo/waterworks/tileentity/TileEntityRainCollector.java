package org.terpo.waterworks.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntityRainCollector extends BaseTileEntity {
	private TileEntityRainCollectorController controller = null;
	private BlockPos controllerPosition = null;
	public TileEntityRainCollector() {
		//
	}

	public boolean hasController() {
		if (this.controller != null) {
			return true;
		}
		if (this.controllerPosition != null && verifyControllerPosition(this.controllerPosition)) {
			return true;
		}
		return false;
	}

	public TileEntityRainCollectorController getController() {
		return this.controller;
	}

	public void informAboutBlockBreak() {
		if (hasController() && !this.controller.isInvalid()) {
			this.controller.removeCollector(this.pos);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (this.hasController()) {
			final BlockPos controllerPos = this.controller.getPos();
			compound.setLong("controllerPos", controllerPos.toLong());
		}
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("controllerPos")) {
			this.controllerPosition = (BlockPos.fromLong(compound.getLong("controllerPos")));
			// setController(this.controllerPosition);
		}
	}

	private boolean verifyControllerPosition(BlockPos position) {
		final TileEntity tile = this.world.getTileEntity(position);
		if (tile instanceof TileEntityRainCollectorController) {
			final TileEntityRainCollectorController tileEntity = (TileEntityRainCollectorController) tile;
			if (tileEntity.isCollectorListed(this.pos)) {
				this.controller = tileEntity;
				return true;
			}
		}
		return false;
	}

	public boolean setController(TileEntityRainCollectorController tE) {
		if (!this.hasController()) {
			this.controller = tE;
			return true;
		}
		return false;
	}

	public boolean setController(BlockPos position) {
		if (!this.hasController()) {
			return verifyControllerPosition(position);
		}
		return false;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (hasController()) {
			if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
				return true;
			}
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
				return true;
			}
		}
		return super.hasCapability(capability, facing);
	}

	@SuppressWarnings({"unchecked"})
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) this.controller.itemStackHandler;
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T) this.controller.fluidTank;
		}
		return super.getCapability(capability, facing);
	}

	public void breakConnection(TileEntityRainCollectorController otherController) {
		if (hasController() && this.controller.getPos().equals(otherController.getPos())) {
			this.controller = null;
			this.controllerPosition = null;
		}
	}
}
