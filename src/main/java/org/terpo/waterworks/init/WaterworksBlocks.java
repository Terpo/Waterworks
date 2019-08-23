package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;

import net.minecraft.block.Block;
import net.minecraftforge.registries.ObjectHolder;

public class WaterworksBlocks {
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_RAIN_TANK_WOOD)
	public static Block rainTankWood;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_WATER_PIPE)
	public static Block waterPipe;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_RAIN_COLLECTOR)
	public static Block rainCollector;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_RAIN_COLLECTOR_CONTROLLER)
	public static Block rainCollectorController;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_GROUNDWATER_PUMP)
	public static Block groundwaterPump;

	private WaterworksBlocks() {
		// hidme
	}
}
