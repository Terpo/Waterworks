package org.terpo.waterworks.gui;

import org.terpo.waterworks.fluid.WaterworksTank;
import org.terpo.waterworks.tileentity.TileEntityRainTankWood;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;

//Client Side
public class GuiFluidContainer extends GuiContainerBase {

	TileWaterworks te;
	WaterworksTank fluidTank;
	public GuiFluidContainer(Container inventorySlotsIn, TileWaterworks te) {
		super(inventorySlotsIn);
		this.te = te;
		if (te instanceof TileEntityRainTankWood) {
			this.fluidTank = ((TileEntityRainTankWood) te).getFluidTank();
		}

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager()
				.bindTexture(new ResourceLocation("waterworks:textures/gui/container/rain_tank_wood.png"));
		this.drawTexturedModalRect(getGuiLeft(), getGuiTop(), 0, 0, getXSize(), getYSize());

		if (this.fluidTank != null) {
			final int fillHeight = this.fluidTank.getFluidAmount() * 52 / this.fluidTank.getCapacity();

			final ResourceLocation waterResource = FluidRegistry.WATER.getStill();
			final TextureAtlasSprite sprite = this.mc.getTextureMapBlocks().getAtlasSprite(waterResource.toString());
			this.mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			this.drawTexturedModalRect(getGuiLeft() + 80, getGuiTop() + 69 - fillHeight, sprite, 16, fillHeight);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		final String s = this.fluidTank.getFluidAmount() + " mB";
		this.fontRendererObj.drawString(s, 88 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752); // #404040
		this.fontRendererObj.drawString("Player Inventory", 8, 72, 4210752); // #404040

		// draw Tooltip
		final String tooltip = this.fluidTank.getFluidAmount() + "/" + this.fluidTank.getCapacity() + " mB";
		final int stringWidth = this.fontRendererObj.getStringWidth(tooltip);

		if (getGuiLeft() + 80 < mouseX && mouseX < getGuiLeft() + 96 && mouseY > getGuiTop() + 16
				&& mouseY < getGuiTop() + 69) {
			drawRect(mouseX - getGuiLeft() + 5, mouseY - getGuiTop() - 5, mouseX - getGuiLeft() + 15 + stringWidth,
					mouseY - getGuiTop() + 15, 0xFF000000);// this.fluidTank.getFluid().getFluid().getColor()
			this.fontRendererObj.drawString(tooltip, mouseX - getGuiLeft() + 10, mouseY - getGuiTop(), 0xAAAAAA); // #404040
		}

	}

}
