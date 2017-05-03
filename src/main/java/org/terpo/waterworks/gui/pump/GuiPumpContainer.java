package org.terpo.waterworks.gui.pump;

import org.terpo.waterworks.gui.GuiFluidContainer;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiPumpContainer extends GuiFluidContainer {

	public GuiPumpContainer(Container inventorySlotsIn, TileWaterworks te) {
		super(inventorySlotsIn, te);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager()
				.bindTexture(new ResourceLocation("waterworks:textures/gui/container/groundwater_pump.png"));
		this.drawTexturedModalRect(getGuiLeft(), getGuiTop(), 0, 0, getXSize(), getYSize());

		drawTank(80, 52, 69, 16);
		drawBattery(8, 52, 69, 16);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		final String s = this.fluidTank.getFluidAmount() + " mB";
		this.fontRendererObj.drawString(s, 88 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752); // #404040
		this.fontRendererObj.drawString("Player Inventory", 8, 72, 4210752); // #404040
		// draw Tooltip
		drawTankTooltip(mouseX, mouseY);
		drawBatteryTooltip(mouseX, mouseY);

	}

	protected void drawTankTooltip(int mouseX, int mouseY) {
		final String tooltip = this.fluidTank.getFluidAmount() + "/" + this.fluidTank.getCapacity() + " mB";
		final int stringWidth = this.fontRendererObj.getStringWidth(tooltip);

		if (getGuiLeft() + 80 < mouseX && mouseX < getGuiLeft() + 96 && mouseY > getGuiTop() + 16
				&& mouseY < getGuiTop() + 69) {
			drawRect(mouseX - getGuiLeft() + 5, mouseY - getGuiTop() - 5, mouseX - getGuiLeft() + 15 + stringWidth,
					mouseY - getGuiTop() + 15, 0xFF000000);// this.fluidTank.getFluid().getFluid().getColor()
			this.fontRendererObj.drawString(tooltip, mouseX - getGuiLeft() + 10, mouseY - getGuiTop(), 0xAAAAAA); // #404040
		}
	}
	protected void drawBatteryTooltip(int mouseX, int mouseY) {
		final String tooltip = this.battery.getEnergyStored() + "/" + this.battery.getMaxEnergyStored() + " RF";
		final int stringWidth = this.fontRendererObj.getStringWidth(tooltip);

		if (getGuiLeft() + 8 < mouseX && mouseX < getGuiLeft() + 24 && mouseY > getGuiTop() + 16
				&& mouseY < getGuiTop() + 69) {
			drawRect(mouseX - getGuiLeft() + 5, mouseY - getGuiTop() - 5, mouseX - getGuiLeft() + 15 + stringWidth,
					mouseY - getGuiTop() + 15, 0xFF000000);// this.fluidTank.getFluid().getFluid().getColor()
			this.fontRendererObj.drawString(tooltip, mouseX - getGuiLeft() + 10, mouseY - getGuiTop(), 0xAAAAAA); // #404040
		}
	}
}
