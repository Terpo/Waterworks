package org.terpo.waterworks.init;

import static org.terpo.waterworks.init.WaterworksItems.first_item;
import static org.terpo.waterworks.init.WaterworksItems.iron_mesh;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.WaterworksReference;
import org.terpo.waterworks.item.ItemFirstItem;
import org.terpo.waterworks.item.ItemIronMesh;

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
		first_item = registerItem(new ItemFirstItem(), "first_item");
		iron_mesh = registerItem(new ItemIronMesh(), "iron_mesh");
	}

	public static void registerRenders() {
		registerRender(first_item);
		registerRender(iron_mesh);
	}

	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0,
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
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
