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
		WaterworksBlocks.rainTankWood = registerBlock(registry, new BlockRainTankWood(),
				WaterworksRegistryNames.BLOCK_RAIN_TANK_WOOD);
		WaterworksBlocks.waterPipe = registerBlock(registry, new BlockWaterPipe(),
				WaterworksRegistryNames.BLOCK_WATER_PIPE);
		WaterworksBlocks.rainCollector = registerBlock(registry, new BlockRainCollector(),
				WaterworksRegistryNames.BLOCK_RAIN_COLLECTOR);
		WaterworksBlocks.rainCollectorController = registerBlock(registry, new BlockRainCollectorController(),
				WaterworksRegistryNames.BLOCK_RAIN_COLLECTOR_CONTROLLER);
		WaterworksBlocks.groundwaterPump = registerBlock(registry, new BlockGroundwaterPump(),
				WaterworksRegistryNames.BLOCK_GROUNDWATER_PUMP);

	}

	public static Block registerBlock(IForgeRegistry<Block> registry, Block block, String name) {
		block.setRegistryName(WaterworksReference.MODID, name);
		registry.register(block);
		return block;
	}

	public static void initItemBlocks(IForgeRegistry<Item> registry) {
		registerItemBlock(registry, WaterworksBlocks.rainTankWood);
		WaterworksBlocks.blockItemWaterPipe = registerItemBlock(registry, WaterworksBlocks.waterPipe);
		registerItemBlock(registry, WaterworksBlocks.rainCollector);
		registerItemBlock(registry, WaterworksBlocks.rainCollectorController);
		registerItemBlock(registry, WaterworksBlocks.groundwaterPump);
	}

	public static Item registerItemBlock(IForgeRegistry<Item> registry, Block block) {
		final Item item = new BlockItem(block, new Item.Properties().group(Waterworks.CREATIVE_TAB))
				.setRegistryName(block.getRegistryName());
		registry.register(item);
		return item;
	}

	private InitBlocks() {
		// hide me
	}
}
