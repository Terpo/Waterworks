package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;
import org.terpo.waterworks.item.ItemFireworkAntiRain;
import org.terpo.waterworks.item.ItemFireworkRain;
import org.terpo.waterworks.item.ItemMaterialController;
import org.terpo.waterworks.item.ItemMaterialEnergyAdapter;
import org.terpo.waterworks.item.ItemPipeWrench;
import org.terpo.waterworks.item.ItemWaterworksDebugger;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class InitItems {

	public static void init(IForgeRegistry<Item> registry) {
		registerItems(registry);
	}

	public static void registerItems(IForgeRegistry<Item> registry) {
		registerItem(registry, new ItemWaterworksDebugger(), WaterworksRegistryNames.ITEM_WATERWORKS_DEBUGGER);
		registerItem(registry, new ItemPipeWrench(), WaterworksRegistryNames.ITEM_PIPE_WRENCH);
		registerItem(registry, new ItemFireworkRain(), WaterworksRegistryNames.ITEM_FIREWORK_RAIN);
		registerItem(registry, new ItemFireworkAntiRain(), WaterworksRegistryNames.ITEM_FIREWORK_ANTI_RAIN);
		registerItem(registry, new ItemMaterialEnergyAdapter(), WaterworksRegistryNames.ITEM_MATERIAL_ENERGY_ADAPTER);
		registerItem(registry, new ItemMaterialController(), WaterworksRegistryNames.ITEM_MATERIAL_CONTROLLER);
	}

	private static Item registerItem(IForgeRegistry<Item> registry, Item item, String name) {
		item.setRegistryName(WaterworksReference.MODID, name);
		registry.register(item);
		return item;
	}

	private InitItems() {
		// hide me
	}
}
