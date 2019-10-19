package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;
import org.terpo.waterworks.entity.item.EntityFireworkRocketAntiRain;
import org.terpo.waterworks.entity.item.EntityFireworkRocketRain;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.IForgeRegistry;

public class InitEntities {
	// FIXME possible missing specific entity id
	public static void register(IForgeRegistry<EntityType<?>> registry) {

//		final List<EntityType<?>> entityEntries = new ArrayList<>();
//		if (WaterworksConfig.register.rainRocket) {
		final EntityType<EntityFireworkRocketRain> rainRocketBuilder = EntityType.Builder
				.<EntityFireworkRocketRain>create(EntityFireworkRocketRain::new, EntityClassification.MISC)
				.setTrackingRange(64).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true)
				.build(WaterworksRegistryNames.ENTITY_FIREWORK_RAIN);
		rainRocketBuilder.setRegistryName(WaterworksReference.MODID, WaterworksRegistryNames.ENTITY_FIREWORK_RAIN);
		registry.register(rainRocketBuilder);
		WaterworksEntities.itemFireworkRain = rainRocketBuilder;
//		}

//		if (WaterworksConfig.register.antiRainRocket) {

		final EntityType<EntityFireworkRocketAntiRain> antiRainRocketBuilder = EntityType.Builder
				.<EntityFireworkRocketAntiRain>create(EntityFireworkRocketAntiRain::new, EntityClassification.MISC)
				.setTrackingRange(64).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true)
				.build(WaterworksRegistryNames.ENTITY_FIREWORK_ANTI_RAIN);
		antiRainRocketBuilder.setRegistryName(WaterworksReference.MODID,
				WaterworksRegistryNames.ENTITY_FIREWORK_ANTI_RAIN);
		registry.register(antiRainRocketBuilder);
		WaterworksEntities.itemFireworkAntiRain = antiRainRocketBuilder;

//		}

//		if (!entityEntries.isEmpty()) {
//			registry.registerAll(entityEntries.toArray(new EntityType<?>[entityEntries.size()]));
//		}
	}

	private InitEntities() {
		// hideme
	}
}
