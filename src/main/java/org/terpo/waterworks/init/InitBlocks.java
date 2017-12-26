package org.terpo.waterworks.init;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;
import org.terpo.waterworks.block.BlockGroundwaterPump;
import org.terpo.waterworks.block.BlockRainCollector;
import org.terpo.waterworks.block.BlockRainCollectorController;
import org.terpo.waterworks.block.BlockRainTankWood;
import org.terpo.waterworks.block.BlockWaterPipe;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.registries.IForgeRegistry;

public class InitBlocks {

	public static void initBlocks(Register<Block> event) {
		final IForgeRegistry<Block> registry = event.getRegistry();
		if (WaterworksConfig.REGISTER_RAIN_TANK) {
			WaterworksBlocks.rain_tank_wood = registerBlock(registry, new BlockRainTankWood(),
					WaterworksRegistryNames.BLOCK_RAIN_TANK_WOOD);
		}
		if (WaterworksConfig.REGISTER_WATER_PIPE) {
			WaterworksBlocks.water_pipe = registerBlock(registry, new BlockWaterPipe(),
					WaterworksRegistryNames.BLOCK_WATER_PIPE);
		}
		if (WaterworksConfig.REGISTER_RAIN_COLLECTING_MULTIBLOCK) {
			WaterworksBlocks.rain_collector = registerBlock(registry, new BlockRainCollector(),
					WaterworksRegistryNames.BLOCK_RAIN_COLLECTOR);
			WaterworksBlocks.rain_collector_controller = registerBlock(registry, new BlockRainCollectorController(),
					WaterworksRegistryNames.BLOCK_RAIN_COLLECTOR_CONTROLLER);
		}
		if (WaterworksConfig.REGISTER_GROUNDWATER_PUMP) {
			WaterworksBlocks.groundwater_pump = registerBlock(registry, new BlockGroundwaterPump(),
					WaterworksRegistryNames.BLOCK_GROUNDWATER_PUMP);
		}
	}

	public static Block registerBlock(IForgeRegistry<Block> registry, Block block, String name) {
		return registerBlock(registry, block, name, Waterworks.CREATIVE_TAB);
	}

	public static Block registerBlock(IForgeRegistry<Block> registry, Block block, String name, CreativeTabs tab) {
		block.setRegistryName(WaterworksReference.MODID, name).setUnlocalizedName(name).setCreativeTab(tab);
		registry.register(block);
		return block;
	}

	public static void initItemBlocks(Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		if (WaterworksConfig.REGISTER_RAIN_TANK) {
			registerItemBlock(registry, WaterworksBlocks.rain_tank_wood);
		}
		if (WaterworksConfig.REGISTER_WATER_PIPE) {
			registerItemBlock(registry, WaterworksBlocks.water_pipe);
		}
		if (WaterworksConfig.REGISTER_RAIN_COLLECTING_MULTIBLOCK) {
			registerItemBlock(registry, WaterworksBlocks.rain_collector);
			registerItemBlock(registry, WaterworksBlocks.rain_collector_controller);
		}
		if (WaterworksConfig.REGISTER_GROUNDWATER_PUMP) {
			registerItemBlock(registry, WaterworksBlocks.groundwater_pump);
		}
	}

	public static void registerItemBlock(IForgeRegistry<Item> registry, Block block) {
		registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}
}
