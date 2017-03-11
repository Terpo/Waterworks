package org.terpo.waterworks.init;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.entity.item.EntityFireworkRocketAntiRain;
import org.terpo.waterworks.entity.item.EntityFireworkRocketRain;
import org.terpo.waterworks.entity.item.RenderFireworkRocketAntiRain;
import org.terpo.waterworks.entity.item.RenderFireworkRocketRain;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InitEntities {
	// Every entity in our mod has an ID (local to this mod)
	public static int entityId = 1;

	public static void init() {
		registerEntities();

	}

	public static void registerEntities() {
		registerEntity(EntityFireworkRocketRain.class, "firework_rocket_rain");
		registerEntity(EntityFireworkRocketAntiRain.class, "firework_rocket_anti_rain");
	}
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void registerEntity(Class entityClass, String name) {
		EntityRegistry.registerModEntity(new ResourceLocation(WaterworksReference.MODID, name), entityClass, name,
				InitEntities.entityId++, Waterworks.instance, 64, 3, true);
	}

	@SideOnly(Side.CLIENT)
	public static void registerRenders() {
		RenderingRegistry.registerEntityRenderingHandler(EntityFireworkRocketRain.class,
				new RenderFireworkRocketRain.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityFireworkRocketAntiRain.class,
				new RenderFireworkRocketAntiRain.Factory());
	}

}
