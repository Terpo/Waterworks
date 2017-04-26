package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.tileentity.BaseTileEntity;
import org.terpo.waterworks.tileentity.TileEntityGroundwaterPump;
import org.terpo.waterworks.tileentity.TileEntityRainCollector;
import org.terpo.waterworks.tileentity.TileEntityRainCollectorController;
import org.terpo.waterworks.tileentity.TileEntityRainTankWood;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class InitTileEntities {
	public static void init() {
		registerTileEntity(TileEntityRainTankWood.class, "TileEntityRainTankWood");
		registerTileEntity(TileEntityRainCollectorController.class, "TileEntityRainCollectorController");
		registerTileEntity(TileEntityRainCollector.class, "TileEntityRainCollector");
		registerTileEntity(TileEntityGroundwaterPump.class, "TileEntityGroundwaterPump");
		registerTileEntity(BaseTileEntity.class, "BaseTileEntity");
		registerTileEntity(TileWaterworks.class, "TileWaterworks");
	}

	private static void registerTileEntity(Class<? extends TileEntity> clazz, String name) {
		GameRegistry.registerTileEntity(clazz, WaterworksReference.MODID + name);
	}
}
