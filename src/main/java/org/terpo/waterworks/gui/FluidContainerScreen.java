package org.terpo.waterworks.gui;

import java.util.ArrayList;
import java.util.List;

import org.terpo.waterworks.energy.WaterworksBattery;
import org.terpo.waterworks.fluid.WaterworksTank;
import org.terpo.waterworks.tileentity.TileEntityGroundwaterPump;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.energy.CapabilityEnergy;

//Client Side
public class FluidContainerScreen extends BaseContainerScreen {

	protected WaterworksTank fluidTank;
	protected WaterworksBattery battery;

	public FluidContainerScreen(ContainerBase screenContainer, PlayerInventory inv, ITextComponent title) {
		super(screenContainer, inv, title);
		this.fluidTank = screenContainer.getTileWaterworks().getFluidTank();
		if (screenContainer.getTileWaterworks() instanceof TileEntityGroundwaterPump) {
			this.battery = (WaterworksBattery) screenContainer.getTileWaterworks()
					.getCapability(CapabilityEnergy.ENERGY).orElse(null);
		}

	}

	protected void drawTank(int tankPosX, int tankSizeX, int tankPosY, int tankSizeY) {
		if (this.fluidTank != null) {
			final int fillHeight = this.fluidTank.getFluidAmount() * tankSizeY / this.fluidTank.getCapacity();

			// FIXME Water Resource
//			final ResourceLocation waterResource = FluidRegistry.WATER.getStill();
//			final TextureAtlasSprite sprite = this.minecraft.getTextureMap().getAtlasSprite(waterResource.toString());
			this.minecraft.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
			this.fillGradient(getGuiLeft() + tankPosX, getGuiTop() + tankPosY + tankSizeY - fillHeight, 0, 0, tankSizeX,
					fillHeight);
		}
	}
	protected void drawBattery(int batteryPosX, int batterySizeX, int batteryPosY, int batterySizeY) {
		if (this.battery != null) {
			final int fillHeight = this.battery.getEnergyStored() * batterySizeY / this.battery.getMaxEnergyStored();

			this.minecraft.getTextureManager()
					.bindTexture(new ResourceLocation("waterworks:textures/blocks/energy_overlay.png"));
			this.fillGradient(getGuiLeft() + batteryPosX, getGuiTop() + batteryPosY + batterySizeY - fillHeight, 0, 0,
					batterySizeX, fillHeight);
		}
	}

	protected void drawTankTooltip(int mouseX, int mouseY, int xStartTank, int xSizeTank, int yStartTank,
			int ySizeTank) {
		final String tooltip = this.fluidTank.getFluidAmount() + "/" + this.fluidTank.getCapacity() + " mB";
		final List<String> toolTipText = new ArrayList<>();
		toolTipText.add(tooltip);
		if (getGuiLeft() + xStartTank <= mouseX && mouseX < getGuiLeft() + xStartTank + xSizeTank
				&& mouseY >= getGuiTop() + yStartTank && mouseY < getGuiTop() + yStartTank + ySizeTank) {
			renderTooltip(toolTipText, mouseX - getGuiLeft() + 10, mouseY - getGuiTop());
		}
	}
	protected void drawBatteryTooltip(int mouseX, int mouseY, int xStartBattery, int xSizeTankBattery,
			int yStartBattery, int ySizeBattery) {
		final String tooltip = this.battery.getEnergyStored() + "/" + this.battery.getMaxEnergyStored() + " RF";

		if (getGuiLeft() + xStartBattery <= mouseX && mouseX < getGuiLeft() + xStartBattery + xSizeTankBattery
				&& mouseY >= getGuiTop() + yStartBattery && mouseY < getGuiTop() + yStartBattery + ySizeBattery) {
			final List<String> toolTipText = new ArrayList<>();
			toolTipText.add(tooltip);
			renderTooltip(toolTipText, mouseX - getGuiLeft() + 10, mouseY - getGuiTop());
		}
	}

}
