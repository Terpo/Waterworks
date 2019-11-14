package org.terpo.waterworks.gui;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.network.ControllerRefreshPacket;
import org.terpo.waterworks.network.WaterworksPacketHandler;

import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class RainCollectorControllerContainerScreen extends FluidContainerScreen {
	private static final ResourceLocation REFRESH_TEXTURE = new ResourceLocation(WaterworksReference.MODID,
			"textures/gui/container/refresh_button.png");

	public RainCollectorControllerContainerScreen(ContainerBase screenContainer, PlayerInventory inv,
			ITextComponent title) {
		super(screenContainer, inv, title);
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		final int xWidth = 24;

		final int xStart = getGuiLeft() + getXSize() - 10 - xWidth;
		final int yStart = 60 + getGuiTop();
		final int yHeight = 18;

		addButton(new ImageButton(xStart, yStart, xWidth, yHeight, 0, 0, 19, REFRESH_TEXTURE,
				button -> WaterworksPacketHandler.INSTANCE
						.sendToServer(new ControllerRefreshPacket(this.container.getTileWaterworks()))));
	}
}
