package org.terpo.waterworks.gui.pump;

import org.terpo.waterworks.gui.ContainerBase;
import org.terpo.waterworks.inventory.FilteredSlotItemHandler;
import org.terpo.waterworks.inventory.SlotDefinition;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.SlotItemHandler;

public class PumpContainer extends ContainerBase {

	public PumpContainer(IInventory playerInv, TileWaterworks te) {
		super(playerInv, te);
	}

	@Override
	protected void addOwnSlots() {
		super.addOwnSlots();
		// Tile Entity, Slot 2-4, Slot IDs 2-4
		// 2 - IO
		// 3 - IO
		// 4 - IO
		final SlotItemHandler pipeSlot1 = new FilteredSlotItemHandler(this.itemHandler, 2, 152, 17, SlotDefinition.I);
		final SlotItemHandler pipeSlot2 = new FilteredSlotItemHandler(this.itemHandler, 3, 152, 35, SlotDefinition.I);
		final SlotItemHandler pipeSlot3 = new FilteredSlotItemHandler(this.itemHandler, 4, 152, 53, SlotDefinition.I);

		addSlotToContainer(pipeSlot1);
		addSlotToContainer(pipeSlot2);
		addSlotToContainer(pipeSlot3);
	}

}
