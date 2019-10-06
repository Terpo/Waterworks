package org.terpo.waterworks.gui.pump;

import org.terpo.waterworks.gui.ContainerBase;
import org.terpo.waterworks.init.WaterworksContainers;
import org.terpo.waterworks.inventory.FilteredSlotItemHandler;
import org.terpo.waterworks.inventory.SlotDefinition;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class PumpContainer extends ContainerBase {

	public PumpContainer(int windowId, IInventory playerInv, TileEntity te) {
		super(WaterworksContainers.groundwaterPump, windowId, playerInv, te);
	}

	@Override
	protected void addOwnSlots(IItemHandler handler) {
		super.addOwnSlots(handler);
		// Tile Entity, Slot 2-4, Slot IDs 2-4
		// 2 - IO
		// 3 - IO
		// 4 - IO
		final SlotItemHandler pipeSlot1 = new FilteredSlotItemHandler(handler, 2, 152, 17, SlotDefinition.I);
		final SlotItemHandler pipeSlot2 = new FilteredSlotItemHandler(handler, 3, 152, 35, SlotDefinition.I);
		final SlotItemHandler pipeSlot3 = new FilteredSlotItemHandler(handler, 4, 152, 53, SlotDefinition.I);

		addSlot(pipeSlot1);
		addSlot(pipeSlot2);
		addSlot(pipeSlot3);
	}

}
