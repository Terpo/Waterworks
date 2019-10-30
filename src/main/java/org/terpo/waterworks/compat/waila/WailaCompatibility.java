package org.terpo.waterworks.compat.waila;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.WailaPlugin;

//FIXME Readd waila compat
@WailaPlugin
public class WailaCompatibility implements IWailaPlugin {
	public static final String WAILA_MOD_ID = "waila";

	public static void register() {
//		if (ModList.get().isLoaded(WAILA_MOD_ID)) {
//		InterModComms.sendTo(WailaCompatibility.modId, "register",
//				() -> "org.terpo.waterworks.compat.waila.WailaHandler.register");
//		}
	}

	@Override
	public void register(IRegistrar registrar) {
		// providers
//		registrar.registerBlockDataProvider(new ProviderTileWaterworks(), TileWaterworks.class);
		// config
//		registrar.addSyncedConfig(new ResourceLocation(WaterworksReference.MODID, Compat.WAILA_CONFIG_TANKINFO), true);

	}
}
