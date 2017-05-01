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

	// Groundwater Pump
	public static int GROUNDWATER_PUMP_CAPACITY = 32000;
	public static int GROUNDWATER_PUMP_FILLRATE = 1000;

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
