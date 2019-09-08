package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;
import org.terpo.waterworks.tileentity.TileEntityGroundwaterPump;
import org.terpo.waterworks.tileentity.TileEntityRainCollector;
import org.terpo.waterworks.tileentity.TileEntityRainCollectorController;
import org.terpo.waterworks.tileentity.TileEntityRainTankWood;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class WaterworksTileEntities {
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_RAIN_TANK_WOOD)
	public static TileEntityType<TileEntityRainTankWood> rainTankWood;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_RAIN_COLLECTOR)
	public static TileEntityType<TileEntityRainCollector> rainCollector;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_RAIN_COLLECTOR_CONTROLLER)
	public static TileEntityType<TileEntityRainCollectorController> rainCollectorController;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_GROUNDWATER_PUMP)
	public static TileEntityType<TileEntityGroundwaterPump> groundwaterPump;

	private WaterworksTileEntities() {
		// hidme
	}
}
