package org.terpo.waterworks.compat.top;

import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;

public class TOPCompatibility {

	public static final String TOP_MOD_ID = "theoneprobe";
	private static ITheOneProbe probe;

	public static void register() {
		if (ModList.get().isLoaded(TOP_MOD_ID)) {
			InterModComms.sendTo(TOPCompatibility.TOP_MOD_ID, "getTheOneProbe", TOPRegistration::new);
		}
	}

	private TOPCompatibility() {
		// hidden
	}

	public static ITheOneProbe getProbe() {
		return probe;
	}

	public static void setProbe(ITheOneProbe probe) {
		TOPCompatibility.probe = probe;
	}
}
