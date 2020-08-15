package org.terpo.waterworks.tileentity.specialrenderer;

import org.terpo.waterworks.fluid.WaterworksTank;
import org.terpo.waterworks.tileentity.TileWaterworks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
@OnlyIn(Dist.CLIENT)
public class TileEntityWaterRenderer extends TileEntityRenderer<TileWaterworks> {
	int blue;
	int green;
	int red;
	int alpha;
	int lightx;
	int lighty;
	float minU;
	float minV;
	float maxU;
	float maxV;
	float diffU;
	float diffV;

	// model related for drawing in the inner area
	static final float START_POS = 0.0625f;
	static final float END_POS = 1 - START_POS;
	static final float Y_START_OFFSET = 0.125f;

	public TileEntityWaterRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(TileWaterworks te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight,
			int combinedOverlay) {

		final WaterworksTank tank = te.getFluidTank();
		final int amount = tank.getFluidAmount();

		if (amount > 0) {
			final int capacity = tank.getCapacity();
			final FluidStack fluidStack = tank.getFluid();
			final Fluid fluid = fluidStack.getFluid();

			if (fluid != null) {

				@SuppressWarnings("resource")
				final TextureAtlasSprite sprite = Minecraft.getInstance().getSpriteAtlas(PlayerContainer.BLOCK_ATLAS_TEXTURE)
						.apply(fluid.getAttributes().getStillTexture());
				final IVertexBuilder builder = buffer.getBuffer(RenderType.getTranslucent());

				final int color = fluid.getAttributes().getColor();
				this.blue = color & 0xFF;
				this.green = (color >> 8) & 0xFF;
				this.red = (color >> 16) & 0xFF;
				this.alpha = (color >> 24) & 0xFF;

				if (sprite != null) {
					this.minU = sprite.getMinU();
					this.maxU = sprite.getMaxU();
					this.minV = sprite.getMinV();
					this.maxV = sprite.getMaxV();

					final float yFilled = 0.8125f * (amount / (float) capacity);

					final float yHeight = Y_START_OFFSET + yFilled;
					matrixStack.push();

					// default uv drawing is 0,0 -> 1,0 -> 1,1 -> 0,1
					// but then our texture is only visible from below
					// so we turn it around by changing the coordinates to 0,1 -> 1,1 -> 1,0 -> 0,0
					add(builder, matrixStack, START_POS, yHeight, END_POS, this.minU, this.maxV); // bottom left
					add(builder, matrixStack, END_POS, yHeight, END_POS, this.maxU, this.maxV); // bottom right
					add(builder, matrixStack, END_POS, yHeight, START_POS, this.maxU, this.minV); // top right
					add(builder, matrixStack, START_POS, yHeight, START_POS, this.minU, this.minV); // top left

					matrixStack.pop();
				}

			}
		}
	}

	private void add(IVertexBuilder vertexBuilder, MatrixStack matrixStack, float x, float y, float z, float u, float v) {
		vertexBuilder.vertex(matrixStack.peek().getModel(), x, y, z) //
				.color(this.red, this.green, this.blue, this.alpha) //
				.texture(u, v) //
				.light(0, 240) // simply make the fluid visible
				.normal(1, 0, 0) //
				.endVertex();
	}
}
