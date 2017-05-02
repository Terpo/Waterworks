package org.terpo.waterworks.init;

import org.terpo.waterworks.Waterworks;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class WaterworksConfig {
	public static Configuration cfg;

	// Categories
	private static final String CATEGORY_WATER_COLLECTING = "Water Collecting Mechanics";
	private static final String CATEGORY_ROCKETS = "Rain Rockets";

	// Simple Rain Tank
	public static int RAIN_TANK_SIMPLE_FILLRATE = 100;
	public static int RAIN_TANK_SIMPLE_CAPACITY = 8000;
	// Multiblock Rain Collector
	public static int RAIN_COLLECTOR_FILLRATE = 100;
	public static int RAIN_COLLECTOR_CAPACITY = 32000;
	public static int RAIN_COLLECTOR_RANGE = 2;

	// Rain Rocket
	public static int RAIN_DURATION = 240;
	public static int RAIN_DURATION_MULTIPLIER_MAX = 8;

	// Anti Rain Rocket
	public static int ANTI_RAIN_DURATION = 2000;
	public static int ANTI_RAIN_DURATION_MULTIPLIER_MAX = 12;

	// Vanilla Firework
	public static boolean FIREWORK_CHARGE_DESCRIPTION = true;
	public static boolean FIREWORKS_DESCRIPTION = true;

	// Groundwater Pump
	public static int GROUNDWATER_PUMP_CAPACITY = 32000;
	public static int GROUNDWATER_PUMP_FILLRATE = 1000;
	public static int GROUNDWATER_PUMP_ENERGY_BASEUSAGE = 160;
	public static int GROUNDWATER_PUMP_ENERGY_PIPEMULTIPLIER = 4;
	public static int GROUNDWATER_PUMP_ENERGY_CAPACITY = 16000;
	public static int GROUNDWATER_PUMP_ENERGY_MAXINPUT = 500;

	public static boolean GROUNDWATER_PUMP_SAFETY = true;

	private static void initWaterCollectingConfig() {
		cfg.addCustomCategoryComment(CATEGORY_WATER_COLLECTING, "Simple Rain Tank");
		RAIN_TANK_SIMPLE_FILLRATE = cfg.getInt("Simple Rain Tank Fillrate", CATEGORY_WATER_COLLECTING,
				RAIN_TANK_SIMPLE_FILLRATE, 1, 8000, "Amount of water per second");
		RAIN_TANK_SIMPLE_CAPACITY = cfg.getInt("Simple Rain Tank Capacity", CATEGORY_WATER_COLLECTING,
				RAIN_TANK_SIMPLE_CAPACITY, 1000, 1024000, "Tank capacity in mB");
		cfg.addCustomCategoryComment(CATEGORY_WATER_COLLECTING, "Multiblock Rain Collector Configuration");
		RAIN_COLLECTOR_FILLRATE = cfg.getInt("Multiblock Rain Collector Fillrate", CATEGORY_WATER_COLLECTING,
				RAIN_COLLECTOR_FILLRATE, 1, 8000, "Amount of water per second per connected block");
		RAIN_COLLECTOR_CAPACITY = cfg.getInt("Multiblock Rain Collector Capacity", CATEGORY_WATER_COLLECTING,
				RAIN_COLLECTOR_CAPACITY, 8000, 1024000, "Tank capacity in mB");
		RAIN_COLLECTOR_RANGE = cfg.getInt("Multiblock Rain Collector Radius", CATEGORY_WATER_COLLECTING,
				RAIN_COLLECTOR_RANGE, 0, 7, "Radius of the Controller block");
		cfg.addCustomCategoryComment(CATEGORY_WATER_COLLECTING, "Groundwater Pump Configuration");
		// FLUID
		GROUNDWATER_PUMP_FILLRATE = cfg.getInt("Groundwater Pump Fillrate", CATEGORY_WATER_COLLECTING,
				GROUNDWATER_PUMP_FILLRATE, 1, 8000, "Amount of water per second");
		GROUNDWATER_PUMP_CAPACITY = cfg.getInt("Groundwater Pump Water Capacity", CATEGORY_WATER_COLLECTING,
				GROUNDWATER_PUMP_CAPACITY, 8000, 1024000, "Pump water capacity in mB");
		// Energy
		GROUNDWATER_PUMP_ENERGY_CAPACITY = cfg.getInt("Groundwater Pump Energy Capacity", CATEGORY_WATER_COLLECTING,
				GROUNDWATER_PUMP_ENERGY_CAPACITY, 8000, 1024000, "Pump energy capacity in forge energy units");
		GROUNDWATER_PUMP_ENERGY_MAXINPUT = cfg.getInt("Groundwater Pump Energy Input Rate", CATEGORY_WATER_COLLECTING,
				GROUNDWATER_PUMP_ENERGY_MAXINPUT, 20, 1024000, "Pump energy input rate in forge energy units");
		GROUNDWATER_PUMP_ENERGY_BASEUSAGE = cfg.getInt("Groundwater Pump Energy Base Usage", CATEGORY_WATER_COLLECTING,
				GROUNDWATER_PUMP_ENERGY_BASEUSAGE, 20, 1024000,
				"Pump energy base usage in forge energy units. Needed For each pump operation.");
		GROUNDWATER_PUMP_ENERGY_PIPEMULTIPLIER = cfg.getInt("Groundwater Pump Energy Pipemultiplier",
				CATEGORY_WATER_COLLECTING, GROUNDWATER_PUMP_ENERGY_PIPEMULTIPLIER, 0, 1024000,
				"Additional to base usage. Each used pipe will multiplied with this value.");
		// Misc
		GROUNDWATER_PUMP_SAFETY = cfg.getBoolean("Groundwater Pump Safety Block", CATEGORY_WATER_COLLECTING,
				GROUNDWATER_PUMP_SAFETY, "Should the Groundwater Pump spawn a slab to close the hole?");
	}

	private static void initRainRockets() {
		cfg.addCustomCategoryComment(CATEGORY_ROCKETS, "Rain Rocket Configuration");
		RAIN_DURATION = cfg.getInt("Rain Duration", CATEGORY_ROCKETS, RAIN_DURATION, 1, 3000,
				"Rain duration with x1 multiplier");
		RAIN_DURATION_MULTIPLIER_MAX = cfg.getInt("Maximum Rain Duration Multiplier", CATEGORY_ROCKETS,
				RAIN_DURATION_MULTIPLIER_MAX, 1, 24, "Rain duration multiplier");

		ANTI_RAIN_DURATION = cfg.getInt("Clear Sky Duration", CATEGORY_ROCKETS, ANTI_RAIN_DURATION, 100, 6000,
				"Clear sky duration with x1 multiplier");
		ANTI_RAIN_DURATION_MULTIPLIER_MAX = cfg.getInt("Maximum Clear Sky Duration Multiplier", CATEGORY_ROCKETS,
				ANTI_RAIN_DURATION_MULTIPLIER_MAX, 1, 24, "Clear sky duration multiplier");

		// VANILLA
		FIREWORK_CHARGE_DESCRIPTION = cfg.getBoolean("Add Minecraft Firework Star Description", CATEGORY_ROCKETS,
				FIREWORK_CHARGE_DESCRIPTION, "Adds a small description for firework star");
		FIREWORKS_DESCRIPTION = cfg.getBoolean("Add Minecraft Fireworks Description", CATEGORY_ROCKETS,
				FIREWORKS_DESCRIPTION, "Adds a small description for fireworks");
	}

	public static void init(FMLPreInitializationEvent event) {
		cfg = new Configuration(event.getSuggestedConfigurationFile());
	}
	public static void load() {
		try {
			cfg.load();
			initWaterCollectingConfig();
			initRainRockets();
		} catch (final Exception ex) {
			Waterworks.LOGGER.error("Problem loading config file!", ex);
		} finally {
			WaterworksConfig.save();
		}
	}

	public static void save() {
		if (cfg.hasChanged()) {
			cfg.save();
		}
	}
}
