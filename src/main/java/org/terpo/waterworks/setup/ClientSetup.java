package org.terpo.waterworks.setup;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.api.constants.Reference;
import org.terpo.waterworks.entity.item.RenderWeatherFireworkRocket;
import org.terpo.waterworks.gui.FluidContainerScreen;
import org.terpo.waterworks.gui.RainCollectorControllerContainerScreen;
import org.terpo.waterworks.gui.pump.PumpContainerScreen;
import org.terpo.waterworks.tileentity.specialrenderer.TileEntityWaterRenderer;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Reference.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

	public static void init(@SuppressWarnings("unused") final FMLClientSetupEvent event) { // NOSONAR
		Waterworks.LOGGER.info("Waterworks Client Setup starting");
		// GUI
		ScreenManager.registerFactory(Registration.rainTankWoodContainer.get(), FluidContainerScreen::new);
		ScreenManager.registerFactory(Registration.rainCollectorControllerContainer.get(), RainCollectorControllerContainerScreen::new);
		ScreenManager.registerFactory(Registration.groundwaterPumpContainer.get(), PumpContainerScreen::new);

		// ENTITY RENDERER
		RenderingRegistry.registerEntityRenderingHandler(Registration.entityFireworkAntiRain.get(), RenderWeatherFireworkRocket::new);
		RenderingRegistry.registerEntityRenderingHandler(Registration.entityFireworkRain.get(), RenderWeatherFireworkRocket::new);

		// TESR
		ClientRegistry.bindTileEntityRenderer(Registration.rainTankWoodTile.get(), TileEntityWaterRenderer::new);
		Waterworks.LOGGER.info("Waterworks Client Setup complete");
	}

	private ClientSetup() {
		// hidden
	}
}
