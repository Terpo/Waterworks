package org.terpo.waterworks.entity.item;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderFireworkRocketAntiRain extends RenderWeatherFireworkRocket {

	public RenderFireworkRocketAntiRain(EntityRendererManager entityRenderManager, ItemRenderer itemRenderer) {
		super(entityRenderManager, itemRenderer);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityWeatherFireworkRocket entity) {
		return new ResourceLocation("waterworks:textures/item/firework_anti_rain.png");
	}
}
