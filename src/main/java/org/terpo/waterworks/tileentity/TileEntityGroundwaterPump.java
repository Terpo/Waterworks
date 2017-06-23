package org.terpo.waterworks.tileentity;

import java.util.HashMap;

import org.terpo.waterworks.energy.WaterworksBattery;
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
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityGroundwaterPump extends TileWaterworks {

	protected FluidStack RESOURCE_WATER = null;
	private static final int invSlots = 5;
	private int pipeCounter = 1;
	private boolean structureComplete = false;
	private WaterworksBattery battery;
	private int energyUsage;

	public TileEntityGroundwaterPump() {
		this(WaterworksConfig.GROUNDWATER_PUMP_FILLRATE, WaterworksConfig.GROUNDWATER_PUMP_CAPACITY);
	}
	public TileEntityGroundwaterPump(int fillrate, int capacity) {
		super(invSlots, capacity);

		this.battery = new WaterworksBattery(WaterworksConfig.GROUNDWATER_PUMP_ENERGY_CAPACITY,
				WaterworksConfig.GROUNDWATER_PUMP_ENERGY_MAXINPUT, 0, this);

		this.energyUsage = WaterworksConfig.GROUNDWATER_PUMP_ENERGY_BASEUSAGE
				+ WaterworksConfig.GROUNDWATER_PUMP_ENERGY_PIPEMULTIPLIER * this.pipeCounter;

		this.RESOURCE_WATER = new FluidStack(FluidRegistry.WATER, fillrate);

		this.fluidTank.setCanFill(false);
		this.fluidTank.setTileEntity(this);

		this.itemStackHandler = new PumpItemStackHandler(this.INVSIZE) {
			@Override
			protected void onContentsChanged(int slot) {
				// We need to tell the tile entity that something has changed so
				// that the chest contents is persisted
				TileEntityGroundwaterPump.this.markDirty();
			}
		};

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
		if (needsUpdate(5)) {
			if (fillFluid()) {
				this.isDirty = true;
			}
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

		if (this.isDirty) {
			this.markDirty();
			this.sendUpdatePacket();
			this.isDirty = false;
		}
	}
	private boolean refill() {
		if (this.battery.getEnergyStored() >= this.energyUsage) {
			final int filled = this.fluidTank.fillInternal(this.RESOURCE_WATER, true);
			if (filled == WaterworksConfig.GROUNDWATER_PUMP_FILLRATE) {
				if (this.battery.extractInternal(this.energyUsage, false) > 0) {
					return true;
				}
				return false;
			} else if (filled > 0) {
				final int energy = this.energyUsage
						* Math.round(((float) filled) / WaterworksConfig.GROUNDWATER_PUMP_FILLRATE);
				if (this.battery.extractInternal(energy, false) > 0) {
					return true;
				}
				return false;
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

		while (y >= 0) {
			final BlockPos currentPos = new BlockPos(x, y, z);
			final IBlockState state = this.world.getBlockState(currentPos);
			final Block block = state.getBlock();
			if (block.equals(WaterworksBlocks.water_pipe)) {
				count++;
				y--;
				continue;
			} else if (block.equals(Blocks.BEDROCK)) {
				this.structureComplete = true;
				this.pipeCounter = count;
				this.energyUsage = WaterworksConfig.GROUNDWATER_PUMP_ENERGY_BASEUSAGE
						+ WaterworksConfig.GROUNDWATER_PUMP_ENERGY_PIPEMULTIPLIER * this.pipeCounter;
				break;
			} else if (block.equals(Blocks.AIR)) {
				if (this.battery.hasEnoughEnergy(WaterworksConfig.GROUNDWATER_PUMP_PIPE_PLACEMENT_ENERGY)
						&& placePipe(currentPos)) {
					this.battery.extractInternal(WaterworksConfig.GROUNDWATER_PUMP_PIPE_PLACEMENT_ENERGY, false);
					count++;
					y--;
					break;
				}
			}
			this.structureComplete = false;
			break;
		}

	}

	private boolean placePipe(BlockPos currentPos) {
		final HashMap<ItemStack, Integer> stacks = getPipeStacks();
		if (!stacks.isEmpty()) {
			stacks.forEach((stack, slot) -> {
				if (this.world.setBlockState(currentPos, WaterworksBlocks.water_pipe.getDefaultState(), 2)) {
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
		for (int i = 0; i < this.INVSIZE; i++) {
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
		compound.setInteger("energyUsage", this.energyUsage);
		compound.setInteger("pipeCounter", this.pipeCounter);
		compound.setBoolean("structureComplete", this.structureComplete);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.battery = this.battery.readFromNBT(compound);
		if (compound.hasKey("energyUsage")) {
			this.energyUsage = compound.getInteger("energyUsage");
		}
		if (compound.hasKey("pipeCounter")) {
			this.pipeCounter = compound.getInteger("pipeCounter");
		}
		if (compound.hasKey("structureComplete")) {
			this.structureComplete = compound.getBoolean("structureComplete");
		}
	}
}
