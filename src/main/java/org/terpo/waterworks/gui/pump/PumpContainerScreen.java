package org.terpo.waterworks.gui.pump;

import java.util.ArrayList;
import java.util.List;

import org.terpo.waterworks.gui.ContainerBase;
import org.terpo.waterworks.gui.FluidContainerScreen;

import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.energy.CapabilityEnergy;

@OnlyIn(Dist.CLIENT)
public class PumpContainerScreen extends FluidContainerScreen {
	protected Rectangle2d batteryRectangle;

	public PumpContainerScreen(ContainerBase screenContainer, PlayerInventory inv, ITextComponent title) {
		super(screenContainer, inv, title);
	}

	@Override
	protected void prepare(ResourceLocation newGui, Rectangle2d newTankRectangle, ContainerBase screenContainer) {
		super.prepare(new ResourceLocation("waterworks:textures/gui/container/groundwater_pump.png"), new Rectangle2d(80, 17, 16, 52),
				screenContainer);
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

	protected void drawBattery(Rectangle2d batteryRect) {

		if (this.tileEntity != null) {
			this.tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(battery -> {
				final int fillHeight = battery.getEnergyStored() * batteryRect.getHeight() / battery.getMaxEnergyStored();

				this.minecraft.getTextureManager().bindTexture(new ResourceLocation("waterworks:textures/blocks/energy_overlay.png"));
				this.blit(getGuiLeft() + batteryRect.getX(), getGuiTop() + batteryRect.getY() + batteryRect.getHeight() - fillHeight, 0, 0,
						batteryRect.getWidth(), fillHeight);
			});

		}
	}

	protected void drawBatteryTooltip(int mouseX, int mouseY, Rectangle2d batteryRect) {
		if (this.tileEntity != null) {
			this.tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(battery -> {
				final String tooltip = battery.getEnergyStored() + "/" + battery.getMaxEnergyStored() + " RF";
				if (getGuiLeft() + batteryRect.getX() <= mouseX && mouseX < getGuiLeft() + batteryRect.getX() + batteryRect.getWidth()
						&& mouseY >= getGuiTop() + batteryRect.getY()
						&& mouseY < getGuiTop() + batteryRect.getY() + batteryRect.getHeight()) {
					final List<String> toolTipText = new ArrayList<>();
					toolTipText.add(tooltip);
					renderTooltip(toolTipText, mouseX - getGuiLeft() + 10, mouseY - getGuiTop());
				}
			});
		}
	}
}
