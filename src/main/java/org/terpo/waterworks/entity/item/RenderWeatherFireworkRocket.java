package org.terpo.waterworks.entity.item;

//public class RenderWeatherFireworkRocket extends EntityRenderer<EntityWeatherFireworkRocket> {

//	private final ItemRenderer itemRenderer;
//
//	public RenderWeatherFireworkRocket(EntityRendererManager entityRenderManager) {
//		super(entityRenderManager);
//		this.itemRenderer = Minecraft.getInstance().getItemRenderer();
//	}
//
//	@Override
//	public void doRender(EntityWeatherFireworkRocket entity, double x, double y, double z, float entityYaw,
//			float partialTicks) {
//		GlStateManager.pushMatrix();
//		GlStateManager.translatef((float) x, (float) y, (float) z);
//		GlStateManager.enableRescaleNormal();
//		GlStateManager.rotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
//		GlStateManager.rotatef(
//				(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX, 1.0F, 0.0F,
//				0.0F);
//		if (entity.isShotAtAngle()) {
//			GlStateManager.rotatef(90.0F, 1.0F, 0.0F, 0.0F);
//		} else {
//			GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
//		}
//
//		this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
//		if (this.renderOutlines) {
//			GlStateManager.enableColorMaterial();
//			GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
//		}
//
//		this.itemRenderer.renderItem(entity.getItem(), TransformType.GROUND);
//
//		if (this.renderOutlines) {
//			GlStateManager.tearDownSolidRenderingTextureCombine();
//			GlStateManager.disableColorMaterial();
//		}
//
//		GlStateManager.disableRescaleNormal();
//		GlStateManager.popMatrix();
//		super.doRender(entity, x, y, z, entityYaw, partialTicks);
//	}
//
//	@Override
//	protected ResourceLocation getEntityTexture(EntityWeatherFireworkRocket entity) {
//		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
//	}
//}
