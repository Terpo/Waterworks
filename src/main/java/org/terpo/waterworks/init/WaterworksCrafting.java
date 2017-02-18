package org.terpo.waterworks.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WaterworksCrafting {
	@SuppressWarnings("boxing")
	public static void register() {
		GameRegistry.addShapedRecipe(new ItemStack(WaterworksBlocks.water_pipe, 8), "IBI", "IBI", "IBI", 'I',
				Items.IRON_INGOT, 'B', Blocks.IRON_BARS);
		GameRegistry.addShapedRecipe(new ItemStack(WaterworksBlocks.rain_tank_wood, 1), "P P", "PSP", "BBB", 'P',
				Blocks.PLANKS, 'S', Blocks.WOODEN_SLAB, 'B', Blocks.STONE);
	}
}
