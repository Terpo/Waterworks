package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;

import net.minecraft.block.Block;
import net.minecraftforge.registries.ObjectHolder;

public class WaterworksBlocks {
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_RAIN_TANK_WOOD)
	public static final Block rainTankWood = null;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_WATER_PIPE)
	public static final Block waterPipe = null;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_RAIN_COLLECTOR)
	public static final Block rainCollector = null;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_RAIN_COLLECTOR_CONTROLLER)
	public static final Block rainCollectorController = null;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_GROUNDWATER_PUMP)
	public static final Block groundwaterPump = null;

	private WaterworksBlocks() {
		// hidme
	}
}
