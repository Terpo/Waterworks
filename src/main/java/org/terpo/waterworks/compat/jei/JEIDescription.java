package org.terpo.waterworks.compat.jei;

import java.util.HashMap;

import org.terpo.waterworks.Config;
import org.terpo.waterworks.setup.Registration;

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
		if (Config.pump.getGroundwaterPumpDescription()) {
			descriptionMap.put(Registration.groundwaterPumpBlock, "tile.groundwater_pump.description");
			descriptionMap.put(Registration.waterPipeBlock, "tile.water_pipe.description");
		}
		if (Config.rainCollection.getRainCollectorDescription()) {
			descriptionMap.put(Registration.rainCollectorBlock, "tile.rain_collector.description");
			descriptionMap.put(Registration.rainCollectorControllerBlock, "tile.rain_collector_controller.description");
		}
		if (Config.rainCollection.getWoodenRainTankDescription()) {
			descriptionMap.put(Registration.rainTankWoodBlock, "tile.rain_tank_wood.description");
		}

		if (Config.rockets.getFireworkRocketsDescription()) {
			descriptionMap.put(Registration.itemFireworkRain, "item.firework_rain.description");
			descriptionMap.put(Registration.itemFireworkAntiRain, "item.firework_anti_rain.description");
		}

		if (Config.rainCollection.getWrenchDescription()) {
			descriptionMap.put(Registration.itemPipeWrench, "item.pipe_wrench.description");
		}
		if (Config.rockets.getFireworkChargeDescription()) {
			descriptionMap.put(Items.FIREWORK_STAR, "item.firework_charge.description");
		}
		if (Config.rockets.getFireworksDescription()) {
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
