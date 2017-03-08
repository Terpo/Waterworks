package org.terpo.waterworks.entity.item;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.init.WaterworksItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderFireworkRocketRain extends Render<EntityFireworkRocketRain> {

	private final RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
	protected RenderFireworkRocketRain(RenderManager renderManager) {
		super(renderManager);
	}

	/**
	 * Renders the desired {@code T} type Entity.
	 */
	@Override
	public void doRender(EntityFireworkRocketRain entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(
				(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX, 1.0F, 0.0F,
				0.0F);
		GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}

		this.itemRenderer.renderItem(new ItemStack(WaterworksItems.firework_charge_rain),
				ItemCameraTransforms.TransformType.GROUND);

		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	public static ItemStack getStackToRender() {
		return new ItemStack(WaterworksItems.firework_charge_rain);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityFireworkRocketRain entity) {
		Waterworks.LOGGER.info("resourceLocation");
		return new ResourceLocation("waterworks:textures/item/firework_rain.png");
		// return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

}
