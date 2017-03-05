package org.terpo.waterworks.init;

import org.terpo.waterworks.compat.waila.WailaCompatibility;

import net.minecraftforge.fml.common.Loader;

public class InitModCompat {

	public static void init(String phase) {
		switch (phase) {
			case "pre" :
				break;
			case "init" :
				if (Loader.isModLoaded(WailaCompatibility.modId)) {
					WailaCompatibility.loadCompat();
				}
				break;
			case "post" :
				break;
			default :
				break;
		}

	}

}
