package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WaterworksItems {
	@GameRegistry.ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.ITEM_PIPE_WRENCH)
	public static Item pipe_wrench;
	@GameRegistry.ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.ITEM_FIREWORK_RAIN)
	public static Item firework_rain;
	@GameRegistry.ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.ITEM_FIREWORK_ANTI_RAIN)
	public static Item firework_anti_rain;
	@GameRegistry.ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.ITEM_MATERIALS)
	public static Item materials;
}
