package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.tileentity.TileEntityRainTankWood;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class InitTileEntities {
	public static void init() {
		registerTileEntity(TileEntityRainTankWood.class, "TileEntityRainTankWood");
	}

	private static void registerTileEntity(Class<? extends TileEntity> clazz, String name) {
		GameRegistry.registerTileEntity(clazz, WaterworksReference.MODID + name);
	}
}
