package org.terpo.waterworks.init;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.block.BlockGroundwaterPump;
import org.terpo.waterworks.block.BlockRainCollector;
import org.terpo.waterworks.block.BlockRainCollectorController;
import org.terpo.waterworks.block.BlockRainTankWood;
import org.terpo.waterworks.block.BlockWaterPipe;
import org.terpo.waterworks.tileentity.TileEntityRainTankWood;
import org.terpo.waterworks.tileentity.specialrenderer.TileEntityWaterRenderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class InitBlocks {

	public static void init() {
		if (WaterworksConfig.REGISTER_RAIN_TANK) {
			WaterworksBlocks.rain_tank_wood = registerBlock(new BlockRainTankWood(), "rain_tank_wood");
		}
		if (WaterworksConfig.REGISTER_WATER_PIPE) {
			WaterworksBlocks.water_pipe = registerBlock(new BlockWaterPipe(), "water_pipe");
		}
		if (WaterworksConfig.REGISTER_RAIN_COLLECTING_MULTIBLOCK) {
			WaterworksBlocks.rain_collector = registerBlock(new BlockRainCollector(), "rain_collector");
			WaterworksBlocks.rain_collector_controller = registerBlock(new BlockRainCollectorController(),
					"rain_collector_controller");
		}
		if (WaterworksConfig.REGISTER_GROUNDWATER_PUMP) {
			WaterworksBlocks.groundwater_pump = registerBlock(new BlockGroundwaterPump(), "groundwater_pump");
		}

	}
	public static void registerRenders() {
		if (WaterworksConfig.REGISTER_RAIN_TANK) {
			registerRender(WaterworksBlocks.rain_tank_wood);
			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRainTankWood.class, new TileEntityWaterRenderer());

		}
		if (WaterworksConfig.REGISTER_WATER_PIPE) {
			registerRender(WaterworksBlocks.water_pipe);
		}
		if (WaterworksConfig.REGISTER_RAIN_COLLECTING_MULTIBLOCK) {
			registerRender(WaterworksBlocks.rain_collector);
			registerRender(WaterworksBlocks.rain_collector_controller);
		}
		if (WaterworksConfig.REGISTER_GROUNDWATER_PUMP) {
			registerRender(WaterworksBlocks.groundwater_pump);
		}
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
