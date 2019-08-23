package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;

@Config(modid = WaterworksReference.MODID)
@Config.LangKey("waterworks.config.title")
public class WaterworksConfig {

	public static final RainCollection rainCollection = new RainCollection();
	public static final GroundwaterPump pump = new GroundwaterPump();
	public static final Rockets rockets = new Rockets();
	public static final WaterworksRegister register = new WaterworksRegister();
	public static final WaterworksRecipes recipes = new WaterworksRecipes();

	// TODO with next major release: move JEI stuff

	public static class RainCollection {
		/**
		 * CONFIG RAIN COLLECTION
		 */
		// Simple Rain Tank
		@Config.Comment("The fillrate in mB/second for the Wooden Rain Tank.")
		@Config.RangeInt(min = 1, max = 8000)
		public int woodenRainTankFillrate = 10;

		@Config.Comment("The capacity in mB for the Wooden Rain Tank.")
		@Config.RangeInt(min = 1000, max = 1024000)
		public int woodenRainTankCapacity = 8000;

		// Multiblock Rain Collector
		@Config.Comment("Amount of water per second per connected block.")
		@Config.RangeInt(min = 1, max = 8000)
		public int rainCollectorFillrate = 20;

		@Config.Comment("The capacity in mB for the Rain Collector Multiblock.")
		@Config.RangeInt(min = 8000, max = 1024000)
		public int rainCollectorCapacity = 32000;

		@Config.Comment("Search radius of the Rain Collector Controller")
		@Config.RangeInt(min = 0, max = 7)
		public int rainCollectorRange = 2;

		@Config.Comment("Turn this to false to disable JEI description for the Wooden Rain Tank.")
		public boolean woodenRainTankDescription = true;

		@Config.Comment("Turn this to false to disable JEI description for the Rain Collector Multiblock.")
		public boolean rainCollectorDescription = true;

		@Config.Comment("Turn this to false to disable JEI description for the Wrench.")
		public boolean wrenchDescription = true;
	}

	public static class GroundwaterPump {
		/**
		 * CONFIG GROUNDWATER PUMP
		 */
		@Config.Comment("The fillrate in mB/second for the Groundwater Pump.")
		@Config.RangeInt(min = 1, max = 8000)
		public int groundwaterPumpFillrate = 500;

		@Config.Comment("The capacity in mB for the Groundwater Pump.")
		@Config.RangeInt(min = 8000, max = 1024000)
		public int groundwaterPumpCapacity = 32000;

		@Config.Comment("Pump energy base usage in forge energy units. Needed for each pump operation.")
		@Config.RangeInt(min = 20, max = 1024000)
		public int groundwaterPumpEnergyBaseUsage = 1600;

		@Config.Comment("Additional to base usage. Each used pipe will multiplied with this value.")
		@Config.RangeInt(min = 0, max = 1024000)
		public int groundwaterPumpEnergyPipeMultiplier = 20;

		@Config.Comment("Pump energy capacity in forge energy units.")
		@Config.RangeInt(min = 8000, max = 1024000)
		public int groundwaterPumpEnergyCapacity = 16000;

		@Config.Comment("Pump energy input rate in forge energy units.")
		@Config.RangeInt(min = 20, max = 1024000)
		public int groundwaterPumpEnergyInput = 500;

		@Config.Comment("Energy used to place a pipe.")
		@Config.RangeInt(min = 0, max = 1024000)
		public int groundwaterPumpEnergyPipePlacement = 2500;

		@Config.Comment("Should the Groundwater Pump spawn a slab to close the hole?")
		public boolean groundwaterPumpSafety = true;

		@Config.Comment("Turn this to false if your world does not generate Bedrock. (Skyblock)")
		public boolean groundwaterPumpCheckBedrock = true;

		@Config.Comment("Turn this to false to disable JEI description for the Groundwater Pump.")
		public boolean groundwaterPumpDescription = true;
	}

	public static class Rockets {
		/**
		 * CONFIG ROCKETS
		 */
		// Rain Rocket
		@Config.Comment("Rain duration with x1 multiplier.")
		@Config.RangeInt(min = 1, max = 12000)
		public int rainDuration = 3000;

		@Config.Comment("Maximum rain multiplier.")
		@Config.RangeInt(min = 1, max = 24)
		public int rainMaxMultiplier = 8;

		// Anti Rain Rocket
		@Config.Comment("Clear sky duration with x1 multiplier.")
		@Config.RangeInt(min = 100, max = 6000)
		public int clearSkyDuration = 4000;

		@Config.Comment("Maximum clear sky multiplier.")
		@Config.RangeInt(min = 100, max = 6000)
		public int clearSkyMaxMultiplier = 12;

		@Config.Comment("Maximum days of clear sky that will added to the calculated time.")
		@Config.RangeInt(min = 0, max = 7)
		public int clearSkyMaxRandomAdditionalDays = 3;

		@Config.Comment("Turn this to false to disable JEI description for the Rockets.")
		public boolean fireworkRocketsDescription = true;

		// Vanilla Firework
		@Config.Comment("JEI: Adds a small description for firework star.")
		public boolean fireworkChargeDescription = true;
		@Config.Comment("JEI: Adds a small description for fireworks.")
		public boolean fireworksDescription = true;
	}

	public static class WaterworksRegister {
		/**
		 * DISABLE REGISTER
		 */
		// ITEMS
		@Config.Comment("If true, the Rain Rocket is registered")
		public boolean rainRocket = true;
		@Config.Comment("If true, the Anti Rain Rocket is registered")
		public boolean antiRainRocket = true;

		// BLOCKS
		@Config.Comment("If true, the Groundwater Pump is registered")
		public boolean groundwaterPump = true;
		@Config.Comment("If true, the Rain Collector and Rain Collector Controller are registered")
		public boolean rainCollectorMultiblock = true;
		@Config.Comment("If true, the Water Pipe is registered")
		public boolean waterPipe = true;
		@Config.Comment("If true, the Wooden Rain Tank is registered")
		public boolean woodenRainTank = true;
	}

	public static class WaterworksRecipes {
		/**
		 * DISABLE RECIPE
		 */
		// ITEMS
		@Config.Comment("If true, the Rain Rocket has a recipe")
		public boolean recipeRainRocket = true;

		@Config.Comment("If true, the Anti Rain Rocket has a recipe")
		public boolean recipeAntiRainRocket = true;

	}

	@Mod.EventBusSubscriber(modid = WaterworksReference.MODID)
	private static class EventHandler {

		/**
		 * Inject the new values and save to the config file when the config has been
		 * changed from the GUI.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(WaterworksReference.MODID)) {
				ConfigManager.sync(WaterworksReference.MODID, Config.Type.INSTANCE);
			}
		}
	}
}
