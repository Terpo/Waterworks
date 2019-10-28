package org.terpo.waterworks.compat.top;

import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraftforge.fml.InterModComms;

public class TOPCompatibility {

	public static final String TOP_MOD_ID = "theoneprobe";
	private static boolean registered;
	public static ITheOneProbe probe;

	public static void register() {
		if (registered) {
			return;
		}
		registered = true;
		InterModComms.sendTo(TOPCompatibility.TOP_MOD_ID, "getTheOneProbe", TOPRegistration::new);
	}

	private TOPCompatibility() {
		// hidden
	}
}
