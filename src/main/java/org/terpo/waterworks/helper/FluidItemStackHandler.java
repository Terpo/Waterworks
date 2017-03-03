package org.terpo.waterworks.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
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

	public FluidItemStackHandler(NonNullList<ItemStack> stacks) {
		super(stacks);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		}

		if (!isValidItemStack(stack)) {
			return stack;
		}

		if (!isInputSlot(slot)) {
			return stack;
		}

		final ItemStack existing = this.stacks.get(slot);

		int limit = getStackLimit(slot, stack);

		if (!existing.isEmpty()) {
			if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
				return stack;
			}

			limit -= existing.getCount();
		}

		if (limit <= 0) {
			return stack;
		}

		final boolean reachedLimit = stack.getCount() > limit;

		if (!simulate) {
			if (existing.isEmpty()) {
				this.stacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
			} else {
				existing.grow(reachedLimit ? limit : stack.getCount());
			}
			onContentsChanged(slot);
		}

		return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
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
			return ItemStack.EMPTY;
		}

		if (!isOutputSlot(slot)) {
			return ItemStack.EMPTY;
		}

		final ItemStack existing = this.stacks.get(slot);

		if (existing.isEmpty()) {
			return ItemStack.EMPTY;
		}

		final int toExtract = Math.min(amount, existing.getMaxStackSize());

		if (existing.getCount() <= toExtract) {
			if (!simulate) {
				this.stacks.set(slot, ItemStack.EMPTY);
				onContentsChanged(slot);
			}
			return existing;
		}

		if (!simulate) {
			this.stacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
			onContentsChanged(slot);
		}

		return ItemHandlerHelper.copyStackWithSize(existing, toExtract);

	}

	public ItemStack extractItemByPlayer(int slot, int amount, boolean simulate) {
		if (amount == 0) {
			return null;
		}

		final ItemStack existing = this.stacks.get(slot);

		if (existing.isEmpty()) {
			return ItemStack.EMPTY;
		}

		final int toExtract = Math.min(amount, existing.getMaxStackSize());

		if (existing.getCount() <= toExtract) {
			if (!simulate) {
				this.stacks.set(slot, ItemStack.EMPTY);
				onContentsChanged(slot);
			}
			return existing;
		}

		if (!simulate) {
			this.stacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
			onContentsChanged(slot);
		}

		return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
	}
}
