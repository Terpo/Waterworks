package org.terpo.waterworks.proxy;

import org.terpo.waterworks.gui.FluidContainerScreen;
import org.terpo.waterworks.gui.pump.PumpContainerScreen;
import org.terpo.waterworks.init.WaterworksBlocks;
import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.init.WaterworksContainers;
import org.terpo.waterworks.tileentity.TileEntityRainTankWood;
import org.terpo.waterworks.tileentity.specialrenderer.TileEntityWaterRenderer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientProxy implements IProxy {

	// FIXME Custom Model Loading

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

	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		registerBlockRenders();
//		registerEntityRenders();
	}

	// INIT BLOCKS
	public static void registerBlockRenders() {
		if (WaterworksConfig.register.woodenRainTank) {
			registerBlockRender(WaterworksBlocks.rainTankWood);
			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRainTankWood.class, new TileEntityWaterRenderer());

		}
		if (WaterworksConfig.register.waterPipe) {
			registerBlockRender(WaterworksBlocks.waterPipe);
		}
		if (WaterworksConfig.register.rainCollectorMultiblock) {
			registerBlockRender(WaterworksBlocks.rainCollector);
			registerBlockRender(WaterworksBlocks.rainCollectorController);
		}
		if (WaterworksConfig.register.groundwaterPump) {
			registerBlockRender(WaterworksBlocks.groundwaterPump);
		}
	}
	public static void registerBlockRender(Block block) {
//		ModelLoader.setCustomModelResourceLocation(Item.BLOCK_TO_ITEM.get(block), 0,
//				new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}
	/*
	 * INIT ENTITIES
	 */

	@Override
	public void init() {
		ScreenManager.registerFactory(WaterworksContainers.rainTankWood, FluidContainerScreen::new);
		ScreenManager.registerFactory(WaterworksContainers.rainCollectorController, FluidContainerScreen::new);
		ScreenManager.registerFactory(WaterworksContainers.groundwaterPump, PumpContainerScreen::new);
	}

	// FIXME Entity Renderers
//	public static void registerEntityRenders() {
//		RenderingRegistry.registerEntityRenderingHandler(EntityFireworkRocketRain.class,
//				new RenderFireworkRocketRain);
//		RenderingRegistry.registerEntityRenderingHandler(EntityFireworkRocketAntiRain.class,
//				new RenderFireworkRocketAntiRain.Factory());
//	}

}
