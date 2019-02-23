package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WaterworksBlocks {
	@GameRegistry.ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_RAIN_TANK_WOOD)
	public static Block rainTankWood;
	@GameRegistry.ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_WATER_PIPE)
	public static Block waterPipe;
	@GameRegistry.ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_RAIN_COLLECTOR)
	public static Block rainCollector;
	@GameRegistry.ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_RAIN_COLLECTOR_CONTROLLER)
	public static Block rainCollectorController;
	@GameRegistry.ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_GROUNDWATER_PUMP)
	public static Block groundwaterPump;

	private WaterworksBlocks() {
		// hidme
	}
}
