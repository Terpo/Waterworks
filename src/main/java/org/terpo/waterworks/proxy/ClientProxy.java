package org.terpo.waterworks.proxy;

import org.terpo.waterworks.entity.item.RenderWeatherFireworkRocket;
import org.terpo.waterworks.gui.FluidContainerScreen;
import org.terpo.waterworks.gui.RainCollectorControllerContainerScreen;
import org.terpo.waterworks.gui.pump.PumpContainerScreen;
import org.terpo.waterworks.init.WaterworksContainers;
import org.terpo.waterworks.init.WaterworksEntities;
import org.terpo.waterworks.tileentity.TileEntityRainTankWood;
import org.terpo.waterworks.tileentity.specialrenderer.TileEntityWaterRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientProxy implements IProxy {

	@Override
	public void setup(FMLCommonSetupEvent event) {
		//
	}

	@Override
	public World getClientWorld() {
		return Minecraft.getInstance().world;
	}

	@Override
	public ClientPlayerEntity getClientPlayerEntity() {
		return Minecraft.getInstance().player;
	}

	@Override
	public void init() {
		// GUI
		ScreenManager.registerFactory(WaterworksContainers.rainTankWood, FluidContainerScreen::new);
		ScreenManager.registerFactory(WaterworksContainers.rainCollectorController,
				RainCollectorControllerContainerScreen::new);
		ScreenManager.registerFactory(WaterworksContainers.groundwaterPump, PumpContainerScreen::new);

		// ENTITY RENDERER
		RenderingRegistry.registerEntityRenderingHandler(WaterworksEntities.entityFireworkAntiRain,
				RenderWeatherFireworkRocket::new);
		RenderingRegistry.registerEntityRenderingHandler(WaterworksEntities.entityFireworkRain,
				RenderWeatherFireworkRocket::new);

		// TESR
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRainTankWood.class, new TileEntityWaterRenderer());
	}
}
