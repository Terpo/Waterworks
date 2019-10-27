package org.terpo.waterworks.compat.top;

import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraftforge.fml.InterModComms;

public class TOPCompatibility {

	public static String modId = "theoneprobe";
	private static boolean registered;
	public static ITheOneProbe probe;

	public static void register() {
		if (registered) {
			return;
		}
		registered = true;
		InterModComms.sendTo(TOPCompatibility.modId, "getTheOneProbe", TOPRegistration::new);
	}

	private TOPCompatibility() {
		// hidden
	}
}
