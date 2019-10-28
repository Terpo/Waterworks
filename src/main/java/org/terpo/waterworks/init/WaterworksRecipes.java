package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;
import org.terpo.waterworks.item.crafting.AntiRainRocketRecipe;
import org.terpo.waterworks.item.crafting.RainRocketRecipe;

import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.registries.ObjectHolder;

public class WaterworksRecipes {

	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.ITEM_FIREWORK_RAIN)
	public static final SpecialRecipeSerializer<RainRocketRecipe> recipeFireworkRain = null;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.ITEM_FIREWORK_ANTI_RAIN)
	public static final SpecialRecipeSerializer<AntiRainRocketRecipe> recipeFireworkAntiRain = null;

	private WaterworksRecipes() {
		// hide me
	}
}
