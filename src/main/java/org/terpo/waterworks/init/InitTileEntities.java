package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;
import org.terpo.waterworks.tileentity.TileEntityGroundwaterPump;
import org.terpo.waterworks.tileentity.TileEntityRainCollector;
import org.terpo.waterworks.tileentity.TileEntityRainCollectorController;
import org.terpo.waterworks.tileentity.TileEntityRainTankWood;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class InitTileEntities {
	public static void register(IForgeRegistry<TileEntityType<?>> registry) {
		registry.register(TileEntityType.Builder.func_223042_a(TileEntityRainTankWood::new).build(null).setRegistryName(
				new ResourceLocation(WaterworksReference.MODID, WaterworksRegistryNames.TILE_ENTITY_RAIN_TANK_WOOD)));
		registry.register(TileEntityType.Builder.func_223042_a(TileEntityRainCollectorController::new).build(null)
				.setRegistryName(new ResourceLocation(WaterworksReference.MODID,
						WaterworksRegistryNames.TILE_ENTITY_RAIN_COLLECTOR_CONTROLLER)));
		registry.register(TileEntityType.Builder.func_223042_a(TileEntityRainCollector::new).build(null)
				.setRegistryName(new ResourceLocation(WaterworksReference.MODID,
						WaterworksRegistryNames.TILE_ENTITY_RAIN_COLLECTOR)));
		registry.register(TileEntityType.Builder.func_223042_a(TileEntityGroundwaterPump::new).build(null)
				.setRegistryName(new ResourceLocation(WaterworksReference.MODID,
						WaterworksRegistryNames.TILE_ENTITY_GROUNDWATER_PUMP)));
//		registry.register(TileEntityType.Builder.func_223042_a(BaseTileEntity::new).build(null).setRegistryName(
//				new ResourceLocation(WaterworksReference.MODID, WaterworksRegistryNames.TILE_ENTITY_BASE)));
		registry.register(TileEntityType.Builder.func_223042_a(TileWaterworks::new).build(null).setRegistryName(
				new ResourceLocation(WaterworksReference.MODID, WaterworksRegistryNames.TILE_ENTITY_WATERWORKS)));
	}

	private InitTileEntities() {
		// hide me
	}
}
