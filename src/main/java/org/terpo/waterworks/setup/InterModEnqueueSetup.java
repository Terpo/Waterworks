package org.terpo.waterworks.setup;

import org.terpo.waterworks.Config;
import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.compat.minecraft.MinecraftCompatibility;
import org.terpo.waterworks.compat.top.TOPCompatibility;

import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

public class InterModEnqueueSetup {

	public static void init(final InterModEnqueueEvent event) {// NOSONAR
		Waterworks.LOGGER.info("Waterworks IMC to other mods starting");
		TOPCompatibility.register();

		if (Config.rockets.getRocketDispenserCompatibility()) {
			MinecraftCompatibility.registerWeatherRocketDispenserBehavior();
		}
		Waterworks.LOGGER.info("Waterworks IMC to other mods complete");
	}

	private InterModEnqueueSetup() {
		// hidden
	}
}
