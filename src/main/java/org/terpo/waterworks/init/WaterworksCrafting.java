package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;
import org.terpo.waterworks.item.crafting.AntiRainRocketRecipe;
import org.terpo.waterworks.item.crafting.RainRocketRecipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class WaterworksCrafting {

	public static void register() {
		registerFireworks();
	}

	public static void registerFireworks() {
		final IForgeRegistry<IRecipe> registry = ForgeRegistries.RECIPES;
		if (WaterworksConfig.RECIPE_RAIN_ROCKET && WaterworksConfig.REGISTER_RAIN_ROCKET) {
			final RainRocketRecipe rainRocketRecipe = new RainRocketRecipe();
			registry.register(rainRocketRecipe.setRegistryName(WaterworksReference.MODID,
					WaterworksRegistryNames.ITEM_FIREWORK_RAIN));
//			RecipeSorter.register(WaterworksReference.DOMAIN + "shapeless_firework_rain", RainRocketRecipe.class,
//					Category.SHAPELESS, "after:minecraft:shapeless");
		}
		if (WaterworksConfig.RECIPE_ANTI_RAIN_ROCKET && WaterworksConfig.REGISTER_ANTI_RAIN_ROCKET) {
			final AntiRainRocketRecipe antiRainRocketRecipe = new AntiRainRocketRecipe();
			registry.register(antiRainRocketRecipe.setRegistryName(WaterworksReference.MODID,
					WaterworksRegistryNames.ITEM_FIREWORK_ANTI_RAIN));
//			RecipeSorter.register(WaterworksReference.DOMAIN + "shapeless_firework_anti_rain",
//					AntiRainRocketRecipe.class, Category.SHAPELESS, "after:minecraft:shapeless");
		}

	}
}
