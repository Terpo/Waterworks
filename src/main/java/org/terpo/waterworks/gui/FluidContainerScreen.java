package org.terpo.waterworks.gui;

import java.util.ArrayList;
import java.util.List;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.fluid.WaterworksTank;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FluidContainerScreen extends ContainerScreen<ContainerBase> {
	protected WaterworksTank fluidTank;

	protected ResourceLocation gui;
	protected Rectangle2d tankRectangle;
	private ResourceLocation waterResource;
	private float[] colors;

	public FluidContainerScreen(ContainerBase screenContainer, PlayerInventory inv, ITextComponent title) {
		super(screenContainer, inv, title);
		prepare(new ResourceLocation(WaterworksReference.MODID, "textures/gui/container/rain_tank_wood.png"),
				new Rectangle2d(80, 17, 16, 52), screenContainer);

	}

	protected void prepare(ResourceLocation newGui, Rectangle2d newTankRectangle, ContainerBase screenContainer) {
		this.fluidTank = screenContainer.getTileWaterworks().getFluidTank();

		this.gui = newGui;
		this.tankRectangle = newTankRectangle;

		// define water colors
		final Fluid fluid = this.fluidTank.getFluid().getFluid();
		this.waterResource = fluid.getAttributes().getStillTexture();
		final int color = fluid.getAttributes().getColor();
		this.colors = new float[]{((color >> 16) & 0xFf) / 255.0f, ((color >> 8) & 0xFf) / 255.0f,
				(color & 0xFf) / 255.0f, ((color >> 24) & 0xFf) / 255.0f};
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(this.gui);
		this.blit(getGuiLeft(), getGuiTop(), 0, 0, getXSize(), getYSize());
		drawTank(this.tankRectangle.getX(), this.tankRectangle.getWidth(), this.tankRectangle.getY(),
				this.tankRectangle.getHeight());
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		// draw Tooltip
		drawTankTooltip(mouseX, mouseY, this.tankRectangle.getX(), this.tankRectangle.getWidth(),
				this.tankRectangle.getY(), this.tankRectangle.getHeight());
	}

	protected void drawTank(int tankPosX, int tankSizeX, int tankPosY, int tankSizeY) {
		if (this.fluidTank != null) {
			final int fillHeight = this.fluidTank.getFluidAmount() * tankSizeY / this.fluidTank.getCapacity();
			final TextureAtlasSprite sprite = this.minecraft.getTextureMap().getSprite(this.waterResource);
			this.minecraft.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

			GlStateManager.color4f(this.colors[0], this.colors[1], this.colors[2], this.colors[3]);
			AbstractGui.blit(getGuiLeft() + tankPosX, getGuiTop() + tankPosY + tankSizeY - fillHeight, 0, tankSizeX,
					fillHeight, sprite);
			GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		}
	}

	protected void drawTankTooltip(int mouseX, int mouseY, int xStartTank, int xSizeTank, int yStartTank,
			int ySizeTank) {
		if (this.fluidTank != null) {
			final String tooltip = this.fluidTank.getFluidAmount() + "/" + this.fluidTank.getCapacity() + " mB";
			final List<String> toolTipText = new ArrayList<>();
			toolTipText.add(tooltip);
			if (getGuiLeft() + xStartTank <= mouseX && mouseX < getGuiLeft() + xStartTank + xSizeTank
					&& mouseY >= getGuiTop() + yStartTank && mouseY < getGuiTop() + yStartTank + ySizeTank) {
				renderTooltip(toolTipText, mouseX - getGuiLeft() + 10, mouseY - getGuiTop());
			}
		}
	}

}
