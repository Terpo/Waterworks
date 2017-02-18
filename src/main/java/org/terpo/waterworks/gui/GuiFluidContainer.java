package org.terpo.waterworks.gui;

import org.terpo.waterworks.fluid.WaterworksTank;
import org.terpo.waterworks.tileentity.TileEntityRainTankWood;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

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
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		if (this.fluidTank != null) {
			final int fillHeight = this.fluidTank.getFluidAmount() * 52 / this.fluidTank.getCapacity();

			this.mc.getTextureManager()
					.bindTexture(new ResourceLocation("minecraft:textures/blocks/water_overlay.png"));
			this.drawTexturedModalRect(this.guiLeft + 80, this.guiTop + 69 - fillHeight, 0, 0, 16, fillHeight);
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

		if (this.guiLeft + 80 < mouseX && mouseX < this.guiLeft + 96 && mouseY > this.guiTop + 16
				&& mouseY < this.guiTop + 69) {
			drawRect(mouseX - this.guiLeft + 5, mouseY - this.guiTop - 5, mouseX - this.guiLeft + 15 + stringWidth,
					mouseY - this.guiTop + 15, 0xFF000000);// this.fluidTank.getFluid().getFluid().getColor()
			this.fontRendererObj.drawString(tooltip, mouseX - this.guiLeft + 10, mouseY - this.guiTop, 0xAAAAAA); // #404040
		}

	}

}
