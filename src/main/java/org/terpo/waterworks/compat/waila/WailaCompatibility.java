package org.terpo.waterworks.compat.waila;

import net.minecraftforge.fml.common.event.FMLInterModComms;

public class WailaCompatibility {
	public static String modId = "waila";

	public static void loadCompat() {
		FMLInterModComms.sendMessage(WailaCompatibility.modId, "register",
				"org.terpo.waterworks.compat.waila.WailaHandler.register");
	}
}
