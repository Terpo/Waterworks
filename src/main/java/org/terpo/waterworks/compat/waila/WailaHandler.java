package org.terpo.waterworks.compat.waila;

import org.terpo.waterworks.api.constants.Compat;
import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.compat.waila.provider.ProviderTileWaterworks;
import org.terpo.waterworks.tileentity.TileWaterworks;

import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaHandler {
	public static void register(IWailaRegistrar registrar) {
		// providers
		registrar.registerBodyProvider(new ProviderTileWaterworks(), TileWaterworks.class);

		// config
		registrar.addConfig(WaterworksReference.MODID, Compat.WAILA_CONFIG_TANKINFO, true);
	}
}
