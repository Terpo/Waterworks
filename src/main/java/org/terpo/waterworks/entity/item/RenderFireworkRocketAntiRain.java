package org.terpo.waterworks.entity.item;

import org.terpo.waterworks.init.WaterworksItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderFireworkRocketAntiRain extends Render<EntityFireworkRocketAntiRain> {

	private final RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
	private final Item item = WaterworksItems.itemFireworkAntiRain;
	public RenderFireworkRocketAntiRain(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityFireworkRocketAntiRain entity, double x, double y, double z, float entityYaw,
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

		this.itemRenderer.renderItem(getStackToRender(), ItemCameraTransforms.TransformType.GROUND);

		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	public ItemStack getStackToRender() {
		return new ItemStack(this.item);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityFireworkRocketAntiRain entity) {
		return new ResourceLocation("waterworks:textures/item/firework_anti_rain.png");
	}

	public static class Factory implements IRenderFactory<EntityFireworkRocketAntiRain> {
		@Override
		public Render<? super EntityFireworkRocketAntiRain> createRenderFor(RenderManager manager) {
			return new RenderFireworkRocketAntiRain(manager);
		}

	}

}
