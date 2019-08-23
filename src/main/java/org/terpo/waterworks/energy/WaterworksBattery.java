package org.terpo.waterworks.energy;

import org.terpo.waterworks.api.constants.NBTContants;
import org.terpo.waterworks.tileentity.TileEntityGroundwaterPump;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.energy.EnergyStorage;

public class WaterworksBattery extends EnergyStorage {

	private final TileWaterworks tile;
	int lastTick = 0;

	public WaterworksBattery(int capacity, int maxReceive, int maxExtract, TileWaterworks tE) {
		super(capacity, maxReceive, maxExtract);
		this.tile = tE;
		this.lastTick = this.tile.getCurrentTick();
	}

	public int extractInternal(int energyAmount, boolean simulate) {
		if (!simulate && (energyAmount <= this.energy)) {
			this.energy -= energyAmount;
			onContentsChanged();
			return energyAmount;
		}
		return 0;
	}

	public boolean hasEnoughEnergy(int energyAmount) {
		return this.getEnergyStored() >= energyAmount;
	}

	@Override
	public int receiveEnergy(int maximumReceive, boolean simulate) {
		onContentsChanged();
		return super.receiveEnergy(maximumReceive, simulate);
	}

	protected void onContentsChanged() {
		// We need to tell the tile entity that something has changed so
		// that the chest contents is persisted
		if (this.tile instanceof TileEntityGroundwaterPump) {
			if (this.tile.getCurrentTick() - this.lastTick > 20) {
				((TileEntityGroundwaterPump) this.tile).sendEnergyPacket();
				this.lastTick = this.tile.getCurrentTick();
			}
		}
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
