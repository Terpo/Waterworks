package org.terpo.waterworks.init;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.block.BlockRainCollector;
import org.terpo.waterworks.block.BlockRainCollectorController;
import org.terpo.waterworks.block.BlockRainTankWood;
import org.terpo.waterworks.block.BlockWaterPipe;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class InitBlocks {

	public static void init() {
		WaterworksBlocks.rain_tank_wood = registerBlock(new BlockRainTankWood(), "rain_tank_wood");
		WaterworksBlocks.water_pipe = registerBlock(new BlockWaterPipe(), "water_pipe");
		WaterworksBlocks.rain_collector = registerBlock(new BlockRainCollector(), "rain_collector");
		WaterworksBlocks.rain_collector_controller = registerBlock(new BlockRainCollectorController(),
				"rain_collector_controller");

	}
	public static void registerRenders() {
		registerRender(WaterworksBlocks.rain_tank_wood);
		registerRender(WaterworksBlocks.water_pipe);
		registerRender(WaterworksBlocks.rain_collector);
		registerRender(WaterworksBlocks.rain_collector_controller);
	}
	public static void registerRender(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
				new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}

	public static Block registerBlock(Block block, String name) {
		return registerBlock(block, name, Waterworks.CREATIVE_TAB);
	}

	public static Block registerBlock(Block block, String name, CreativeTabs tab) {
		block.setRegistryName(WaterworksReference.MODID, name);
		GameRegistry.register(block);
		final ItemBlock itemBLock = new ItemBlock(block);
		itemBLock.setRegistryName(name);
		GameRegistry.register(itemBLock);

		block.setUnlocalizedName(name);
		block.setCreativeTab(tab);
		return block;

	}
}
