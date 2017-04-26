package org.terpo.waterworks.inventory;

import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public class WaterworksInventoryHelper {
	public static void dropItemsFromInventory(World world, BlockPos pos, IItemHandler inventory) {
		int x, y, z;
		x = pos.getX();
		y = pos.getY();
		z = pos.getZ();

		for (int i = 0; i < inventory.getSlots(); ++i) {
			final ItemStack itemstack = inventory.getStackInSlot(i);
			if (!itemstack.isEmpty()) {
				InventoryHelper.spawnItemStack(world, x, y, z, itemstack);
			}
		}
	}
}
