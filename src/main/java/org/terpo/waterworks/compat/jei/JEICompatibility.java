package org.terpo.waterworks.compat.jei;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class JEICompatibility extends BlankModPlugin {
	@Override
	public void register(IModRegistry registry) {
		JEIDescription.addJEIDescriptions(registry);
	}
}
