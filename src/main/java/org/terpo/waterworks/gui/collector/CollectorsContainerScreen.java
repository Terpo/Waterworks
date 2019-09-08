package org.terpo.waterworks.gui.collector;

import org.terpo.waterworks.gui.ContainerBase;
import org.terpo.waterworks.gui.FluidContainerScreen;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CollectorsContainerScreen extends FluidContainerScreen {
	private final int TANK_POS_X = 80;
	private final int TANK_SIZE_X = 16;
	private final int TANK_POS_Y = 17;
	private final int TANK_SIZE_Y = 52;
	public CollectorsContainerScreen(ContainerBase screenContainer, PlayerInventory inv, ITextComponent title) {
		super(screenContainer, inv, title);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.minecraft.getTextureManager()
				.bindTexture(new ResourceLocation("waterworks:textures/gui/container/rain_tank_wood.png"));
		this.fillGradient(getGuiLeft(), getGuiTop(), 0, 0, getXSize(), getYSize());

		drawTank(this.TANK_POS_X, this.TANK_SIZE_X, this.TANK_POS_Y, this.TANK_SIZE_Y);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		// draw Tooltip
		drawTankTooltip(mouseX, mouseY, this.TANK_POS_X, this.TANK_SIZE_X, this.TANK_POS_Y, this.TANK_SIZE_Y);
	}
}
