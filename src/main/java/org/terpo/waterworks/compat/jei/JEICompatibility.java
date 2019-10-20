package org.terpo.waterworks.compat.jei;

import org.terpo.waterworks.api.constants.WaterworksReference;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JEICompatibility implements IModPlugin {

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(WaterworksReference.MODID, "jeiplugin");
	}

	@Override
	public void registerRecipes(IRecipeRegistration registry) {

		// FIXME add recipe handlers for JEI
		// Add Rocket Recipe Handler
//		registry.handleRecipes(RainRocketRecipe.class, new JEIRainRocketRecipeWrapperFactory(),
//				VanillaRecipeCategoryUid.CRAFTING);
//		registry.handleRecipes(AntiRainRocketRecipe.class, new JEIAntiRainRocketRecipeWrapperFactory(),
//				VanillaRecipeCategoryUid.CRAFTING);

		// Add Descriptions
		JEIDescription.addJEIDescriptions(registry);
	}
}
