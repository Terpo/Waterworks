package org.terpo.waterworks.proxy;

import org.terpo.waterworks.api.constants.EnumItemMaterials;
import org.terpo.waterworks.gui.BaseContainerScreen;
import org.terpo.waterworks.init.WaterworksBlocks;
import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.init.WaterworksContainers;
import org.terpo.waterworks.init.WaterworksItems;
import org.terpo.waterworks.tileentity.TileEntityRainTankWood;
import org.terpo.waterworks.tileentity.specialrenderer.TileEntityWaterRenderer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
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
		registerItemRenders();
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
	 * INIT ITEMS
	 */
	public static void registerItemRenders() {
		registerItemRender(WaterworksItems.itemPipeWrench);
		if (WaterworksConfig.register.rainRocket) {
			registerItemRender(WaterworksItems.itemFireworkRain);
		}
		if (WaterworksConfig.register.antiRainRocket) {
			registerItemRender(WaterworksItems.itemFireworkAntiRain);
		}
		registerItemRenderMaterials(WaterworksItems.itemMaterials);
	}

	public static void registerItemRender(Item item) {
//		ModelLoader.setCustomModelResourceLocation(item, 0,
//				new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	public static void registerItemRenderMaterials(Item item) {
		for (int i = 0; i < EnumItemMaterials.VALUES.length; i++) {
			final ResourceLocation loc = item.getRegistryName();
//			ModelLoader.setCustomModelResourceLocation(item, i,
//					new ModelResourceLocation(loc + "_" + EnumItemMaterials.VALUES[i], "inventory"));
		}
	}
	/*
	 * INIT ENTITIES
	 */

	@Override
	public void init() {
		ScreenManager.registerFactory(WaterworksContainers.waterworks, BaseContainerScreen::new);
//		ScreenManager.registerFactory(WaterworksContainers.groundwaterPump, PumpContainerScreen::new);
//		ScreenManager.registerFactory(WaterworksContainers.groundwaterPump, PumpContainerScreen::new);
	}

	// FIXME Entity Renderers
//	public static void registerEntityRenders() {
//		RenderingRegistry.registerEntityRenderingHandler(EntityFireworkRocketRain.class,
//				new RenderFireworkRocketRain);
//		RenderingRegistry.registerEntityRenderingHandler(EntityFireworkRocketAntiRain.class,
//				new RenderFireworkRocketAntiRain.Factory());
//	}

}
