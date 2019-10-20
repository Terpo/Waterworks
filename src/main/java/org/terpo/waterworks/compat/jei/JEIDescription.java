package org.terpo.waterworks.compat.jei;

import java.util.HashMap;

import org.terpo.waterworks.init.WaterworksBlocks;
import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.init.WaterworksItems;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class JEIDescription {
	static IRecipeRegistration registry;

	public static void addJEIDescriptions(IRecipeRegistration modRegistry) {
		JEIDescription.registry = modRegistry;

		final HashMap<Object, String> descriptionMap = new HashMap<>();
		if (WaterworksConfig.pump.getGroundwaterPumpDescription()) {
			descriptionMap.put(WaterworksBlocks.groundwaterPump, "tile.groundwater_pump.description");
			descriptionMap.put(WaterworksBlocks.waterPipe, "tile.water_pipe.description");
		}
		if (WaterworksConfig.rainCollection.getRainCollectorDescription()) {
			descriptionMap.put(WaterworksBlocks.rainCollector, "tile.rain_collector.description");
			descriptionMap.put(WaterworksBlocks.rainCollectorController, "tile.rain_collector_controller.description");
		}
		if (WaterworksConfig.rainCollection.getWoodenRainTankDescription()) {
			descriptionMap.put(WaterworksBlocks.rainTankWood, "tile.rain_tank_wood.description");
		}

		if (WaterworksConfig.rockets.getFireworkRocketsDescription()) {
			descriptionMap.put(WaterworksItems.itemFireworkRain, "item.firework_rain.description");
			descriptionMap.put(WaterworksItems.itemFireworkAntiRain, "item.firework_anti_rain.description");
		}

		if (WaterworksConfig.rainCollection.getWrenchDescription()) {
			descriptionMap.put(WaterworksItems.itemPipeWrench, "item.pipe_wrench.description");
		}
		if (WaterworksConfig.rockets.getFireworkChargeDescription()) {
			descriptionMap.put(Items.FIREWORK_STAR, "item.firework_charge.description");
		}
		if (WaterworksConfig.rockets.getFireworksDescription()) {
			descriptionMap.put(Items.FIREWORK_ROCKET, "item.fireworks.description");
		}
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
		registry.addIngredientInfo(new ItemStack(item, 1), VanillaTypes.ITEM, descriptionKey);
	}

	private static void add(Block block, String descriptionKey) {
		registry.addIngredientInfo(new ItemStack(block, 1), VanillaTypes.ITEM, descriptionKey);
	}

	private JEIDescription() {
		// hide me
	}
}
