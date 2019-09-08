package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;
import org.terpo.waterworks.tileentity.TileEntityGroundwaterPump;
import org.terpo.waterworks.tileentity.TileEntityRainCollector;
import org.terpo.waterworks.tileentity.TileEntityRainCollectorController;
import org.terpo.waterworks.tileentity.TileEntityRainTankWood;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.IForgeRegistry;

public class InitTileEntities {
	public static void register(IForgeRegistry<TileEntityType<?>> registry) {
		registry.register(TileEntityType.Builder.create(TileEntityRainTankWood::new, WaterworksBlocks.rainTankWood)
				.build(null)
				.setRegistryName(WaterworksReference.MODID, WaterworksRegistryNames.TILE_ENTITY_RAIN_TANK_WOOD));
		registry.register(TileEntityType.Builder
				.create(TileEntityRainCollectorController::new, WaterworksBlocks.rainCollectorController).build(null)
				.setRegistryName(WaterworksReference.MODID,
						WaterworksRegistryNames.TILE_ENTITY_RAIN_COLLECTOR_CONTROLLER));
		registry.register(TileEntityType.Builder.create(TileEntityRainCollector::new, WaterworksBlocks.rainCollector)
				.build(null)
				.setRegistryName(WaterworksReference.MODID, WaterworksRegistryNames.TILE_ENTITY_RAIN_COLLECTOR));
		registry.register(TileEntityType.Builder
				.create(TileEntityGroundwaterPump::new, WaterworksBlocks.groundwaterPump).build(null)
				.setRegistryName(WaterworksReference.MODID, WaterworksRegistryNames.TILE_ENTITY_GROUNDWATER_PUMP));
//		registry.register(TileEntityType.Builder.create(BaseTileEntity::new).build(null).setRegistryName(
//				new ResourceLocation(WaterworksReference.MODID, WaterworksRegistryNames.TILE_ENTITY_BASE)));
//		registry.register(TileEntityType.Builder.create(TileWaterworks::new).build(null).setRegistryName(
//				new ResourceLocation(WaterworksReference.MODID, WaterworksRegistryNames.TILE_ENTITY_WATERWORKS)));
	}

	private InitTileEntities() {
		// hide me
	}
}
