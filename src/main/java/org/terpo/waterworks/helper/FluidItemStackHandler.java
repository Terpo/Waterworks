package org.terpo.waterworks.helper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class FluidItemStackHandler extends ItemStackHandler {

	private boolean inputSlots[];
	private boolean outputSlots[];

	public FluidItemStackHandler() {
		super(1);
	}

	public FluidItemStackHandler(int size) {
		super(size);
		this.inputSlots = new boolean[size];
		this.outputSlots = new boolean[size];
		for (int i = 0; i < size; i++) {
			this.inputSlots[i] = false;
			this.outputSlots[i] = false;
		}
	}

	public void setInputFlagForIndex(int slot, boolean allowed) {
		validateSlotIndex(slot);
		this.inputSlots[slot] = allowed;
	}

	public void setOutputFlagForIndex(int slot, boolean allowed) {
		validateSlotIndex(slot);
		this.outputSlots[slot] = allowed;
	}
	public boolean isInputSlot(int slot) {
		validateSlotIndex(slot);
		return this.inputSlots[slot];
	}
	public boolean isOutputSlot(int slot) {
		validateSlotIndex(slot);
		return this.outputSlots[slot];
	}

	public FluidItemStackHandler(ItemStack[] stacks) {
		super(stacks);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (stack == null || stack.stackSize == 0) {
			return null;
		}

		if (!isValidItemStack(stack)) {
			return stack;
		}

		if (!isInputSlot(slot)) {
			return stack;
		}

		final ItemStack existing = this.stacks[slot];

		int limit = getStackLimit(slot, stack);

		if (existing != null) {
			if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
				return stack;
			}

			limit -= existing.stackSize;
		}

		if (limit <= 0) {
			return stack;
		}

		final boolean reachedLimit = stack.stackSize > limit;

		if (!simulate) {
			if (existing == null) {
				this.stacks[slot] = reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack;
			} else {
				existing.stackSize += reachedLimit ? limit : stack.stackSize;
			}
			onContentsChanged(slot);
		}

		return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.stackSize - limit) : null;
	}
	private static boolean isValidItemStack(ItemStack stack) {
		if (stack != null) {
			return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
		}
		return false;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (amount == 0) {
			return null;
		}

		if (!isOutputSlot(slot)) {
			return null;
		}

		final ItemStack existing = this.stacks[slot];

		if (existing == null) {
			return null;
		}

		final int toExtract = Math.min(amount, existing.getMaxStackSize());

		if (existing.stackSize <= toExtract) {
			if (!simulate) {
				this.stacks[slot] = null;
				onContentsChanged(slot);
			}
			return existing;
		}

		if (!simulate) {
			this.stacks[slot] = ItemHandlerHelper.copyStackWithSize(existing, existing.stackSize - toExtract);
			onContentsChanged(slot);
		}

		return ItemHandlerHelper.copyStackWithSize(existing, toExtract);

	}

	public ItemStack extractItemByPlayer(int slot, int amount, boolean simulate) {
		if (amount == 0) {
			return null;
		}

		final ItemStack existing = this.stacks[slot];

		if (existing == null) {
			return null;
		}

		final int toExtract = Math.min(amount, existing.getMaxStackSize());

		if (existing.stackSize <= toExtract) {
			if (!simulate) {
				this.stacks[slot] = null;
				onContentsChanged(slot);
			}
			return existing;
		}

		if (!simulate) {
			this.stacks[slot] = ItemHandlerHelper.copyStackWithSize(existing, existing.stackSize - toExtract);
			onContentsChanged(slot);
		}

		return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
	}
}
