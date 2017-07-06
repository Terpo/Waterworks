package org.terpo.waterworks.helper;

import java.util.HashMap;

import org.terpo.waterworks.inventory.SlotDefinition;
import org.terpo.waterworks.tileentity.BaseTileEntity;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class GeneralItemStackHandler extends ItemStackHandler {

	private boolean inputSlots[];
	private boolean outputSlots[];
	HashMap<Item, SlotDefinition> filter = new HashMap<>();
	protected TileEntity tile;

	public GeneralItemStackHandler() {
		super(1);
	}

	public GeneralItemStackHandler(int size, TileEntity tE) {
		super(size);
		this.inputSlots = new boolean[size];
		this.outputSlots = new boolean[size];

		this.filter.put(Items.GLASS_BOTTLE, SlotDefinition.I);
		this.filter.put(Items.POTIONITEM, SlotDefinition.O);

		for (int i = 0; i < size; i++) {
			this.inputSlots[i] = false;
			this.outputSlots[i] = false;
		}

		this.tile = tE;
	}

	public void setTileEntity(TileEntity tile) {
		this.tile = tile;
	}

	@Override
	protected void onContentsChanged(int slot) {
		if (this.tile instanceof BaseTileEntity) {
			((BaseTileEntity) this.tile).setDirty(true);
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
	public void setIOFlagForIndex(int slot, boolean allowed) {
		validateSlotIndex(slot);
		this.inputSlots[slot] = allowed;
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

	public GeneralItemStackHandler(NonNullList<ItemStack> stacks) {
		super(stacks);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		}

		if (!isValidItemStack(stack, slot)) {
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

	protected boolean isValidItemStack(ItemStack stack, int slot) {
		if (slot <= 1) {
			return (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)
					|| additionalFilters(stack, slot));
		}
		return false;
	}

	protected boolean additionalFilters(ItemStack stack, int slot) {
		final Item item = stack.getItem();
		if (!this.filter.isEmpty()) {
			SlotDefinition def;
			if (this.filter.containsKey(item)) {
				def = this.filter.get(item);
				return checkSlotDefinitionForSlot(def, slot);
			}
		}
		return false;
	}

	private boolean checkSlotDefinitionForSlot(SlotDefinition def, int slot) {
		switch (def) {
			case I :
				return (isInputSlot(slot));
			case O :
				return (isOutputSlot(slot));
			case IO :
				return (isInputSlot(slot) && isOutputSlot(slot));
			default :
				return false;
		}
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
