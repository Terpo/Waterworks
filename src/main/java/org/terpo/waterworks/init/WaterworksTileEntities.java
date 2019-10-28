package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;
import org.terpo.waterworks.tileentity.TileEntityGroundwaterPump;
import org.terpo.waterworks.tileentity.TileEntityRainCollector;
import org.terpo.waterworks.tileentity.TileEntityRainCollectorController;
import org.terpo.waterworks.tileentity.TileEntityRainTankWood;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(WaterworksReference.MODID)
public class WaterworksTileEntities {
	@ObjectHolder(WaterworksRegistryNames.BLOCK_RAIN_TANK_WOOD)
	public static final TileEntityType<TileEntityRainTankWood> rainTankWood = null;
	@ObjectHolder(WaterworksRegistryNames.BLOCK_RAIN_COLLECTOR)
	public static final TileEntityType<TileEntityRainCollector> rainCollector = null;
	@ObjectHolder(WaterworksRegistryNames.BLOCK_RAIN_COLLECTOR_CONTROLLER)
	public static final TileEntityType<TileEntityRainCollectorController> rainCollectorController = null;
	@ObjectHolder(WaterworksRegistryNames.BLOCK_GROUNDWATER_PUMP)
	public static final TileEntityType<TileEntityGroundwaterPump> groundwaterPump = null;

	private WaterworksTileEntities() {
		// hidme
	}
}
