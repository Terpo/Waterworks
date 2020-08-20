package org.terpo.waterworks.gui;

import org.terpo.waterworks.api.constants.Reference;
import org.terpo.waterworks.fluid.WaterworksTank;
import org.terpo.waterworks.tileentity.TileWaterworks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

//TODO use capability for fluid tank drawing
@OnlyIn(Dist.CLIENT)
public class FluidContainerScreen extends ContainerScreen<ContainerBase> {
	protected WaterworksTank fluidTank;
	protected TileWaterworks tileEntity;

	protected ResourceLocation gui;
	protected Rectangle2d tankRectangle;
	private ResourceLocation waterResource;
	private float[] colors;
	private boolean hasColorAttributes;

	public FluidContainerScreen(ContainerBase screenContainer, PlayerInventory inv, ITextComponent title) {
		super(screenContainer, inv, title);
		prepare(new ResourceLocation(Reference.MODID, "textures/gui/container/rain_tank_wood.png"),
				new Rectangle2d(80, 17, 16, 52), screenContainer);

	}

	protected void prepare(ResourceLocation newGui, Rectangle2d newTankRectangle, ContainerBase screenContainer) {
		this.tileEntity = screenContainer.getTileWaterworks();
		this.fluidTank = screenContainer.getTileWaterworks().getFluidTank();

		this.gui = newGui;
		this.tankRectangle = newTankRectangle;

		// define water colors
		this.hasColorAttributes = defineColors();
	}

	protected boolean defineColors() {
		final Fluid fluid = this.fluidTank.getFluid().getFluid();
		this.waterResource = fluid.getAttributes().getStillTexture();
		final int color = fluid.getAttributes().getColor();
		this.colors = new float[] { ((color >> 16) & 0xFf) / 255.0f, ((color >> 8) & 0xFf) / 255.0f,
				(color & 0xFf) / 255.0f, ((color >> 24) & 0xFf) / 255.0f };

		return (0f != this.colors[0]);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.drawMouseoverTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(this.gui);
		this.drawTexture(matrixStack,getGuiLeft(), getGuiTop(), 0, 0, getXSize(), getYSize());
		drawTank(matrixStack,this.tankRectangle.getX(), this.tankRectangle.getWidth(), this.tankRectangle.getY(),
				this.tankRectangle.getHeight());
	}

	@Override
	protected void drawForeground(MatrixStack matrixStack, int mouseX, int mouseY) {
		super.drawForeground(matrixStack, mouseX, mouseY);
		// draw Tooltip
		drawTankTooltip(matrixStack, mouseX, mouseY, this.tankRectangle.getX(), this.tankRectangle.getWidth(),
				this.tankRectangle.getY(), this.tankRectangle.getHeight());
	}

	protected void drawTank(MatrixStack matrixStack, int tankPosX, int tankSizeX, int tankPosY, int tankSizeY) {

		if (this.fluidTank != null) {
			if (!this.hasColorAttributes) {
				this.hasColorAttributes = defineColors();
			}

			final int fillHeight = this.fluidTank.getFluidAmount() * tankSizeY / this.fluidTank.getCapacity();
			this.client.getTextureManager().bindTexture(PlayerContainer.BLOCK_ATLAS_TEXTURE);
			RenderSystem.color4f(this.colors[0], this.colors[1], this.colors[2], this.colors[3]);
			AbstractGui.drawSprite(matrixStack,getGuiLeft() + tankPosX, getGuiTop() + tankPosY + tankSizeY - fillHeight, 0, tankSizeX,
					fillHeight, Minecraft.getInstance().getSpriteAtlas(PlayerContainer.BLOCK_ATLAS_TEXTURE)
							.apply(this.waterResource));
			RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		}
	}

	protected void drawTankTooltip(MatrixStack matrixStack, int mouseX, int mouseY, int xStartTank, int xSizeTank,
			int yStartTank, int ySizeTank) {
		if (this.fluidTank != null) {
			final StringTextComponent tooltip = new StringTextComponent(
					this.fluidTank.getFluidAmount() + "/" + this.fluidTank.getCapacity() + " mB");
			if (getGuiLeft() + xStartTank <= mouseX && mouseX < getGuiLeft() + xStartTank + xSizeTank
					&& mouseY >= getGuiTop() + yStartTank && mouseY < getGuiTop() + yStartTank + ySizeTank) {
				renderTooltip(matrixStack, tooltip, mouseX - getGuiLeft() + 10, mouseY - getGuiTop());
			}
		}
	}

}
