package org.terpo.waterworks.tileentity;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.terpo.waterworks.fluid.WaterworksTank;
import org.terpo.waterworks.helper.GeneralItemStackHandler;
import org.terpo.waterworks.network.TankPacket;
import org.terpo.waterworks.network.WaterworksPacketHandler;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.INamedContainerProvider;
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
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileWaterworks extends BaseTileEntity implements ITickableTileEntity, INamedContainerProvider {

	private static final String NBT_ITEMS = "items";
	private static final Random random = new Random();
	private int currentTick = random.nextInt(256);
	protected final LazyOptional<WaterworksTank> fluidTank = LazyOptional.of(this::createTank);
	protected LazyOptional<GeneralItemStackHandler> itemStackHandler = LazyOptional.of(this::createItemHandler);

	protected int inventorySize;
	protected int tankSize;

	// TileEntity
	public TileWaterworks(TileEntityType<?> tileEntityTypeIn, int inventorySize, int tankSize) {
		super(tileEntityTypeIn);
		this.inventorySize = inventorySize;
		this.tankSize = tankSize;
	}

	public TileWaterworks(TileEntityType<?> tileEntityTypeIn) {
		this(tileEntityTypeIn, 2, 8000);
	}

	protected WaterworksTank createTank() {
		return new WaterworksTank(this.tankSize, this);
	}

	protected abstract GeneralItemStackHandler createItemHandler();

	protected void sendUpdatePacket() {
		WaterworksPacketHandler.sendToAllAround(new TankPacket(this), this);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		this.itemStackHandler.ifPresent(handler -> compound.put(NBT_ITEMS, handler.serializeNBT()));
		this.fluidTank.ifPresent(tank -> tank.writeToNBT(compound));
		return compound;
	}

	@Override
	public void fromTag(BlockState state,CompoundNBT compound) {
		super.fromTag(state, compound);
		if (compound.contains(NBT_ITEMS)) {
			this.itemStackHandler.ifPresent(handler -> handler.deserializeNBT(compound.getCompound(NBT_ITEMS)));
		}
		this.fluidTank.ifPresent(tank -> tank.readFromNBT(compound));

	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return this.itemStackHandler.cast();
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return this.fluidTank.cast();

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
		}
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

	public int getInventorySize() {
		return this.inventorySize;
	}

	public int getComparatorOutput() {
		final WaterworksTank tank = this.fluidTank.orElseGet(this::createTank);
		if (tank.isEmpty()) {
			return 0;
		}
		return (int) (1 + ((double) tank.getFluidAmount() / (double) tank.getCapacity()) * 14);
	}

	public WaterworksTank getFluidTank() {
		return this.fluidTank.orElseGet(this::createTank);
	}

	public boolean isDirty() {
		return this.isDirty;
	}

	// FluidStuff
	protected boolean fillFluid() {
		final WaterworksTank tank = this.fluidTank.orElseGet(this::createTank);
		final GeneralItemStackHandler handler = this.itemStackHandler.orElseGet(this::createItemHandler);
		final int internalFluidAmount = tank.getFluidAmount();
		if (internalFluidAmount > 0) {
			final ItemStack stackInput = handler.getStackInSlot(0);
			if (!stackInput.isEmpty()) {
				if (handleFluidCapabilityItem(tank, handler, stackInput)) {
					return true;
				}
				if (stackInput.getItem().equals(Items.GLASS_BOTTLE) && internalFluidAmount >= 1000
						&& handleGlassBottle(tank, handler, stackInput)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean handleFluidCapabilityItem(final WaterworksTank tank, final GeneralItemStackHandler handler,
			final ItemStack stackInput) {
		final LazyOptional<IFluidHandlerItem> capability = stackInput.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
		if (capability.isPresent()) {
			final FluidActionResult filledSimulated = FluidUtil.tryFillContainer(stackInput, tank, Integer.MAX_VALUE, null, false);
			if (filledSimulated.isSuccess()) {
				final ItemStack simResult = filledSimulated.getResult();
				final ItemStack outputSlot = handler.getStackInSlot(1);
				if (checkOutputSlot(outputSlot, simResult)) {
					final FluidActionResult fillResult = FluidUtil.tryFillContainer(stackInput, tank, Integer.MAX_VALUE, null, true);
					if (fillResult.isSuccess()) {
						final ItemStack realResult = fillResult.getResult();
						moveFilledInputToOutput(stackInput, outputSlot, realResult, handler);
						return true;
					}
				}
			}

		}
		return false;
	}

	private static boolean handleGlassBottle(final WaterworksTank tank, final GeneralItemStackHandler handler, final ItemStack stackInput) {
		final ItemStack outputSlot = handler.getStackInSlot(1);
		final ItemStack outputStack = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER);
		if (checkOutputSlot(outputSlot, outputStack)) {
			tank.drain(1000, FluidAction.EXECUTE);
			moveFilledInputToOutput(stackInput, outputSlot, outputStack, handler);
			return true;
		}
		return false;
	}

	private static boolean checkOutputSlot(ItemStack outputSlot, ItemStack stack) {
		final boolean isItemIdentical = outputSlot.getItem().equals(stack.getItem());
		final boolean hasSpace = outputSlot.getCount() < outputSlot.getMaxStackSize();
		return (outputSlot.isEmpty() || (isItemIdentical) && (hasSpace));
	}

	private static void moveFilledInputToOutput(final ItemStack stackInput, final ItemStack outputSlot, final ItemStack realResult,
			ItemStackHandler handler) {
		if (outputSlot.isEmpty()) {
			handler.setStackInSlot(1, realResult);
		} else {
			outputSlot.grow(1);
		}
		if (stackInput.getCount() > 1) {
			stackInput.shrink(1);
		} else {
			handler.setStackInSlot(0, ItemStack.EMPTY);
		}
	}
	protected static FluidStack getWaterFluidStack(int amount) {
		return new FluidStack(Fluids.WATER, amount);
	}

	public int getCurrentTick() {
		return this.currentTick;
	}
}
