package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;

import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;

public class WaterworksItems {
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.ITEM_PIPE_WRENCH)
	public static Item itemPipeWrench;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.ITEM_FIREWORK_RAIN)
	public static Item itemFireworkRain;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.ITEM_FIREWORK_ANTI_RAIN)
	public static Item itemFireworkAntiRain;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.ITEM_MATERIALS)
	public static Item itemMaterials;

	private WaterworksItems() {
		// hide me
	}
}
