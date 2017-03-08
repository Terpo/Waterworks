package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.item.crafting.ChargeRain;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
@SuppressWarnings("boxing")
public class WaterworksCrafting {

	public static void register() {
		GameRegistry.addShapedRecipe(new ItemStack(WaterworksBlocks.water_pipe, 8), "IBI", "IBI", "IBI", 'I',
				Items.IRON_INGOT, 'B', Blocks.IRON_BARS);
		GameRegistry.addShapedRecipe(new ItemStack(WaterworksBlocks.rain_tank_wood, 1), "P P", "PSP", "BBB", 'P',
				Blocks.PLANKS, 'S', Blocks.WOODEN_SLAB, 'B', Blocks.STONE);
		registerFireworks();
	}

	public static void registerFireworks() {
		GameRegistry.addRecipe(new ChargeRain());
		RecipeSorter.register(WaterworksReference.DOMAIN + "shapeless_firework_rain", ChargeRain.class,
				Category.SHAPELESS, "after:minecraft:shapeless");
	}
}
