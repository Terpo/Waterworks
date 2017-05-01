package org.terpo.waterworks.gui;

import org.terpo.waterworks.energy.WaterworksBattery;
import org.terpo.waterworks.fluid.WaterworksTank;
import org.terpo.waterworks.tileentity.TileEntityGroundwaterPump;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;

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

	protected void drawTank(int tankPosX, int tankSizeX, int tankPosY, int tankWidth) {
		if (this.fluidTank != null) {
			final int fillHeight = this.fluidTank.getFluidAmount() * tankSizeX / this.fluidTank.getCapacity();

			final ResourceLocation waterResource = FluidRegistry.WATER.getStill();
			final TextureAtlasSprite sprite = this.mc.getTextureMapBlocks().getAtlasSprite(waterResource.toString());
			this.mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			this.drawTexturedModalRect(getGuiLeft() + tankPosX, getGuiTop() + tankPosY - fillHeight, sprite, tankWidth,
					fillHeight);
		}
	}
	protected void drawBattery(int batteryPosX, int batterySizeX, int batteryPosY, int tankWidth) {
		if (this.battery != null) {
			final int fillHeight = this.battery.getEnergyStored() * batterySizeX / this.battery.getMaxEnergyStored();

			this.mc.getTextureManager()
					.bindTexture(new ResourceLocation("waterworks:textures/blocks/energy_overlay.png"));
			this.drawTexturedModalRect(getGuiLeft() + batteryPosX, getGuiTop() + batteryPosY - fillHeight, 0, 0,
					tankWidth, fillHeight);
		}
	}

}
