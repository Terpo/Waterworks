package org.terpo.waterworks;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ForgeI18n;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber
public class Config {

	static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
	public static ForgeConfigSpec commonConfig; // NOSONAR

	public static final RainCollection rainCollection;
	public static final GroundwaterPump pump;
	public static final Rockets rockets;
	public static final WaterworksRecipes recipes;

	static {
		rainCollection = new RainCollection();
		pump = new GroundwaterPump();
		rockets = new Rockets();
		recipes = new WaterworksRecipes();

		commonConfig = COMMON_BUILDER.build();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {
		//
	}

	@SubscribeEvent
	public static void onReload(final ModConfig.Reloading configEvent) {
		//
	}

	static int get(ForgeConfigSpec.IntValue value) {
		return value.get().intValue();
	}

	static boolean get(ForgeConfigSpec.BooleanValue value) {
		return value.get().booleanValue();
	}

	private Config() {
		// hide me
	}

	public static class RainCollection {

		private final ForgeConfigSpec.IntValue woodenRainTankFillrate;
		private final ForgeConfigSpec.IntValue woodenRainTankCapacity;
		private final ForgeConfigSpec.IntValue rainCollectorFillrate;
		private final ForgeConfigSpec.IntValue rainCollectorCapacity;
		private final ForgeConfigSpec.IntValue rainCollectorRange;
		private final ForgeConfigSpec.BooleanValue woodenRainTankDescription;
		private final ForgeConfigSpec.BooleanValue rainCollectorDescription;
		private final ForgeConfigSpec.BooleanValue wrenchDescription;

		private static final String CATEGORY_RAIN_COLLECTION = "rain_collection";

		private static final String SUBCATEGORY_WOODEN_RAIN_TANK = "wooden_rain_tank";
		private static final String SUBCATEGORY_MULTIBLOCK_RAIN_COLLECTOR = "multiblock_rain_collector";

		public RainCollection() {
			COMMON_BUILDER.comment(ForgeI18n.parseMessage("")).push(CATEGORY_RAIN_COLLECTION);

			// Simple Rain Tank
			COMMON_BUILDER.comment("Wooden Rain Tank").push(SUBCATEGORY_WOODEN_RAIN_TANK);

			this.woodenRainTankFillrate = COMMON_BUILDER.comment("The fillrate in mB/second for the Wooden Rain Tank.")
					.defineInRange("woodenRainTankFillrate", 10, 1, 8000);

			this.woodenRainTankCapacity = COMMON_BUILDER.comment("The capacity in mB for the Wooden Rain Tank.")
					.defineInRange("woodenRainTankCapacity", 8000, 1000, 1024000);

			COMMON_BUILDER.pop();

			// Multiblock Rain Collector
			COMMON_BUILDER.comment("Multiblock Rain Collector").push(SUBCATEGORY_MULTIBLOCK_RAIN_COLLECTOR);
			this.rainCollectorFillrate = COMMON_BUILDER.comment("Amount of water per second per connected block.")
					.defineInRange("rainCollectorFillrate", 20, 1, 8000);

			this.rainCollectorCapacity = COMMON_BUILDER.comment("The capacity in mB for the Rain Collector Multiblock.")
					.defineInRange("rainCollectorCapacity", 32000, 8000, 1024000);

			this.rainCollectorRange = COMMON_BUILDER.comment("Search radius of the Rain Collector Controller.")
					.defineInRange("rainCollectorRange", 2, 0, 7);

			this.woodenRainTankDescription = COMMON_BUILDER
					.comment("Turn this to false to disable JEI description for the Wooden Rain Tank.")
					.define("woodenRainTankDescription", true);

			this.rainCollectorDescription = COMMON_BUILDER
					.comment("Turn this to false to disable JEI description for the Rain Collector Multiblock.")
					.define("rainCollectorDescription", true);

			this.wrenchDescription = COMMON_BUILDER.comment("Turn this to false to disable JEI description for the Wrench.")
					.define("wrenchDescription", true);

			COMMON_BUILDER.pop();

			COMMON_BUILDER.pop();
		}

		public int getWoodenRainTankFillrate() {
			return get(this.woodenRainTankFillrate);
		}

		public int getWoodenRainTankCapacity() {
			return get(this.woodenRainTankCapacity);
		}

		public int getRainCollectorFillrate() {
			return get(this.rainCollectorFillrate);
		}

		public int getRainCollectorCapacity() {
			return get(this.rainCollectorCapacity);
		}

		public int getRainCollectorRange() {
			return get(this.rainCollectorRange);
		}

		public boolean getWoodenRainTankDescription() {
			return get(this.woodenRainTankDescription);
		}

		public boolean getRainCollectorDescription() {
			return get(this.rainCollectorDescription);
		}

		public boolean getWrenchDescription() {
			return get(this.wrenchDescription);
		}
	}

	public static class GroundwaterPump {
		private final ForgeConfigSpec.IntValue groundwaterPumpFillrate;
		private final ForgeConfigSpec.IntValue groundwaterPumpCapacity;
		private final ForgeConfigSpec.IntValue groundwaterPumpEnergyBaseUsage;
		private final ForgeConfigSpec.IntValue groundwaterPumpEnergyPipeMultiplier;
		private final ForgeConfigSpec.IntValue groundwaterPumpEnergyCapacity;
		private final ForgeConfigSpec.IntValue groundwaterPumpEnergyInput;
		private final ForgeConfigSpec.IntValue groundwaterPumpEnergyPipePlacement;
		private final ForgeConfigSpec.BooleanValue groundwaterPumpSafety;
		private final ForgeConfigSpec.BooleanValue groundwaterPumpCheckBedrock;
		private final ForgeConfigSpec.BooleanValue groundwaterPumpDescription;

		private static final String CATEGORY_GROUNDWATER_PUMP = "groundwater_pump";

		public GroundwaterPump() {

			COMMON_BUILDER.comment(ForgeI18n.parseMessage("")).push(CATEGORY_GROUNDWATER_PUMP);

			this.groundwaterPumpFillrate = COMMON_BUILDER.comment("The fillrate in mB/second for the Groundwater Pump.")
					.defineInRange("groundwaterPumpFillrate", 500, 1, 8000);

			this.groundwaterPumpCapacity = COMMON_BUILDER.comment("The capacity in mB for the Groundwater Pump.")
					.defineInRange("groundwaterPumpCapacity", 32000, 8000, 1024000);

			this.groundwaterPumpEnergyBaseUsage = COMMON_BUILDER
					.comment("Pump energy base usage in forge energy units. Needed for each pump operation.")
					.defineInRange("groundwaterPumpEnergyBaseUsage", 1600, 20, 1024000);

			this.groundwaterPumpEnergyPipeMultiplier = COMMON_BUILDER
					.comment("Additional to base usage. Each used pipe will multiplied with this value.")
					.defineInRange("groundwaterPumpEnergyPipeMultiplier", 20, 0, 1024000);

			this.groundwaterPumpEnergyCapacity = COMMON_BUILDER.comment("Pump energy capacity in forge energy units.")
					.defineInRange("groundwaterPumpEnergyCapacity", 16000, 8000, 1024000);

			this.groundwaterPumpEnergyInput = COMMON_BUILDER.comment("Pump energy input rate in forge energy units.")
					.defineInRange("groundwaterPumpEnergyInput", 500, 20, 1024000);

			this.groundwaterPumpEnergyPipePlacement = COMMON_BUILDER.comment("Energy used to place a pipe.")
					.defineInRange("groundwaterPumpEnergyPipePlacement", 2500, 0, 1024000);

			this.groundwaterPumpSafety = COMMON_BUILDER.comment("Should the Groundwater Pump spawn a slab to close the hole?")
					.define("groundwaterPumpSafety", true);

			this.groundwaterPumpCheckBedrock = COMMON_BUILDER
					.comment("Turn this to false if your world does not generate Bedrock. (Skyblock)")
					.define("groundwaterPumpCheckBedrock", true);

			this.groundwaterPumpDescription = COMMON_BUILDER
					.comment("Turn this to false to disable JEI description for the Groundwater Pump.")
					.define("groundwaterPumpDescription", true);

			COMMON_BUILDER.pop();
		}

		public int getGroundwaterPumpFillrate() {
			return get(this.groundwaterPumpFillrate);
		}

		public int getGroundwaterPumpCapacity() {
			return get(this.groundwaterPumpCapacity);
		}

		public int getGroundwaterPumpEnergyBaseUsage() {
			return get(this.groundwaterPumpEnergyBaseUsage);
		}

		public int getGroundwaterPumpEnergyPipeMultiplier() {
			return get(this.groundwaterPumpEnergyPipeMultiplier);
		}

		public int getGroundwaterPumpEnergyCapacity() {
			return get(this.groundwaterPumpEnergyCapacity);
		}

		public int getGroundwaterPumpEnergyInput() {
			return get(this.groundwaterPumpEnergyInput);
		}

		public int getGroundwaterPumpEnergyPipePlacement() {
			return get(this.groundwaterPumpEnergyPipePlacement);
		}

		public boolean getGroundwaterPumpSafety() {
			return get(this.groundwaterPumpSafety);
		}

		public boolean getGroundwaterPumpCheckBedrock() {
			return get(this.groundwaterPumpCheckBedrock);
		}

		public boolean getGroundwaterPumpDescription() {
			return get(this.groundwaterPumpDescription);
		}
	}

	public static class Rockets {
		private static final String CATEGORY_ROCKETS = "rockets";

		private final ForgeConfigSpec.IntValue rainDuration;
		private final ForgeConfigSpec.IntValue rainMaxMultiplier;
		private final ForgeConfigSpec.IntValue clearSkyDuration;
		private final ForgeConfigSpec.IntValue clearSkyMaxMultiplier;
		private final ForgeConfigSpec.IntValue clearSkyMaxRandomAdditionalDays;
		private final ForgeConfigSpec.BooleanValue fireworkRocketsDescription;
		private final ForgeConfigSpec.BooleanValue fireworkRocketsJEIRecipes;
		private final ForgeConfigSpec.BooleanValue fireworkChargeDescription;
		private final ForgeConfigSpec.BooleanValue fireworksDescription;

		public Rockets() {

			COMMON_BUILDER.comment(ForgeI18n.parseMessage("")).push(CATEGORY_ROCKETS);

			// Rain Rocket
			this.rainDuration = COMMON_BUILDER.comment("Rain duration with x1 multiplier.").defineInRange("rainDuration", 3000, 1, 12000);

			this.rainMaxMultiplier = COMMON_BUILDER.comment("Maximum rain multiplier.").defineInRange("rainMaxMultiplier", 8, 1, 24);

			// Anti Rain Rocket
			this.clearSkyDuration = COMMON_BUILDER.comment("Clear sky duration with x1 multiplier.").defineInRange("clearSkyDuration", 4000,
					100, 6000);

			this.clearSkyMaxMultiplier = COMMON_BUILDER.comment("Maximum clear sky multiplier.").defineInRange("clearSkyMaxMultiplier", 12,
					1, 36);

			this.clearSkyMaxRandomAdditionalDays = COMMON_BUILDER
					.comment("Maximum days of clear sky that will added to the calculated time.")
					.defineInRange("clearSkyMaxRandomAdditionalDays", 3, 0, 7);

			// JEI
			this.fireworkRocketsDescription = COMMON_BUILDER.comment("Turn this to false to disable JEI description for the Rockets.")
					.define("fireworkRocketsDescription", true);
			this.fireworkRocketsJEIRecipes = COMMON_BUILDER.comment("Turn this to false to disable JEI recipe information for the Rockets.")
					.define("fireworkRocketsJEIRecipes", true);

			// JEI Vanilla Firework
			this.fireworkChargeDescription = COMMON_BUILDER.comment("JEI: Adds a small description for firework star.")
					.define("fireworkChargeDescription", true);

			this.fireworksDescription = COMMON_BUILDER.comment("JEI: Adds a small description for fireworks.")
					.define("fireworksDescription", true);

			COMMON_BUILDER.pop();
		}

		public int getRainDuration() {
			return get(this.rainDuration);
		}

		public int getRainMaxMultiplier() {
			return get(this.rainMaxMultiplier);
		}

		public int getClearSkyDuration() {
			return get(this.clearSkyDuration);
		}

		public int getClearSkyMaxMultiplier() {
			return get(this.clearSkyMaxMultiplier);
		}

		public int getClearSkyMaxRandomAdditionalDays() {
			return get(this.clearSkyMaxRandomAdditionalDays);
		}

		public boolean getFireworkRocketsDescription() {
			return get(this.fireworkRocketsDescription);
		}

		public boolean getFireworkRocketsJEIRecipes() {
			return get(this.fireworkRocketsJEIRecipes);
		}

		public boolean getFireworkChargeDescription() {
			return get(this.fireworkChargeDescription);
		}

		public boolean getFireworksDescription() {
			return get(this.fireworksDescription);
		}
	}

	public static class WaterworksRecipes {

		private static final String CATEGORY_RECIPES = "recipes";

		private final ForgeConfigSpec.BooleanValue recipeRainRocket;
		private final ForgeConfigSpec.BooleanValue recipeAntiRainRocket;

		public WaterworksRecipes() {

			COMMON_BUILDER.comment(ForgeI18n.parseMessage("")).push(CATEGORY_RECIPES);

			this.recipeRainRocket = COMMON_BUILDER.comment("If true, the Rain Rocket has a recipe.").define("recipeRainRocket", true);

			this.recipeAntiRainRocket = COMMON_BUILDER.comment("If true, the Anti Rain Rocket has a recipe.").define("recipeAntiRainRocket",
					true);

			COMMON_BUILDER.pop();
		}

		public boolean getRecipeRainRocket() {
			return get(this.recipeRainRocket);
		}

		public boolean getRecipeAntiRainRocket() {
			return get(this.recipeAntiRainRocket);
		}
	}
}
