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
	public void registerRecipes(IRecipeRegistration registration) {
		// Add Descriptions
		JEIDescription.addJEIDescriptions(registration);

		// Add Rocket Recipe Handler
		JEIRocketRecipes.addRocketRecipes(registration);
	}

}
