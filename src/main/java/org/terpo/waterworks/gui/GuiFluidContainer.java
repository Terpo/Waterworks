package org.terpo.waterworks.gui;

import org.terpo.waterworks.fluid.WaterworksTank;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;

//Client Side
public class GuiFluidContainer extends GuiContainerBase {

	protected final WaterworksTank fluidTank;

	public GuiFluidContainer(Container inventorySlotsIn, TileWaterworks te) {
		super(inventorySlotsIn);
		this.fluidTank = te.getFluidTank();
	}

	protected void drawTank(int tankPosX, int tankSizeX, int tankPosY, int tankSizeY) {
		if (this.fluidTank != null) {
			final int fillHeight = this.fluidTank.getFluidAmount() * tankSizeX / this.fluidTank.getCapacity();

			final ResourceLocation waterResource = FluidRegistry.WATER.getStill();
			final TextureAtlasSprite sprite = this.mc.getTextureMapBlocks().getAtlasSprite(waterResource.toString());
			this.mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			this.drawTexturedModalRect(getGuiLeft() + tankPosX, getGuiTop() + tankPosY - fillHeight, sprite, tankSizeY,
					fillHeight);
		}
	}

}
