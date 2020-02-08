package org.terpo.waterworks.entity.item;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderWeatherFireworkRocket extends EntityRenderer<EntityWeatherFireworkRocket> {

	private final ItemRenderer itemRenderer;

	public RenderWeatherFireworkRocket(EntityRendererManager entityRenderManager) {
		super(entityRenderManager);
		this.itemRenderer = Minecraft.getInstance().getItemRenderer();
	}
	/**
	 * the texture for the rockets will be fetched from the entity itemStack, no need to give a texture information
	 *
	 */
	@Override
	public void render(EntityWeatherFireworkRocket entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();
		matrixStackIn.rotate(this.renderManager.getCameraOrientation());
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
		if (entityIn.isShotAtAngle()) {
			matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180.0F));
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
			matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
		}

		@SuppressWarnings("deprecation")
		final net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType transform = net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType.GROUND; // NOSONAR

		this.itemRenderer.renderItem(entityIn.getRocketItem(), transform, packedLightIn, OverlayTexture.DEFAULT_LIGHT, matrixStackIn,
				bufferIn);
		matrixStackIn.pop();
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getEntityTexture(EntityWeatherFireworkRocket entity) {
		return PlayerContainer.LOCATION_BLOCKS_TEXTURE;
	}
}
