package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;
import org.terpo.waterworks.item.crafting.AntiRainRocketRecipe;
import org.terpo.waterworks.item.crafting.RainRocketRecipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class WaterworksCrafting {

	public static void register() {
		registerFireworks();
	}

	public static void registerFireworks() {
		final IForgeRegistry<IRecipeSerializer<?>> registry = ForgeRegistries.RECIPE_SERIALIZERS;
		if (WaterworksConfig.recipes.recipeRainRocket && WaterworksConfig.register.rainRocket) {
			final RainRocketRecipe rainRocketRecipe = new RainRocketRecipe();
			registry.register(rainRocketRecipe.setRegistryName(WaterworksReference.MODID,
					WaterworksRegistryNames.ITEM_FIREWORK_RAIN));
//			RecipeSorter.register(WaterworksReference.DOMAIN + "shapeless_firework_rain", RainRocketRecipe.class,
//					Category.SHAPELESS, "after:minecraft:shapeless");
		}
		if (WaterworksConfig.recipes.recipeAntiRainRocket && WaterworksConfig.register.antiRainRocket) {
			final AntiRainRocketRecipe antiRainRocketRecipe = new AntiRainRocketRecipe();
			registry.register(antiRainRocketRecipe.setRegistryName(WaterworksReference.MODID,
					WaterworksRegistryNames.ITEM_FIREWORK_ANTI_RAIN));
//			RecipeSorter.register(WaterworksReference.DOMAIN + "shapeless_firework_anti_rain",
//					AntiRainRocketRecipe.class, Category.SHAPELESS, "after:minecraft:shapeless");
		}

	}
}
