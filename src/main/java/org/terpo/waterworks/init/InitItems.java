package org.terpo.waterworks.init;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;
import org.terpo.waterworks.item.ItemFireworkAntiRain;
import org.terpo.waterworks.item.ItemFireworkRain;
import org.terpo.waterworks.item.ItemMaterialController;
import org.terpo.waterworks.item.ItemMaterialEnergyAdapter;
import org.terpo.waterworks.item.ItemPipeWrench;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.registries.IForgeRegistry;

public class InitItems {

	public static void init(IForgeRegistry<Item> registry) {
		registerItems(registry);
	}

	public static Item registerItem(IForgeRegistry<Item> registry, Item item, String name) {
		return registerItem(registry, item, name, Waterworks.CREATIVE_TAB);
	}

	public static void registerItems(IForgeRegistry<Item> registry) {
		WaterworksItems.itemPipeWrench = registerItem(registry, new ItemPipeWrench(),
				WaterworksRegistryNames.ITEM_PIPE_WRENCH);
		if (WaterworksConfig.register.rainRocket) {
			WaterworksItems.itemFireworkRain = registerItem(registry, new ItemFireworkRain(),
					WaterworksRegistryNames.ITEM_FIREWORK_RAIN);
		}
		if (WaterworksConfig.register.antiRainRocket) {
			WaterworksItems.itemFireworkAntiRain = registerItem(registry, new ItemFireworkAntiRain(),
					WaterworksRegistryNames.ITEM_FIREWORK_ANTI_RAIN);
		}
		WaterworksItems.itemMaterialEnergyAdapter = registerItem(registry, new ItemMaterialEnergyAdapter(),
				WaterworksRegistryNames.ITEM_MATERIAL_ENERGY_ADAPTER);
		WaterworksItems.itemMaterialController = registerItem(registry, new ItemMaterialController(),
				WaterworksRegistryNames.ITEM_MATERIAL_CONTROLLER);
	}

	private static Item registerItem(IForgeRegistry<Item> registry, Item item, String name, ItemGroup tab) {
		item.setRegistryName(WaterworksReference.MODID, name);
		registry.register(item);
		return item;
	}

	private InitItems() {
		// hide me
	}
}
