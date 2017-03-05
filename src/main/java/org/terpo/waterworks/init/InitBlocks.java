package org.terpo.waterworks.init;

import static org.terpo.waterworks.init.WaterworksBlocks.rain_tank_wood;
import static org.terpo.waterworks.init.WaterworksBlocks.water_pipe;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.api.constants.WaterworksReference;
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
		rain_tank_wood = registerBlock(new BlockRainTankWood(), "rain_tank_wood");
		water_pipe = registerBlock(new BlockWaterPipe(), "water_pipe");

	}
	public static void registerRenders() {
		registerRender(rain_tank_wood);
		registerRender(water_pipe);
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
