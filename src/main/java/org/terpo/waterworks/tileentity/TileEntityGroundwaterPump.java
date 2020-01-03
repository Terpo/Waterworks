package org.terpo.waterworks.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.terpo.waterworks.compat.bedrockbgone.BBGCompatibility;
import org.terpo.waterworks.energy.WaterworksBattery;
import org.terpo.waterworks.helper.FluidHelper;
import org.terpo.waterworks.helper.PumpItemStackHandler;
import org.terpo.waterworks.init.WaterworksBlocks;
import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.network.EnergyPacket;
import org.terpo.waterworks.network.WaterworksPacketHandler;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityGroundwaterPump extends TileWaterworks {

	/**
	 * used NBT Tags
	 */
	private static final String NBT_BOOLEAN_STRUCTURE_COMPLETE = "structureComplete";
	private static final String NBT_INT_PIPE_COUNTER = "pipeCounter";
	private static final String NBT_INT_ENERGY_USAGE = "energyUsage";

	/**
	 * constants
	 */
	private static final int PUMP_INVENTORY_SLOTS = 5;
	private static final int PIPE_INVENTARY_SLOT_START = 2;

	protected FluidStack resourceWater = null;
	private int pipeCounter = 1;
	private boolean structureComplete = false;
	private WaterworksBattery battery;
	private int energyUsage;

	public TileEntityGroundwaterPump() {
		this(WaterworksConfig.pump.groundwaterPumpFillrate, WaterworksConfig.pump.groundwaterPumpCapacity);
	}
	public TileEntityGroundwaterPump(int fillrate, int capacity) {
		super(PUMP_INVENTORY_SLOTS, capacity);

		this.battery = new WaterworksBattery(WaterworksConfig.pump.groundwaterPumpEnergyCapacity,
				WaterworksConfig.pump.groundwaterPumpEnergyInput, 0, this);

		this.energyUsage = WaterworksConfig.pump.groundwaterPumpEnergyBaseUsage
				+ WaterworksConfig.pump.groundwaterPumpEnergyPipeMultiplier * this.pipeCounter;

		this.resourceWater = FluidHelper.getFluidResource(WaterworksConfig.pump.groundwaterPumpFluidName, fillrate);

		this.fluidTank.setCanFill(false);
		this.fluidTank.setTileEntity(this);

		this.itemStackHandler = new PumpItemStackHandler(this.inventorySize, this);

		// FluidItems Slots
		this.itemStackHandler.setInputFlagForIndex(0, true);
		this.itemStackHandler.setOutputFlagForIndex(1, true);

		// WaterPipe Slots
		this.itemStackHandler.setInputFlagForIndex(2, true);
		this.itemStackHandler.setInputFlagForIndex(3, true);
		this.itemStackHandler.setInputFlagForIndex(4, true);
	}

	@Override
	protected void updateServerSide() {
		if (needsUpdate(5) && fillFluid()) {
			this.isDirty = true;
		}

		if (needsUpdate(20)) {
			if (!this.structureComplete) {
				checkStructure();
			} else {
				if (needsUpdate(100)) {
					checkStructure(); // validation
				}
				// we can create water now
				if (refill()) {
					this.isDirty = true;
				}
			}
		}
		super.updateServerSide();

	}
	private boolean refill() {
		if (this.battery.getEnergyStored() >= this.energyUsage) {
			final int filled = this.fluidTank.fillInternal(this.resourceWater, true);
			if (filled == WaterworksConfig.pump.groundwaterPumpFillrate) {
				return this.battery.extractInternal(this.energyUsage, false) > 0;
			} else if (filled > 0) {
				final int energy = this.energyUsage
						* Math.round(((float) filled) / WaterworksConfig.pump.groundwaterPumpFillrate);
				return this.battery.extractInternal(energy, false) > 0;
			}
		}
		return false;
	}

	@Override
	public boolean shouldRefresh(World worldIn, BlockPos posIn, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	private void checkStructure() {
		int count = 0;
		final int x = this.pos.getX();
		int y = this.pos.getY() - 1;
		final int z = this.pos.getZ();

		final List<Block> bedrocks = new ArrayList<>();
		bedrocks.add(Blocks.BEDROCK);

		if (BBGCompatibility.BETTER_BEDROCK != null) {
			bedrocks.add(BBGCompatibility.BETTER_BEDROCK);
		}

		while (y >= 0) {
			final BlockPos currentPos = new BlockPos(x, y, z);
			final IBlockState state = this.world.getBlockState(currentPos);
			final Block block = state.getBlock();
			if (block.equals(WaterworksBlocks.waterPipe)) {
				count++;
				y--;
				if (y >= 0) {
					continue;
				}
			}
			if (bedrocks.contains(block) || (!WaterworksConfig.pump.groundwaterPumpCheckBedrock && y < 0)) {
				this.structureComplete = true;
				this.pipeCounter = count;
				this.energyUsage = WaterworksConfig.pump.groundwaterPumpEnergyBaseUsage
						+ WaterworksConfig.pump.groundwaterPumpEnergyPipeMultiplier * this.pipeCounter;
				return;
			} else if (block.equals(Blocks.AIR)
					&& this.battery.hasEnoughEnergy(WaterworksConfig.pump.groundwaterPumpEnergyPipePlacement)
					&& placePipe(currentPos)) {
				this.battery.extractInternal(WaterworksConfig.pump.groundwaterPumpEnergyPipePlacement, false);
				break;
			}
			this.structureComplete = false;
			break;
		}

	}

	private boolean placePipe(BlockPos currentPos) {
		final HashMap<ItemStack, Integer> stacks = getPipeStacks();
		if (!stacks.isEmpty()) {
			stacks.forEach((stack, slot) -> {
				if (this.world.setBlockState(currentPos, WaterworksBlocks.waterPipe.getDefaultState(), 2)) {
					if (stack.getCount() > 1) {
						stack.shrink(1);
					} else {
						this.itemStackHandler.setStackInSlot(slot.intValue(), ItemStack.EMPTY);
					}
					return;
				}
			});
			return true;
		}
		return false;
	}

	private HashMap<ItemStack, Integer> getPipeStacks() {
		final HashMap<ItemStack, Integer> pipeStack = new HashMap<>();
		for (int i = PIPE_INVENTARY_SLOT_START; i < this.inventorySize; i++) {
			final ItemStack stack = this.itemStackHandler.getStackInSlot(i);
			if (!stack.isEmpty()) {
				pipeStack.put(stack, Integer.valueOf(i));
			}
		}
		return pipeStack;
	}
	public boolean isStructureComplete() {
		return this.structureComplete;
	}
	public void setStructureComplete(boolean structureComplete) {
		this.structureComplete = structureComplete;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return (T) this.battery;
		}
		return super.getCapability(capability, facing);
	}
	public WaterworksBattery getBattery() {
		return this.battery;
	}

	public void sendEnergyPacket() {
		WaterworksPacketHandler.sendToAllAround(new EnergyPacket(this), this);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		this.battery.writeToNBT(compound);
		compound.setInteger(NBT_INT_ENERGY_USAGE, this.energyUsage);
		compound.setInteger(NBT_INT_PIPE_COUNTER, this.pipeCounter);
		compound.setBoolean(NBT_BOOLEAN_STRUCTURE_COMPLETE, this.structureComplete);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.battery = this.battery.readFromNBT(compound);
		if (compound.hasKey(NBT_INT_ENERGY_USAGE)) {
			this.energyUsage = compound.getInteger(NBT_INT_ENERGY_USAGE);
		}
		if (compound.hasKey(NBT_INT_PIPE_COUNTER)) {
			this.pipeCounter = compound.getInteger(NBT_INT_PIPE_COUNTER);
		}
		if (compound.hasKey(NBT_BOOLEAN_STRUCTURE_COMPLETE)) {
			this.structureComplete = compound.getBoolean(NBT_BOOLEAN_STRUCTURE_COMPLETE);
		}
	}
}
