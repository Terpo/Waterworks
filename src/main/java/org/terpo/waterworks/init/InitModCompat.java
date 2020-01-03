package org.terpo.waterworks.init;

import org.terpo.waterworks.compat.minecraft.MinecraftCompatibility;
import org.terpo.waterworks.compat.top.TOPCompatibility;
import org.terpo.waterworks.compat.waila.WailaCompatibility;

import net.minecraftforge.fml.common.Loader;

public class InitModCompat {

	public static void init(String phase) {
		switch (phase) {
			case "pre" :
				if (Loader.isModLoaded(TOPCompatibility.modId)) {
					TOPCompatibility.register();
				}
				break;
			case "init" :
				if (Loader.isModLoaded(WailaCompatibility.modId)) {
					WailaCompatibility.register();
				}
				MinecraftCompatibility.registerWeatherRocketDispenserBehavior();
				break;
			case "post" :
				break;
			default :
				break;
		}

	}

}
