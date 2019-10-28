package org.terpo.waterworks.tileentity;

import org.terpo.waterworks.init.WaterworksTileEntities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntityRainCollector extends BaseTileEntity {
	private static final String NBT_CONTROLLER_POS = "controllerPos";
	private TileEntityRainCollectorController controller = null;
	private BlockPos controllerPosition = null;
	public TileEntityRainCollector() {
		super(WaterworksTileEntities.rainCollector);
	}

	public boolean hasController() {
		if (this.controller != null) {
			return true;
		}
		return (this.controllerPosition != null && verifyControllerPosition(this.controllerPosition));

	}

	public TileEntityRainCollectorController getController() {
		return this.controller;
	}

	public void informAboutBlockBreak() {
		if (hasController() && !this.controller.isRemoved()) {
			this.controller.removeCollector(this.pos);
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		if (this.hasController()) {
			final BlockPos controllerPos = this.controller.getPos();
			compound.putLong(NBT_CONTROLLER_POS, controllerPos.toLong());
		}
		return compound;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		if (compound.contains(NBT_CONTROLLER_POS)) {
			this.controllerPosition = (BlockPos.fromLong(compound.getLong(NBT_CONTROLLER_POS)));
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

	@SuppressWarnings({"unchecked"})
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return this.controller != null
					? (LazyOptional.of(() -> (T) this.controller.itemStackHandler))
					: LazyOptional.empty();
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return this.controller != null
					? (LazyOptional.of(() -> (T) this.controller.fluidTank))
					: LazyOptional.empty();
		}
		return super.getCapability(capability, side);
	}

	public void breakConnection(TileEntityRainCollectorController otherController) {
		if (hasController() && this.controller.getPos().equals(otherController.getPos())) {
			this.controller = null;
			this.controllerPosition = null;
		}
	}
}
