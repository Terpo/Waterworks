package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.item.crafting.AntiRainRocketRecipe;
import org.terpo.waterworks.item.crafting.RainRocketRecipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
@SuppressWarnings("boxing")
public class WaterworksCrafting {

	public static void register() {
		// BLOCKS
		GameRegistry.addShapedRecipe(new ItemStack(WaterworksBlocks.water_pipe, 8), "IBI", "IBI", "IBI", 'I',
				Items.IRON_INGOT, 'B', Blocks.IRON_BARS);
		GameRegistry.addShapedRecipe(new ItemStack(WaterworksBlocks.rain_tank_wood, 1), "P P", "PSP", "BBB", 'P',
				Blocks.PLANKS, 'S', Blocks.WOODEN_SLAB, 'B', Blocks.STONE);
		GameRegistry.addShapedRecipe(new ItemStack(WaterworksBlocks.groundwater_pump, 1), "ABA", "STS", "SPS", 'P',
				WaterworksBlocks.water_pipe, 'I', Items.IRON_INGOT, 'S', Blocks.IRON_BLOCK, 'A', Blocks.IRON_BARS, 'T',
				WaterworksBlocks.rain_tank_wood, 'B', Items.APPLE);
		// ITEMS
		GameRegistry.addShapedRecipe(new ItemStack(WaterworksItems.pipe_wrench, 1), " II", "  L", "  I", 'I',
				Items.IRON_INGOT, 'L', new ItemStack(Items.DYE, 1, 4));
		registerFireworks();
	}

	public static void registerFireworks() {
		GameRegistry.addRecipe(new RainRocketRecipe());
		RecipeSorter.register(WaterworksReference.DOMAIN + "shapeless_firework_rain", RainRocketRecipe.class,
				Category.SHAPELESS, "after:minecraft:shapeless");
		GameRegistry.addRecipe(new AntiRainRocketRecipe());
		RecipeSorter.register(WaterworksReference.DOMAIN + "shapeless_firework_anti_rain", AntiRainRocketRecipe.class,
				Category.SHAPELESS, "after:minecraft:shapeless");
	}
}
