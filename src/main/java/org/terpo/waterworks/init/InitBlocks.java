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
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.registries.IForgeRegistry;

public class InitBlocks {

	public static void initBlocks(Register<Block> event) {
		final IForgeRegistry<Block> registry = event.getRegistry();
		if (WaterworksConfig.register.woodenRainTank) {
			WaterworksBlocks.rainTankWood = registerBlock(registry, new BlockRainTankWood(),
					WaterworksRegistryNames.BLOCK_RAIN_TANK_WOOD);
		}
		if (WaterworksConfig.register.waterPipe) {
			WaterworksBlocks.waterPipe = registerBlock(registry, new BlockWaterPipe(),
					WaterworksRegistryNames.BLOCK_WATER_PIPE);
		}
		if (WaterworksConfig.register.rainCollectorMultiblock) {
			WaterworksBlocks.rainCollector = registerBlock(registry, new BlockRainCollector(),
					WaterworksRegistryNames.BLOCK_RAIN_COLLECTOR);
			WaterworksBlocks.rainCollectorController = registerBlock(registry, new BlockRainCollectorController(),
					WaterworksRegistryNames.BLOCK_RAIN_COLLECTOR_CONTROLLER);
		}
		if (WaterworksConfig.register.groundwaterPump) {
			WaterworksBlocks.groundwaterPump = registerBlock(registry, new BlockGroundwaterPump(),
					WaterworksRegistryNames.BLOCK_GROUNDWATER_PUMP);
		}
	}

	public static Block registerBlock(IForgeRegistry<Block> registry, Block block, String name) {
		block.setRegistryName(WaterworksReference.MODID, name);
		registry.register(block);
		return block;
	}

	public static void initItemBlocks(Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		if (WaterworksConfig.register.woodenRainTank) {
			registerItemBlock(registry, WaterworksBlocks.rainTankWood);
		}
		if (WaterworksConfig.register.waterPipe) {
			registerItemBlock(registry, WaterworksBlocks.waterPipe);
		}
		if (WaterworksConfig.register.rainCollectorMultiblock) {
			registerItemBlock(registry, WaterworksBlocks.rainCollector);
			registerItemBlock(registry, WaterworksBlocks.rainCollectorController);
		}
		if (WaterworksConfig.register.groundwaterPump) {
			registerItemBlock(registry, WaterworksBlocks.groundwaterPump);
		}
	}

	public static void registerItemBlock(IForgeRegistry<Item> registry, Block block) {
		registry.register(new BlockItem(block, new Item.Properties().group(Waterworks.CREATIVE_TAB))
				.setRegistryName(block.getRegistryName()));
	}

	private InitBlocks() {
		// hide me
	}
}
