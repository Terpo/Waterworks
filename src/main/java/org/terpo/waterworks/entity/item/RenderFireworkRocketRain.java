package org.terpo.waterworks.entity.item;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderFireworkRocketRain extends RenderWeatherFireworkRocket {

	public RenderFireworkRocketRain(EntityRendererManager entityRenderManager, ItemRenderer itemRenderer) {
		super(entityRenderManager, itemRenderer);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityWeatherFireworkRocket entity) {
		return new ResourceLocation("waterworks:textures/item/firework_rain.png");
	}

}
