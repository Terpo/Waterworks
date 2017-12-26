package org.terpo.waterworks.init;

import java.util.ArrayList;
import java.util.List;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.entity.item.EntityFireworkRocketAntiRain;
import org.terpo.waterworks.entity.item.EntityFireworkRocketRain;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.registries.IForgeRegistry;

public class InitEntities {
	// Every entity in our mod has an ID (local to this mod)
	public static int entityId = 1;

	private static <E extends Entity> EntityEntryBuilder<E> createEntityEntryBuilder(final String name) {
		final EntityEntryBuilder<E> builder = EntityEntryBuilder.create();
		final ResourceLocation registryName = new ResourceLocation(WaterworksReference.MODID, name);
		return builder.id(registryName, entityId++).name(registryName.toString());
	}

	public static void init(Register<EntityEntry> event) {
		final IForgeRegistry<EntityEntry> registry = event.getRegistry();
		registerEntities(registry);
	}

	public static void registerEntities(IForgeRegistry<EntityEntry> registry) {
		final List<EntityEntry> entityEntries = new ArrayList<>();
		if (WaterworksConfig.REGISTER_RAIN_ROCKET) {
			entityEntries.add(createEntityEntryBuilder("firework_rocket_rain").entity(EntityFireworkRocketRain.class)
					.tracker(64, 3, true).build());
		}
		if (WaterworksConfig.REGISTER_ANTI_RAIN_ROCKET) {
			entityEntries.add(createEntityEntryBuilder("firework_rocket_anti_rain")
					.entity(EntityFireworkRocketAntiRain.class).tracker(64, 3, true).build());
		}

		if (entityEntries.size() > 0) {
			registry.registerAll(entityEntries.toArray(new EntityEntry[entityEntries.size()]));
		}
	}
}
