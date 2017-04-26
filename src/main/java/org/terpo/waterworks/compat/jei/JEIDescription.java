package org.terpo.waterworks.compat.jei;

import java.util.HashMap;

import org.terpo.waterworks.init.WaterworksBlocks;
import org.terpo.waterworks.init.WaterworksItems;

import mezz.jei.api.IModRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class JEIDescription {
	static IModRegistry registry;

	public static void addJEIDescriptions(IModRegistry modRegistry) {
		JEIDescription.registry = modRegistry;

		final HashMap<Object, String> descriptionMap = new HashMap<>();
		descriptionMap.put(WaterworksBlocks.groundwater_pump, "tile.groundwater_pump.description");
		descriptionMap.put(WaterworksBlocks.rain_collector, "tile.rain_collector.description");
		descriptionMap.put(WaterworksBlocks.rain_collector_controller, "tile.rain_collector_controller.description");
		descriptionMap.put(WaterworksBlocks.rain_tank_wood, "tile.rain_tank_wood.description");
		descriptionMap.put(WaterworksBlocks.water_pipe, "tile.water_pipe.description");
		descriptionMap.put(WaterworksItems.firework_rain, "item.firework_rain.description");
		descriptionMap.put(WaterworksItems.firework_anti_rain, "item.firework_anti_rain.description");

		descriptionMap.forEach((obj, descriptionKey) -> {
			if (obj instanceof Block) {
				JEIDescription.add((Block) obj, descriptionKey);
			}
			if (obj instanceof Item) {
				JEIDescription.add((Item) obj, descriptionKey);
			}
		});
	}

	private static void add(Item item, String descriptionKey) {
		registry.addDescription(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE), descriptionKey);
	}

	private static void add(Block block, String descriptionKey) {
		registry.addDescription(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE), descriptionKey);
	}
}
