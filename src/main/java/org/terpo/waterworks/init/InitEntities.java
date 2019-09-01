package org.terpo.waterworks.init;

import java.util.ArrayList;
import java.util.List;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.entity.item.EntityFireworkRocketAntiRain;
import org.terpo.waterworks.entity.item.EntityFireworkRocketRain;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class InitEntities {
	// FIXME possible missing specific entity id
	public static void register(IForgeRegistry<EntityType<?>> registry) {

		final List<EntityType<?>> entityEntries = new ArrayList<>();
//		if (WaterworksConfig.register.rainRocket) {
		entityEntries.add(EntityType.Builder
				.<EntityFireworkRocketRain>create(EntityFireworkRocketRain::new, EntityClassification.MISC)
				.setTrackingRange(64).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true)
				.build("firework_rocket_rain")
				.setRegistryName(new ResourceLocation(WaterworksReference.MODID, "firework_rocket_rain")));
//		}

//		if (WaterworksConfig.register.antiRainRocket) {
		entityEntries.add(EntityType.Builder
				.<EntityFireworkRocketAntiRain>create(EntityFireworkRocketAntiRain::new, EntityClassification.MISC)
				.setTrackingRange(64).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true)
				.build("firework_rocket_anti_rain")
				.setRegistryName(new ResourceLocation(WaterworksReference.MODID, "firework_rocket_anti_rain")));
//		}

		if (!entityEntries.isEmpty()) {
			registry.registerAll(entityEntries.toArray(new EntityType<?>[entityEntries.size()]));
		}
	}

	private InitEntities() {
		// hideme
	}
}
