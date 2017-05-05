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
		// Water Pipe
		if (WaterworksConfig.RECIPE_WATER_PIPE && WaterworksConfig.REGISTER_WATER_PIPE) {
			GameRegistry.addShapedRecipe(new ItemStack(WaterworksBlocks.water_pipe, 8), "IBI", "IBI", "IBI", 'I',
					Items.IRON_INGOT, 'B', Blocks.IRON_BARS);
		}
		// Raintank
		if (WaterworksConfig.RECIPE_RAIN_TANK && WaterworksConfig.REGISTER_RAIN_TANK) {
			GameRegistry.addShapedRecipe(new ItemStack(WaterworksBlocks.rain_tank_wood, 1), "P P", "PSP", "BBB", 'P',
					Blocks.PLANKS, 'S', Blocks.WOODEN_SLAB, 'B', Blocks.STONE);
		}

		if (WaterworksConfig.RECIPE_RAIN_COLLECTING_MULTIBLOCK && WaterworksConfig.REGISTER_RAIN_COLLECTING_MULTIBLOCK
				&& WaterworksConfig.REGISTER_WATER_PIPE) {
			// Collector
			GameRegistry.addShapedRecipe(new ItemStack(WaterworksBlocks.rain_collector, 1), "IMI", "T T", "III", 'T',
					WaterworksBlocks.water_pipe, 'I', Items.IRON_INGOT, 'M', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
			// Collector Controller
			if (WaterworksConfig.REGISTER_RAIN_TANK) {
				GameRegistry.addShapedRecipe(new ItemStack(WaterworksBlocks.rain_collector_controller, 1), "ICI", "PTP",
						"III", 'P', WaterworksBlocks.water_pipe, 'T', WaterworksBlocks.rain_tank_wood, 'I',
						Items.IRON_INGOT, 'C', new ItemStack(WaterworksItems.materials, 1, 1));
			}
		}
		// Groundwater Pump
		if (WaterworksConfig.RECIPE_GROUNDWATER_PUMP && WaterworksConfig.REGISTER_GROUNDWATER_PUMP
				&& WaterworksConfig.REGISTER_RAIN_TANK && WaterworksConfig.REGISTER_WATER_PIPE) {
			GameRegistry.addShapedRecipe(new ItemStack(WaterworksBlocks.groundwater_pump, 1), "ABA", "STS", "SPS", 'P',
					WaterworksBlocks.water_pipe, 'I', Items.IRON_INGOT, 'S', Blocks.IRON_BLOCK, 'A', Blocks.IRON_BARS,
					'T', WaterworksBlocks.rain_tank_wood, 'B', new ItemStack(WaterworksItems.materials, 1, 0));
		}
		// ITEMS
		GameRegistry.addShapedRecipe(new ItemStack(WaterworksItems.materials, 1, 0), " G ", "GRG", " G ", 'G',
				Items.GOLD_NUGGET, 'R', Items.REDSTONE);
		GameRegistry.addShapedRecipe(new ItemStack(WaterworksItems.materials, 1, 1), " G ", "GEG", " G ", 'G',
				Items.GOLD_NUGGET, 'E', Items.ENDER_PEARL);
		GameRegistry.addShapedRecipe(new ItemStack(WaterworksItems.pipe_wrench, 1), " II", "  L", "  I", 'I',
				Items.IRON_INGOT, 'L', new ItemStack(Items.DYE, 1, 4));
		registerFireworks();
	}

	public static void registerFireworks() {
		if (WaterworksConfig.RECIPE_RAIN_ROCKET && WaterworksConfig.REGISTER_RAIN_ROCKET) {
			GameRegistry.addRecipe(new RainRocketRecipe());
			RecipeSorter.register(WaterworksReference.DOMAIN + "shapeless_firework_rain", RainRocketRecipe.class,
					Category.SHAPELESS, "after:minecraft:shapeless");
		}
		if (WaterworksConfig.RECIPE_ANTI_RAIN_ROCKET && WaterworksConfig.REGISTER_ANTI_RAIN_ROCKET) {
			GameRegistry.addRecipe(new AntiRainRocketRecipe());
			RecipeSorter.register(WaterworksReference.DOMAIN + "shapeless_firework_anti_rain",
					AntiRainRocketRecipe.class, Category.SHAPELESS, "after:minecraft:shapeless");
		}
	}
}
