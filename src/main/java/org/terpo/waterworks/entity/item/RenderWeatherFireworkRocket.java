package org.terpo.waterworks.entity.item;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
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
	      matrixStackIn.multiply(this.renderManager.getRotation());
	      matrixStackIn.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
	      if (entityIn.isShotAtAngle()) {
	         matrixStackIn.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
	         matrixStackIn.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
	         matrixStackIn.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
	      }

	      this.itemRenderer.renderItem(entityIn.getItem(), ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.DEFAULT_UV, matrixStackIn, bufferIn);
	      matrixStackIn.pop();
		
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getEntityTexture(EntityWeatherFireworkRocket entity) {
		return PlayerContainer.BLOCK_ATLAS_TEXTURE;
	}
}
