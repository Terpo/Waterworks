package org.terpo.waterworks.compat.jei;

import org.terpo.waterworks.compat.jei.rockets.JEIAntiRainRocketRecipeWrapperFactory;
import org.terpo.waterworks.compat.jei.rockets.JEIRainRocketRecipeWrapperFactory;
import org.terpo.waterworks.item.crafting.AntiRainRocketRecipe;
import org.terpo.waterworks.item.crafting.RainRocketRecipe;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

@JEIPlugin
public class JEICompatibility extends BlankModPlugin {
	@Override
	public void register(IModRegistry registry) {

		// Add Rocket Recipe Handler
		for (int i = 1; i < 9; i++) {
			registry.handleRecipes(RainRocketRecipe.class, new JEIRainRocketRecipeWrapperFactory(),
					VanillaRecipeCategoryUid.CRAFTING);
			registry.handleRecipes(AntiRainRocketRecipe.class, new JEIAntiRainRocketRecipeWrapperFactory(),
					VanillaRecipeCategoryUid.CRAFTING);
		}
		// Add Descriptions
		JEIDescription.addJEIDescriptions(registry);
	}
}
