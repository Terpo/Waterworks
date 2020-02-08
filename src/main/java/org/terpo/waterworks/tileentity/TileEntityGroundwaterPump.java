package org.terpo.waterworks.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.terpo.waterworks.Config;
import org.terpo.waterworks.block.BlockWaterPipe;
import org.terpo.waterworks.energy.WaterworksBattery;
import org.terpo.waterworks.gui.pump.PumpContainer;
import org.terpo.waterworks.helper.GeneralItemStackHandler;
import org.terpo.waterworks.helper.PumpItemStackHandler;
import org.terpo.waterworks.network.PumpPacket;
import org.terpo.waterworks.network.WaterworksPacketHandler;
import org.terpo.waterworks.setup.Registration;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.ItemStackHandler;

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
	private final LazyOptional<WaterworksBattery> battery = LazyOptional.of(this::createBattery);
	private int energyUsage;

	public TileEntityGroundwaterPump() {
		this(Config.pump.getGroundwaterPumpFillrate(), Config.pump.getGroundwaterPumpCapacity());
	}
	public TileEntityGroundwaterPump(int fillrate, int capacity) {
		super(Registration.groundwaterPumpTile.get(), PUMP_INVENTORY_SLOTS, capacity);

		this.energyUsage = Config.pump.getGroundwaterPumpEnergyBaseUsage()
				+ Config.pump.getGroundwaterPumpEnergyPipeMultiplier() * this.pipeCounter;

		this.resourceWater = new FluidStack(Fluids.WATER, fillrate);
	}

	@Override
	protected GeneralItemStackHandler createItemHandler() {
		final PumpItemStackHandler handler = new PumpItemStackHandler(this.inventorySize, this);

		// FluidItems Slots
		handler.setInputFlagForIndex(0, true);
		handler.setOutputFlagForIndex(1, true);

		// WaterPipe Slots
		handler.setInputFlagForIndex(2, true);
		handler.setInputFlagForIndex(3, true);
		handler.setInputFlagForIndex(4, true);
		return handler;
	}

	protected WaterworksBattery createBattery() {
		return new WaterworksBattery(Config.pump.getGroundwaterPumpEnergyCapacity(),
				Config.pump.getGroundwaterPumpEnergyInput(), 0, this);
	}

	@Override
	protected void updateServerSide() {
		if (needsUpdate(5) && fillFluid()) {
			this.isDirty = true;
		}

		if (needsUpdate(20)) {
			this.battery.ifPresent(b -> {
				if (b.isDirty()) {
					this.isDirty = true;
					b.setDirty(false);
				}
			});

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
		final WaterworksBattery waterworksBattery = this.battery.orElseGet(this::createBattery);
		if (waterworksBattery != null && waterworksBattery.getEnergyStored() >= this.energyUsage) {
			final int filled = getFluidTank().fillInternal(this.resourceWater, FluidAction.EXECUTE);
			if (filled == Config.pump.getGroundwaterPumpFillrate()) {
				return waterworksBattery.extractInternal(this.energyUsage, false) > 0;
			} else if (filled > 0) {
				final int energy = this.energyUsage * Math.round(((float) filled) / Config.pump.getGroundwaterPumpFillrate());
				return waterworksBattery.extractInternal(energy, false) > 0;
			}
		}
		return false;
	}

	private void checkStructure() {
		int count = 0;
		final int x = this.pos.getX();
		int y = this.pos.getY() - 1;
		final int z = this.pos.getZ();

		final List<Block> bedrocks = new ArrayList<>();
		bedrocks.add(Blocks.BEDROCK);

		final WaterworksBattery internalBattery = this.battery.orElseGet(this::createBattery);
		final GeneralItemStackHandler handler = this.itemStackHandler.orElseGet(this::createItemHandler);
		final Mutable currentPos = new BlockPos.Mutable();
		while (y >= 0) {
			currentPos.setPos(x, y, z);
			final BlockState state = this.world.getBlockState(currentPos);
			final Block block = state.getBlock();
			if (block instanceof BlockWaterPipe) {
				count++;
				y--;
				if (y >= 0) {
					continue;
				}
			}
			if (bedrocks.contains(block) || (!Config.pump.getGroundwaterPumpCheckBedrock() && y < 0)) {
				this.structureComplete = true;
				this.pipeCounter = count;
				this.energyUsage = Config.pump.getGroundwaterPumpEnergyBaseUsage()
						+ Config.pump.getGroundwaterPumpEnergyPipeMultiplier() * this.pipeCounter;
				return;
			} else if ((block instanceof AirBlock || block == Blocks.WATER)
					&& internalBattery.hasEnoughEnergy(Config.pump.getGroundwaterPumpEnergyPipePlacement())
					&& placePipe(currentPos, handler)) {
				internalBattery.extractInternal(Config.pump.getGroundwaterPumpEnergyPipePlacement(), false);
				break;
			}
			this.structureComplete = false;
			break;
		}

	}

	private boolean placePipe(BlockPos currentPos, ItemStackHandler handler) {
		final HashMap<ItemStack, Integer> stacks = getPipeStacks(handler);
		if (!stacks.isEmpty()) {
			final BlockWaterPipe pipe = Registration.waterPipeBlock.get();
			stacks.forEach((stack, slot) -> {
				if (pipe != null && this.world.setBlockState(currentPos, pipe.getBlockStateForPlacement(this.world, currentPos), 2)) {
					if (stack.getCount() > 1) {
						stack.shrink(1);
					} else {
						handler.setStackInSlot(slot.intValue(), ItemStack.EMPTY);
					}
					return;
				}
			});
			return true;
		}
		return false;
	}

	private HashMap<ItemStack, Integer> getPipeStacks(ItemStackHandler handler) {
		final HashMap<ItemStack, Integer> pipeStack = new HashMap<>();
		for (int i = PIPE_INVENTARY_SLOT_START; i < this.inventorySize; i++) {
			final ItemStack stack = handler.getStackInSlot(i);
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

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction facing) {
		if (cap == CapabilityEnergy.ENERGY) {
			return this.battery.cast();
		}
		return super.getCapability(cap, facing);
	}

	@Override
	protected void sendUpdatePacket() {
		WaterworksPacketHandler.sendToAllAround(new PumpPacket(this), this);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		this.battery.ifPresent(b -> b.write(compound));
		compound.putInt(NBT_INT_ENERGY_USAGE, this.energyUsage);
		compound.putInt(NBT_INT_PIPE_COUNTER, this.pipeCounter);
		compound.putBoolean(NBT_BOOLEAN_STRUCTURE_COMPLETE, this.structureComplete);
		return compound;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.battery.ifPresent(b -> b.read(compound));
		if (compound.contains(NBT_INT_ENERGY_USAGE)) {
			this.energyUsage = compound.getInt(NBT_INT_ENERGY_USAGE);
		}
		if (compound.contains(NBT_INT_PIPE_COUNTER)) {
			this.pipeCounter = compound.getInt(NBT_INT_PIPE_COUNTER);
		}
		if (compound.contains(NBT_BOOLEAN_STRUCTURE_COMPLETE)) {
			this.structureComplete = compound.getBoolean(NBT_BOOLEAN_STRUCTURE_COMPLETE);
		}
	}

	// server side GUI container
	@Override
	public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity entity) {
		return new PumpContainer(windowId, inventory, this);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("block.waterworks.groundwater_pump");
	}
}
