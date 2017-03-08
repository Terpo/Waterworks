package org.terpo.waterworks.init;

import org.terpo.waterworks.Waterworks;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class WaterworksConfig {
	public static Configuration cfg;

	// Categories
	private static final String CATEGORY_RAIN_TANK_SIMPLE = "Simple Rain Tank";
	private static final String CATEGORY_ROCKETS = "Rain Rockets";
	// Simple Rain Tank
	public static int RAIN_TANK_SIMPLE_FILLRATE = 100;
	public static int RAIN_TANK_SIMPLE_CAPACITY = 8000;

	// Rain Rocket
	public static int RAIN_DURATION = 240;
	public static int RAIN_DURATION_MULTIPLIER_MAX = 8;

	private static void initRainTankSimpleConfig() {
		cfg.addCustomCategoryComment(CATEGORY_RAIN_TANK_SIMPLE, "Rain Tank Configuration");
		RAIN_TANK_SIMPLE_FILLRATE = cfg.getInt("fillrate", CATEGORY_RAIN_TANK_SIMPLE, RAIN_TANK_SIMPLE_FILLRATE, 1,
				8000, "Amount of water per second");
		RAIN_TANK_SIMPLE_CAPACITY = cfg.getInt("capacity", CATEGORY_RAIN_TANK_SIMPLE, RAIN_TANK_SIMPLE_CAPACITY, 1000,
				1024000, "Tank capacity in mB");
	}

	private static void initRainRockets() {
		cfg.addCustomCategoryComment(CATEGORY_ROCKETS, "Rain Rocket Configuration");
		RAIN_DURATION = cfg.getInt("duration", CATEGORY_ROCKETS, RAIN_DURATION, 1, 3000,
				"Rain duration with x1 multiplier");
		RAIN_DURATION_MULTIPLIER_MAX = cfg.getInt("multiplier", CATEGORY_ROCKETS, RAIN_DURATION_MULTIPLIER_MAX, 1, 24,
				"Rain duration multiplier");

	}

	public static void init(FMLPreInitializationEvent event) {
		cfg = new Configuration(event.getSuggestedConfigurationFile());
	}
	public static void load() {
		try {
			cfg.load();
			initRainTankSimpleConfig();
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
