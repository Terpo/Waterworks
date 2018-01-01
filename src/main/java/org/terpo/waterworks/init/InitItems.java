package org.terpo.waterworks.init;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;
import org.terpo.waterworks.item.ItemFireworkAntiRain;
import org.terpo.waterworks.item.ItemFireworkRain;
import org.terpo.waterworks.item.ItemMaterials;
import org.terpo.waterworks.item.ItemPipeWrench;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.registries.IForgeRegistry;

public class InitItems {

	public static void init(Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		registerItems(registry);
	}

	public static Item registerItem(IForgeRegistry<Item> registry, Item item, String name) {
		return registerItem(registry, item, name, Waterworks.CREATIVE_TAB);
	}

	public static void registerItems(IForgeRegistry<Item> registry) {
		WaterworksItems.pipe_wrench = registerItem(registry, new ItemPipeWrench(),
				WaterworksRegistryNames.ITEM_PIPE_WRENCH);
		if (WaterworksConfig.register.rainRocket) {
			WaterworksItems.firework_rain = registerItem(registry, new ItemFireworkRain(),
					WaterworksRegistryNames.ITEM_FIREWORK_RAIN);
		}
		if (WaterworksConfig.register.antiRainRocket) {
			WaterworksItems.firework_anti_rain = registerItem(registry, new ItemFireworkAntiRain(),
					WaterworksRegistryNames.ITEM_FIREWORK_ANTI_RAIN);
		}
		WaterworksItems.materials = registerItem(registry, new ItemMaterials(), WaterworksRegistryNames.ITEM_MATERIALS);
	}

	private static Item registerItem(IForgeRegistry<Item> registry, Item item, String name, CreativeTabs tab) {
		item.setRegistryName(WaterworksReference.MODID, name).setUnlocalizedName(name).setCreativeTab(tab);
		registry.register(item);
		return item;
	}
}
