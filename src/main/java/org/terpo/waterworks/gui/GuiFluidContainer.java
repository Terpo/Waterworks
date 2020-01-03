package org.terpo.waterworks.gui;

import java.util.ArrayList;
import java.util.List;

import org.terpo.waterworks.energy.WaterworksBattery;
import org.terpo.waterworks.fluid.WaterworksTank;
import org.terpo.waterworks.tileentity.TileEntityGroundwaterPump;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

//Client Side
public class GuiFluidContainer extends GuiContainerBase {

	protected WaterworksTank fluidTank;
	protected WaterworksBattery battery;

	public GuiFluidContainer(Container inventorySlotsIn, TileWaterworks te) {
		super(inventorySlotsIn);
		this.fluidTank = te.getFluidTank();
		if (te instanceof TileEntityGroundwaterPump) {
			this.battery = ((TileEntityGroundwaterPump) te).getBattery();
		}

	}

	protected void drawTank(int tankPosX, int tankSizeX, int tankPosY, int tankSizeY) {
		if (this.fluidTank != null) {
			final int fillHeight = this.fluidTank.getFluidAmount() * tankSizeY / this.fluidTank.getCapacity();
			if (this.fluidTank.getFluid() != null && this.fluidTank.getFluid().getFluid() != null) {
				final TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks()
						.getTextureExtry(this.fluidTank.getFluid().getFluid().getStill().toString());
				this.mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				this.drawTexturedModalRect(getGuiLeft() + tankPosX, getGuiTop() + tankPosY + tankSizeY - fillHeight,
						sprite, tankSizeX, fillHeight);
			}
		}
	}
	protected void drawBattery(int batteryPosX, int batterySizeX, int batteryPosY, int batterySizeY) {
		if (this.battery != null) {
			final int fillHeight = this.battery.getEnergyStored() * batterySizeY / this.battery.getMaxEnergyStored();

			this.mc.getTextureManager()
					.bindTexture(new ResourceLocation("waterworks:textures/blocks/energy_overlay.png"));
			this.drawTexturedModalRect(getGuiLeft() + batteryPosX,
					getGuiTop() + batteryPosY + batterySizeY - fillHeight, 0, 0, batterySizeX, fillHeight);
		}
	}

	protected void drawTankTooltip(int mouseX, int mouseY, int xStartTank, int xSizeTank, int yStartTank,
			int ySizeTank) {
		final String fluidInfo = this.fluidTank.getFluid() != null
				? " (" + this.fluidTank.getFluid().getLocalizedName() + ")"
				: "";
		final String tooltip = this.fluidTank.getFluidAmount() + "/" + this.fluidTank.getCapacity() + " mB" + fluidInfo;
		final List<String> toolTipText = new ArrayList<>();
		toolTipText.add(tooltip);
		if (getGuiLeft() + xStartTank <= mouseX && mouseX < getGuiLeft() + xStartTank + xSizeTank
				&& mouseY >= getGuiTop() + yStartTank && mouseY < getGuiTop() + yStartTank + ySizeTank) {

			drawHoveringText(toolTipText, mouseX - getGuiLeft() + 10, mouseY - getGuiTop());
		}
	}
	protected void drawBatteryTooltip(int mouseX, int mouseY, int xStartBattery, int xSizeTankBattery,
			int yStartBattery, int ySizeBattery) {
		final String tooltip = this.battery.getEnergyStored() + "/" + this.battery.getMaxEnergyStored() + " RF";

		if (getGuiLeft() + xStartBattery <= mouseX && mouseX < getGuiLeft() + xStartBattery + xSizeTankBattery
				&& mouseY >= getGuiTop() + yStartBattery && mouseY < getGuiTop() + yStartBattery + ySizeBattery) {
			final List<String> toolTipText = new ArrayList<>();
			toolTipText.add(tooltip);
			drawHoveringText(toolTipText, mouseX - getGuiLeft() + 10, mouseY - getGuiTop());
		}
	}

}
