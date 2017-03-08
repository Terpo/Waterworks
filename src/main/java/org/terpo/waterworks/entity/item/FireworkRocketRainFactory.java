package org.terpo.waterworks.entity.item;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class FireworkRocketRainFactory implements IRenderFactory<EntityFireworkRocketRain> {

	@Override
	public Render<? super EntityFireworkRocketRain> createRenderFor(RenderManager manager) {
		return new RenderFireworkRocketRain(manager);
	}

}
