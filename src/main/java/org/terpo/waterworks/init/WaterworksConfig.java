package org.terpo.waterworks.init;

import org.terpo.waterworks.Waterworks;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class WaterworksConfig {
	public static Configuration cfg;

	// Categories
	private static final String CATEGORY_DISABLE_RECIPE = "disable recipes";
	private static final String CATEGORY_DISABLE_REGISTER = "disable register";

	private static final String CATEGORY_RAIN_COLLECTING = "rain collecting configuration";
	private static final String CATEGORY_GROUND_WATER = "groundwater pump configuration";
	private static final String CATEGORY_ROCKETS = "rain rockets";

	/**
	 * DISABLE REGISTER
	 */
	// ITEMS
	public static boolean REGISTER_RAIN_ROCKET = true;
	public static boolean REGISTER_ANTI_RAIN_ROCKET = true;

	// BLOCKS
	public static boolean REGISTER_GROUNDWATER_PUMP = true;
	public static boolean REGISTER_RAIN_COLLECTING_MULTIBLOCK = true;
	public static boolean REGISTER_WATER_PIPE = true;
	public static boolean REGISTER_RAIN_TANK = true;
	/**
	 * DISABLE RECIPE
	 */
	// ITEMS
	public static boolean RECIPE_RAIN_ROCKET = true;
	public static boolean RECIPE_ANTI_RAIN_ROCKET = true;

	// BLOCKS
	public static boolean RECIPE_GROUNDWATER_PUMP = true;
	public static boolean RECIPE_RAIN_COLLECTING_MULTIBLOCK = true;
	public static boolean RECIPE_WATER_PIPE = true;
	public static boolean RECIPE_RAIN_TANK = true;
	/**
	 * CONFIG ROCKETS
	 */
	// Rain Rocket
	public static int RAIN_DURATION = 240;
	public static int RAIN_DURATION_MULTIPLIER_MAX = 8;

	// Anti Rain Rocket
	public static int ANTI_RAIN_DURATION = 4000;
	public static int ANTI_RAIN_DURATION_MULTIPLIER_MAX = 12;
	public static int ANTI_RAIN_MAX_RANDOM_ADDITIONAL_DAYS = 3;

	// Vanilla Firework
	public static boolean FIREWORK_CHARGE_DESCRIPTION = true;
	public static boolean FIREWORKS_DESCRIPTION = true;

	/**
	 * CONFIG RAIN COLLECTION
	 */
	// Simple Rain Tank
	public static int RAIN_TANK_SIMPLE_FILLRATE = 100;
	public static int RAIN_TANK_SIMPLE_CAPACITY = 8000;
	// Multiblock Rain Collector
	public static int RAIN_COLLECTOR_FILLRATE = 100;
	public static int RAIN_COLLECTOR_CAPACITY = 32000;
	public static int RAIN_COLLECTOR_RANGE = 2;

	/**
	 * CONFIG GROUNDWATER PUMP
	 */
	public static int GROUNDWATER_PUMP_CAPACITY = 32000;
	public static int GROUNDWATER_PUMP_FILLRATE = 1000;
	public static int GROUNDWATER_PUMP_ENERGY_BASEUSAGE = 160;
	public static int GROUNDWATER_PUMP_ENERGY_PIPEMULTIPLIER = 4;
	public static int GROUNDWATER_PUMP_ENERGY_CAPACITY = 16000;
	public static int GROUNDWATER_PUMP_ENERGY_MAXINPUT = 500;
	public static boolean GROUNDWATER_PUMP_SAFETY = true;

	/**
	 * METHODS FOR CONFIGURATION INITIALIZATION
	 */

	private static void initDisabledRegister() {
		cfg.addCustomCategoryComment(CATEGORY_DISABLE_REGISTER,
				"Disables blocks and items. There are no fallback recipes defined if something is disabled.");
		// ITEMS
		REGISTER_RAIN_ROCKET = cfg.getBoolean("Register Rain Rocket", CATEGORY_DISABLE_REGISTER, REGISTER_RAIN_ROCKET,
				"If true, the Rain Rocket is registered");
		REGISTER_ANTI_RAIN_ROCKET = cfg.getBoolean("Register Anti Rain Rocket", CATEGORY_DISABLE_REGISTER,
				REGISTER_ANTI_RAIN_ROCKET, "If true, the Anti Rain Rocket is registered");
		// BLOCKS
		REGISTER_GROUNDWATER_PUMP = cfg.getBoolean("Register Groundwater Pump", CATEGORY_DISABLE_REGISTER,
				REGISTER_GROUNDWATER_PUMP, "If true, the Groundwater Pump is registered");
		REGISTER_RAIN_COLLECTING_MULTIBLOCK = cfg.getBoolean("Register Rain Collector and Rain Collector Controller",
				CATEGORY_DISABLE_REGISTER, REGISTER_RAIN_COLLECTING_MULTIBLOCK,
				"If true, the Rain Collector and Rain Collector Controller are registered");
		REGISTER_WATER_PIPE = cfg.getBoolean("Register Water Pipe", CATEGORY_DISABLE_REGISTER, REGISTER_WATER_PIPE,
				"If true, the Water Pipe is registered");
		REGISTER_RAIN_TANK = cfg.getBoolean("Register Rain Tank", CATEGORY_DISABLE_REGISTER, REGISTER_RAIN_TANK,
				"If true, the Rain Tank is registered");

	}
	private static void initDisabledRecipe() {
		cfg.addCustomCategoryComment(CATEGORY_DISABLE_RECIPE, "Disables recipes on startup.");
		// ITEMS
		RECIPE_RAIN_ROCKET = cfg.getBoolean("Recipe Rain Rocket", CATEGORY_DISABLE_RECIPE, RECIPE_RAIN_ROCKET,
				"If true, the Rain Rocket has a recipe");
		RECIPE_ANTI_RAIN_ROCKET = cfg.getBoolean("Recipe Anti Rain Rocket", CATEGORY_DISABLE_RECIPE,
				RECIPE_ANTI_RAIN_ROCKET, "If true, the Anti Rain Rocket has a recipe");
		// BLOCKS
		RECIPE_GROUNDWATER_PUMP = cfg.getBoolean("Recipe Groundwater Pump", CATEGORY_DISABLE_RECIPE,
				RECIPE_GROUNDWATER_PUMP, "If true, the Groundwater Pump has a recipe");
		RECIPE_RAIN_COLLECTING_MULTIBLOCK = cfg.getBoolean("Recipe Rain Collector and Rain Collector Controller",
				CATEGORY_DISABLE_RECIPE, RECIPE_RAIN_COLLECTING_MULTIBLOCK,
				"If true, the Rain Collector and Rain Collector Controller have a recipe");
		RECIPE_WATER_PIPE = cfg.getBoolean("Recipe Water Pipe", CATEGORY_DISABLE_RECIPE, RECIPE_WATER_PIPE,
				"If true, the Water Pipe has a recipe");
		RECIPE_RAIN_TANK = cfg.getBoolean("Recipe Rain Tank", CATEGORY_DISABLE_RECIPE, RECIPE_RAIN_TANK,
				"If true, the Rain Tank has a recipe");
	}

	private static void initRainCollectingConfig() {
		cfg.addCustomCategoryComment(CATEGORY_RAIN_COLLECTING, "Configuration for rain collecting blocks");
		// Rain Tank
		RAIN_TANK_SIMPLE_FILLRATE = cfg.getInt("Simple Rain Tank Fillrate", CATEGORY_RAIN_COLLECTING,
				RAIN_TANK_SIMPLE_FILLRATE, 1, 8000, "Amount of water per second");
		RAIN_TANK_SIMPLE_CAPACITY = cfg.getInt("Simple Rain Tank Capacity", CATEGORY_RAIN_COLLECTING,
				RAIN_TANK_SIMPLE_CAPACITY, 1000, 1024000, "Tank capacity in mB");
		// Rain Collector
		RAIN_COLLECTOR_FILLRATE = cfg.getInt("Multiblock Rain Collector Fillrate", CATEGORY_RAIN_COLLECTING,
				RAIN_COLLECTOR_FILLRATE, 1, 8000, "Amount of water per second per connected block");
		RAIN_COLLECTOR_CAPACITY = cfg.getInt("Multiblock Rain Collector Capacity", CATEGORY_RAIN_COLLECTING,
				RAIN_COLLECTOR_CAPACITY, 8000, 1024000, "Tank capacity in mB");
		RAIN_COLLECTOR_RANGE = cfg.getInt("Multiblock Rain Collector Radius", CATEGORY_RAIN_COLLECTING,
				RAIN_COLLECTOR_RANGE, 0, 7, "Radius of the Controller block");
	}

	private static void initGroundWaterPumpConfig() {
		cfg.addCustomCategoryComment(CATEGORY_GROUND_WATER, "Configuration for the Groundwater Pump");
		// FLUID
		GROUNDWATER_PUMP_FILLRATE = cfg.getInt("Groundwater Pump Water Fillrate", CATEGORY_GROUND_WATER,
				GROUNDWATER_PUMP_FILLRATE, 1, 8000, "Amount of water per second");
		GROUNDWATER_PUMP_CAPACITY = cfg.getInt("Groundwater Pump Water Capacity", CATEGORY_GROUND_WATER,
				GROUNDWATER_PUMP_CAPACITY, 8000, 1024000, "Pump water capacity in mB");
		// Energy
		GROUNDWATER_PUMP_ENERGY_CAPACITY = cfg.getInt("Groundwater Pump Energy Capacity", CATEGORY_GROUND_WATER,
				GROUNDWATER_PUMP_ENERGY_CAPACITY, 8000, 1024000, "Pump energy capacity in forge energy units");
		GROUNDWATER_PUMP_ENERGY_MAXINPUT = cfg.getInt("Groundwater Pump Energy Input Rate", CATEGORY_GROUND_WATER,
				GROUNDWATER_PUMP_ENERGY_MAXINPUT, 20, 1024000, "Pump energy input rate in forge energy units");
		GROUNDWATER_PUMP_ENERGY_BASEUSAGE = cfg.getInt("Groundwater Pump Energy Base Usage", CATEGORY_GROUND_WATER,
				GROUNDWATER_PUMP_ENERGY_BASEUSAGE, 20, 1024000,
				"Pump energy base usage in forge energy units. Needed For each pump operation.");
		GROUNDWATER_PUMP_ENERGY_PIPEMULTIPLIER = cfg.getInt("Groundwater Pump Energy Pipe Multiplier",
				CATEGORY_GROUND_WATER, GROUNDWATER_PUMP_ENERGY_PIPEMULTIPLIER, 0, 1024000,
				"Additional to base usage. Each used pipe will multiplied with this value.");
		// Misc
		GROUNDWATER_PUMP_SAFETY = cfg.getBoolean("Groundwater Pump Safety Block", CATEGORY_GROUND_WATER,
				GROUNDWATER_PUMP_SAFETY, "Should the Groundwater Pump spawn a slab to close the hole?");
	}
	private static void initRainRocketsConfig() {
		cfg.addCustomCategoryComment(CATEGORY_ROCKETS, "Configuration for Rockets");
		RAIN_DURATION = cfg.getInt("Rain Duration", CATEGORY_ROCKETS, RAIN_DURATION, 1, 3000,
				"Rain duration with x1 multiplier");
		RAIN_DURATION_MULTIPLIER_MAX = cfg.getInt("Maximum Rain Duration Multiplier", CATEGORY_ROCKETS,
				RAIN_DURATION_MULTIPLIER_MAX, 1, 24, "Rain duration multiplier");

		ANTI_RAIN_DURATION = cfg.getInt("Clear Sky Duration", CATEGORY_ROCKETS, ANTI_RAIN_DURATION, 100, 6000,
				"Clear sky duration with x1 multiplier");
		ANTI_RAIN_DURATION_MULTIPLIER_MAX = cfg.getInt("Maximum Clear Sky Duration Multiplier", CATEGORY_ROCKETS,
				ANTI_RAIN_DURATION_MULTIPLIER_MAX, 1, 24, "Clear sky duration multiplier");
		ANTI_RAIN_MAX_RANDOM_ADDITIONAL_DAYS = cfg.getInt("Maximum Random Additional Days", CATEGORY_ROCKETS,
				ANTI_RAIN_MAX_RANDOM_ADDITIONAL_DAYS, 0, 7,
				"Maximum days of clear sky that will added to the calculated time");

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
			initDisabledRegister();
			initDisabledRecipe();
			initRainCollectingConfig();
			initGroundWaterPumpConfig();
			initRainRocketsConfig();
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
