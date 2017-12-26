package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WaterworksBlocks {
	@GameRegistry.ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_RAIN_TANK_WOOD)
	public static Block rain_tank_wood;
	@GameRegistry.ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_WATER_PIPE)
	public static Block water_pipe;
	@GameRegistry.ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_RAIN_COLLECTOR)
	public static Block rain_collector;
	@GameRegistry.ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_RAIN_COLLECTOR_CONTROLLER)
	public static Block rain_collector_controller;
	@GameRegistry.ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_GROUNDWATER_PUMP)
	public static Block groundwater_pump;
}
