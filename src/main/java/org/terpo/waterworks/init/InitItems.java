package org.terpo.waterworks.init;

import static org.terpo.waterworks.init.WaterworksItems.firework_anti_rain;
import static org.terpo.waterworks.init.WaterworksItems.firework_rain;
import static org.terpo.waterworks.init.WaterworksItems.materials;
import static org.terpo.waterworks.init.WaterworksItems.pipe_wrench;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.api.constants.EnumItemMaterials;
import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.item.ItemFireworkAntiRain;
import org.terpo.waterworks.item.ItemFireworkRain;
import org.terpo.waterworks.item.ItemMaterials;
import org.terpo.waterworks.item.ItemPipeWrench;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class InitItems {

	public static void init() {
		registerItems();
	}

	public static void registerItems() {
		pipe_wrench = registerItem(new ItemPipeWrench(), "pipe_wrench");
		if (WaterworksConfig.REGISTER_RAIN_ROCKET) {
			firework_rain = registerItem(new ItemFireworkRain(), "firework_rain");
		}
		if (WaterworksConfig.REGISTER_ANTI_RAIN_ROCKET) {
			firework_anti_rain = registerItem(new ItemFireworkAntiRain(), "firework_anti_rain");
		}
		materials = registerItem(new ItemMaterials(), "materials");
	}

	public static void registerRenders() {
		registerRender(pipe_wrench);
		if (WaterworksConfig.REGISTER_RAIN_ROCKET) {
			registerRender(firework_rain);
		}
		if (WaterworksConfig.REGISTER_ANTI_RAIN_ROCKET) {
			registerRender(firework_anti_rain);
		}
		registerRenderMaterials(materials);
	}

	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0,
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	public static void registerRenderMaterials(Item item) {
		for (int i = 0; i < EnumItemMaterials.VALUES.length; i++) {
			final ResourceLocation loc = item.getRegistryName();
			ModelLoader.setCustomModelResourceLocation(item, i,
					new ModelResourceLocation(loc + "_" + EnumItemMaterials.VALUES[i], "inventory"));
		}
	}

	public static Item registerItem(Item item, String name) {
		return registerItem(item, name, Waterworks.CREATIVE_TAB);

	}

	private static Item registerItem(Item item, String name, CreativeTabs tab) {
		item.setUnlocalizedName(name);
		if (tab != null) {
			item.setCreativeTab(tab);
		}
		GameRegistry.register(item, new ResourceLocation(WaterworksReference.MODID, name));
		return item;
	}

}
