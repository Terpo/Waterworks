package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;

import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;

public class WaterworksItems {
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.ITEM_WATERWORKS_DEBUGGER)
	public static final Item itemWaterworksDebugger = null;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.ITEM_PIPE_WRENCH)
	public static final Item itemPipeWrench = null;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.ITEM_FIREWORK_RAIN)
	public static final Item itemFireworkRain = null;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.ITEM_FIREWORK_ANTI_RAIN)
	public static final Item itemFireworkAntiRain = null;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.ITEM_MATERIAL_ENERGY_ADAPTER)
	public static final Item itemMaterialEnergyAdapter = null;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.ITEM_MATERIAL_CONTROLLER)
	public static final Item itemMaterialController = null;

	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.BLOCK_WATER_PIPE)
	public static final Item blockItemWaterPipe = null;

	private WaterworksItems() {
		// hide me
	}
}
