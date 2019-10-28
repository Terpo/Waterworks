package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;
import org.terpo.waterworks.entity.item.EntityFireworkRocketAntiRain;
import org.terpo.waterworks.entity.item.EntityFireworkRocketRain;

import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.ObjectHolder;

public class WaterworksEntities {
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.ENTITY_FIREWORK_RAIN)
	public static final EntityType<EntityFireworkRocketRain> entityFireworkRain = null;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.ENTITY_FIREWORK_ANTI_RAIN)
	public static final EntityType<EntityFireworkRocketAntiRain> entityFireworkAntiRain = null;

	private WaterworksEntities() {
		// hide me
	}
}
