package org.terpo.waterworks.energy;

import org.terpo.waterworks.api.constants.NBTContants;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.energy.EnergyStorage;

public class WaterworksBattery extends EnergyStorage {

	private final TileWaterworks tile;
	int lastTick = 0;
	private boolean dirty;

	public WaterworksBattery(int capacity, int maxReceive, int maxExtract, TileWaterworks tE) {
		super(capacity, maxReceive, maxExtract);
		this.tile = tE;
		this.lastTick = this.tile.getCurrentTick();
	}

	public int extractInternal(int energyAmount, boolean simulate) {
		if (!simulate && (energyAmount <= this.energy)) {
			this.energy -= energyAmount;
			this.dirty = true;
			return energyAmount;
		}
		return 0;
	}

	public boolean hasEnoughEnergy(int energyAmount) {
		return this.getEnergyStored() >= energyAmount;
	}

	@Override
	public int receiveEnergy(int maximumReceive, boolean simulate) {
		final int quantity = super.receiveEnergy(maximumReceive, simulate);
		if (quantity > 0) {
			this.dirty = true;
		}
		return quantity;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public boolean isDirty() {
		return this.dirty;
	}

	public void setEnergyAmount(int energyAmount) {
		if (energyAmount > this.capacity) {
			this.energy = this.capacity;
		} else {
			this.energy = energyAmount;
		}
	}

	public CompoundNBT write(CompoundNBT compound) {
		compound.putInt(NBTContants.ENERGY_STORED, this.energy);
		return compound;
	}

	public WaterworksBattery read(CompoundNBT compound) {
		if (compound.contains(NBTContants.ENERGY_STORED)) {
			this.energy = compound.getInt(NBTContants.ENERGY_STORED);
		}
		return this;
	}
}
