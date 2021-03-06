package org.terpo.waterworks.inventory;

import org.terpo.waterworks.helper.GeneralItemStackHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FluidSlotItemHandler extends SlotItemHandler {

	private final int index;

	private SlotDefinition slotDefinition = null;
	public FluidSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition, SlotDefinition slotDefinition) {
		super(itemHandler, index, xPosition, yPosition);
		this.index = index;
		this.slotDefinition = slotDefinition;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		if (!isSlotValid()) {
			return false;
		}
		if (!stack.isEmpty()) {
			return (isFilteredItemValid(stack));
		}
		return super.isItemValid(stack);
	}

	protected boolean isFilteredItemValid(ItemStack stack) {
		return (stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent() || additionalFilters(stack));
	}

	/**
	 * 
	 * @param stack use this
	 * @return
	 */
	protected boolean additionalFilters(ItemStack stack) {
		return false;
	}

	protected boolean isSlotValid() {
		return this.slotDefinition != SlotDefinition.O;
	}

	@Override
	public boolean canTakeStack(PlayerEntity playerIn) {
		final IItemHandler handler = this.getItemHandler();
		if (handler instanceof GeneralItemStackHandler) {
			return ((GeneralItemStackHandler) handler).extractItemByPlayer(this.index, 1, true) != null;
		}
		return !(handler.extractItem(this.index, 1, false).isEmpty());
	}

	@Override
	public ItemStack decrStackSize(int amount) {
		final IItemHandler handler = this.getItemHandler();
		if (handler instanceof GeneralItemStackHandler) {
			return ((GeneralItemStackHandler) handler).extractItemByPlayer(this.index, amount, false);
		}
		return handler.extractItem(this.index, amount, false);
	}
}
