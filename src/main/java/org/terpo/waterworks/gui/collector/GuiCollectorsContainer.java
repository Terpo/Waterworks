package org.terpo.waterworks.gui.collector;

import org.terpo.waterworks.gui.GuiFluidContainer;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiCollectorsContainer extends GuiFluidContainer {
	private final int TANK_POS_X = 80;
	private final int TANK_SIZE_X = 16;
	private final int TANK_POS_Y = 17;
	private final int TANK_SIZE_Y = 52;
	public GuiCollectorsContainer(Container inventorySlotsIn, TileWaterworks te) {
		super(inventorySlotsIn, te);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager()
				.bindTexture(new ResourceLocation("waterworks:textures/gui/container/rain_tank_wood.png"));
		this.drawTexturedModalRect(getGuiLeft(), getGuiTop(), 0, 0, getXSize(), getYSize());

		drawTank(this.TANK_POS_X, this.TANK_SIZE_X, this.TANK_POS_Y, this.TANK_SIZE_Y);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		// draw Tooltip
		drawTankTooltip(mouseX, mouseY, this.TANK_POS_X, this.TANK_SIZE_X, this.TANK_POS_Y, this.TANK_SIZE_Y);
	}
}
