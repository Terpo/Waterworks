package org.terpo.waterworks.gui.pump;

import org.terpo.waterworks.gui.ContainerBase;
import org.terpo.waterworks.gui.FluidContainerScreen;

import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class PumpContainerScreen extends FluidContainerScreen {
	protected Rectangle2d batteryRectangle;

	public PumpContainerScreen(ContainerBase screenContainer, PlayerInventory inv, ITextComponent title) {
		super(new ResourceLocation("waterworks:textures/gui/container/groundwater_pump.png"),
				new Rectangle2d(80, 17, 16, 52), screenContainer, inv, title);
		this.batteryRectangle = new Rectangle2d(8, 17, 16, 52);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		drawBattery(this.batteryRectangle);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		// draw Tooltip
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		drawBatteryTooltip(mouseX, mouseY, this.batteryRectangle);
	}
}
