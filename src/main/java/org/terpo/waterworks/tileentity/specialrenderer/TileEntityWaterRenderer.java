package org.terpo.waterworks.tileentity.specialrenderer;

import org.lwjgl.opengl.GL11;
import org.terpo.waterworks.fluid.WaterworksTank;
import org.terpo.waterworks.tileentity.TileWaterworks;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
@OnlyIn(Dist.CLIENT)
public class TileEntityWaterRenderer extends TileEntityRenderer<TileWaterworks> {
	int blue;
	int green;
	int red;
	int alphaValue;
	int lightx;
	int lighty;
	double minU;
	double minV;
	double maxU;
	double maxV;
	double diffU;
	double diffV;

	public TileEntityWaterRenderer() {
		super();
	}

	@Override
	public void render(TileWaterworks te, double x, double y, double z, float partialTicks, int destroyStage) {
		final WaterworksTank tank = te.getFluidTank();
		final int amount = tank.getFluidAmount();
		final int capacity = tank.getCapacity();
		final FluidStack fluidStack = tank.getFluid();
		final Fluid fluid = fluidStack.getFluid();

		if (fluid != null) {
			final int c = fluid.getAttributes().getColor();
			this.blue = c & 0xFF;
			this.green = (c >> 8) & 0xFF;
			this.red = (c >> 16) & 0xFF;
			this.alphaValue = (c >> 24) & 0xFF;
			final TextureAtlasSprite sprite = Minecraft.getInstance().getTextureMap()
					.getSprite(fluid.getAttributes().getStillTexture());
			this.diffU = this.maxU - this.minU;
			this.diffV = this.maxV - this.minV;

			if (sprite != null) {
				final double multiplier = 0.25;
				this.minU = sprite.getMinU() + this.diffU * multiplier;
				this.maxU = sprite.getMaxU() - this.diffU * multiplier;
				this.minV = sprite.getMinV() + this.diffV * multiplier;
				this.maxV = sprite.getMaxV() - this.diffV * multiplier;

				// pos.up is really important here, otherwise the lighing is broken
				final int i = getWorld().getCombinedLight(te.getPos().up(), fluid.getAttributes().getLuminosity());
				this.lightx = i >> 0x10 & 0xFFFF;
				this.lighty = i & 0xFFFF;

				final double yFilled = 0.8125 * (amount / (double) capacity);
				final double startPos = 0.0625;
				final double endPos = 1 - startPos;
				final double yStartOffset = 0.125;

				GlStateManager.disableCull();
				GlStateManager.disableLighting();
				GlStateManager.enableBlend();
				GlStateManager.enableAlphaTest();
				GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
				Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
				final Tessellator tess = Tessellator.getInstance();
				final BufferBuilder buffer = tess.getBuffer();

				buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);

				buffer.pos(x + startPos, y + yStartOffset + yFilled, z + startPos).tex(this.minU, this.minV)
						.lightmap(this.lightx, this.lighty).color(this.red, this.green, this.blue, this.alphaValue)
						.endVertex();
				buffer.pos(x + endPos, y + yStartOffset + yFilled, z + startPos).tex(this.maxU, this.minV)
						.lightmap(this.lightx, this.lighty).color(this.red, this.green, this.blue, this.alphaValue)
						.endVertex();
				buffer.pos(x + endPos, y + yStartOffset + yFilled, z + endPos).tex(this.maxU, this.maxV)
						.lightmap(this.lightx, this.lighty).color(this.red, this.green, this.blue, this.alphaValue)
						.endVertex();
				buffer.pos(x + startPos, y + yStartOffset + yFilled, z + endPos).tex(this.minU, this.maxV)
						.lightmap(this.lightx, this.lighty).color(this.red, this.green, this.blue, this.alphaValue)
						.endVertex();
				tess.draw();

				GlStateManager.disableBlend();
				GlStateManager.enableLighting();
			}
		}
	}
}
