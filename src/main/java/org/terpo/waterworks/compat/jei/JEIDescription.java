package org.terpo.waterworks.compat.jei;

public class JEIDescription {
	// TODO restore JEI Compatibility
//	static IModRegistry registry;
//
//	public static void addJEIDescriptions(IModRegistry modRegistry) {
//		JEIDescription.registry = modRegistry;
//
//		final HashMap<Object, String> descriptionMap = new HashMap<>();
//		if (WaterworksConfig.pump.groundwaterPumpDescription) {
//			descriptionMap.put(WaterworksBlocks.groundwaterPump, "tile.groundwater_pump.description");
//			descriptionMap.put(WaterworksBlocks.waterPipe, "tile.water_pipe.description");
//		}
//		if (WaterworksConfig.rainCollection.rainCollectorDescription) {
//			descriptionMap.put(WaterworksBlocks.rainCollector, "tile.rain_collector.description");
//			descriptionMap.put(WaterworksBlocks.rainCollectorController,
//					"tile.rain_collector_controller.description");
//		}
//		if (WaterworksConfig.rainCollection.woodenRainTankDescription) {
//			descriptionMap.put(WaterworksBlocks.rainTankWood, "tile.rain_tank_wood.description");
//		}
//
//		if (WaterworksConfig.rockets.fireworkRocketsDescription) {
//			descriptionMap.put(WaterworksItems.firework_rain, "item.firework_rain.description");
//			descriptionMap.put(WaterworksItems.firework_anti_rain, "item.firework_anti_rain.description");
//		}
//
//		if (WaterworksConfig.rainCollection.wrenchDescription) {
//			descriptionMap.put(WaterworksItems.pipe_wrench, "item.pipe_wrench.description");
//		}
//		if (WaterworksConfig.rockets.fireworkChargeDescription) {
//			descriptionMap.put(Items.FIREWORK_CHARGE, "item.firework_charge.description");
//		}
//		if (WaterworksConfig.rockets.fireworksDescription) {
//			descriptionMap.put(Items.FIREWORKS, "item.fireworks.description");
//		}
//		descriptionMap.forEach((obj, descriptionKey) -> {
//			if (obj instanceof Block) {
//				JEIDescription.add((Block) obj, descriptionKey);
//			}
//			if (obj instanceof Item) {
//				JEIDescription.add((Item) obj, descriptionKey);
//			}
//		});
//	}
//
//	private static void add(Item item, String descriptionKey) {
//		registry.addIngredientInfo(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE), ItemStack.class,
//				descriptionKey);
//	}
//
//	private static void add(Block block, String descriptionKey) {
//		registry.addIngredientInfo(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE), ItemStack.class,
//				descriptionKey);
//	}
}
