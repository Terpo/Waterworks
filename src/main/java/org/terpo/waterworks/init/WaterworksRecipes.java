package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;
import org.terpo.waterworks.item.crafting.AntiRainRocketRecipe;
import org.terpo.waterworks.item.crafting.RainRocketRecipe;

import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.registries.ObjectHolder;

public class WaterworksRecipes {

	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.ITEM_FIREWORK_RAIN)
	public static SpecialRecipeSerializer<RainRocketRecipe> recipeFireworkRain;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.ITEM_FIREWORK_ANTI_RAIN)
	public static SpecialRecipeSerializer<AntiRainRocketRecipe> recipeFireworkAntiRain;

	private WaterworksRecipes() {
		// hide me
	}
}
