package org.terpo.waterworks.inventory;

import org.terpo.waterworks.helper.FluidItemStackHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FluidSlotItemHandler extends SlotItemHandler {

	private final int index;

	private SlotDefinition slotDefinition = null;
	public FluidSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition,
			SlotDefinition slotDefinition) {
		super(itemHandler, index, xPosition, yPosition);
		this.index = index;
		this.slotDefinition = slotDefinition;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		if (this.slotDefinition == SlotDefinition.O) {
			return false;
		}
		if (stack != ItemStack.EMPTY) {
			return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		}
		return super.isItemValid(stack);
	}

	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		final IItemHandler handler = this.getItemHandler();
		if (handler instanceof FluidItemStackHandler) {
			return ((FluidItemStackHandler) handler).extractItemByPlayer(this.index, 1, true) != null;
		}
		return handler.extractItem(this.index, 1, false) != ItemStack.EMPTY;
	}

	@Override
	public ItemStack decrStackSize(int amount) {
		final IItemHandler handler = this.getItemHandler();
		if (handler instanceof FluidItemStackHandler) {
			return ((FluidItemStackHandler) handler).extractItemByPlayer(this.index, amount, false);
		}
		return handler.extractItem(this.index, amount, false);
	}
}
