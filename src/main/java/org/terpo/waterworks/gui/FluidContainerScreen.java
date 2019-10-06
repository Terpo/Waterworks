package org.terpo.waterworks.gui;

import java.util.ArrayList;
import java.util.List;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.energy.WaterworksBattery;
import org.terpo.waterworks.fluid.WaterworksTank;
import org.terpo.waterworks.tileentity.TileEntityGroundwaterPump;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.energy.CapabilityEnergy;

//Client Side
public class FluidContainerScreen extends ContainerScreen<ContainerBase> {

	protected WaterworksTank fluidTank;
	protected WaterworksBattery battery;
	protected ResourceLocation gui;
	protected Rectangle2d tankRectangle;
	private final ResourceLocation waterResource = Fluids.WATER.getAttributes().getStillTexture();

	public FluidContainerScreen(ResourceLocation gui, Rectangle2d tankRectangle, ContainerBase screenContainer,
			PlayerInventory inv, ITextComponent title) {
		super(screenContainer, inv, title);
		this.fluidTank = screenContainer.getTileWaterworks().getFluidTank();
		if (screenContainer.getTileWaterworks() instanceof TileEntityGroundwaterPump) {
			this.battery = (WaterworksBattery) screenContainer.getTileWaterworks()
					.getCapability(CapabilityEnergy.ENERGY).orElse(null);
		}
		this.gui = gui;
		this.tankRectangle = tankRectangle;

	}

	public FluidContainerScreen(ContainerBase screenContainer, PlayerInventory inv, ITextComponent title) {
		this(new ResourceLocation(WaterworksReference.MODID, "textures/gui/container/rain_tank_wood.png"),
				new Rectangle2d(80, 17, 16, 52), screenContainer, inv, title);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(this.gui);
//		this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
		this.blit(getGuiLeft(), getGuiTop(), 0, 0, getXSize(), getYSize());
		drawTank(this.tankRectangle.getX(), this.tankRectangle.getWidth(), this.tankRectangle.getY(),
				this.tankRectangle.getHeight());
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		// draw Tooltip
		drawTankTooltip(mouseX, mouseY, this.tankRectangle.getX(), this.tankRectangle.getWidth(),
				this.tankRectangle.getY(), this.tankRectangle.getHeight());
	}

	protected void drawTank(int tankPosX, int tankSizeX, int tankPosY, int tankSizeY) {
		if (this.fluidTank != null) {
			final int fillHeight = this.fluidTank.getFluidAmount() * tankSizeY / this.fluidTank.getCapacity();
			final TextureAtlasSprite sprite = this.minecraft.getTextureMap().getSprite(this.waterResource);

			this.minecraft.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
			AbstractGui.blit(getGuiLeft() + tankPosX, getGuiTop() + tankPosY + tankSizeY - fillHeight, 0, tankSizeX,
					fillHeight, sprite);
		}
	}
	protected void drawBattery(Rectangle2d batteryRect) {
		if (this.battery != null) {

			final int fillHeight = this.battery.getEnergyStored() * batteryRect.getHeight()
					/ this.battery.getMaxEnergyStored();

			this.minecraft.getTextureManager()
					.bindTexture(new ResourceLocation("waterworks:textures/blocks/energy_overlay.png"));
			this.blit(getGuiLeft() + batteryRect.getX(),
					getGuiTop() + batteryRect.getY() + batteryRect.getHeight() - fillHeight, 0, 0,
					batteryRect.getWidth(), fillHeight);
		}
	}

	protected void drawTankTooltip(int mouseX, int mouseY, int xStartTank, int xSizeTank, int yStartTank,
			int ySizeTank) {
		if (this.fluidTank != null) {
			final String tooltip = this.fluidTank.getFluidAmount() + "/" + this.fluidTank.getCapacity() + " mB";
			final List<String> toolTipText = new ArrayList<>();
			toolTipText.add(tooltip);
			if (getGuiLeft() + xStartTank <= mouseX && mouseX < getGuiLeft() + xStartTank + xSizeTank
					&& mouseY >= getGuiTop() + yStartTank && mouseY < getGuiTop() + yStartTank + ySizeTank) {
				renderTooltip(toolTipText, mouseX - getGuiLeft() + 10, mouseY - getGuiTop());
			}
		}
	}
	protected void drawBatteryTooltip(int mouseX, int mouseY, Rectangle2d batteryRect) {
		if (this.battery != null) {
			final String tooltip = this.battery.getEnergyStored() + "/" + this.battery.getMaxEnergyStored() + " RF";

			if (getGuiLeft() + batteryRect.getX() <= mouseX
					&& mouseX < getGuiLeft() + batteryRect.getX() + batteryRect.getWidth()
					&& mouseY >= getGuiTop() + batteryRect.getY()
					&& mouseY < getGuiTop() + batteryRect.getY() + batteryRect.getHeight()) {
				final List<String> toolTipText = new ArrayList<>();
				toolTipText.add(tooltip);
				renderTooltip(toolTipText, mouseX - getGuiLeft() + 10, mouseY - getGuiTop());
			}
		}
	}

}
