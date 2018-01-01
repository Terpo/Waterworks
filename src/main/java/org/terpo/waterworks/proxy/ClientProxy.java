package org.terpo.waterworks.proxy;

import org.terpo.waterworks.api.constants.EnumItemMaterials;
import org.terpo.waterworks.entity.item.EntityFireworkRocketAntiRain;
import org.terpo.waterworks.entity.item.EntityFireworkRocketRain;
import org.terpo.waterworks.entity.item.RenderFireworkRocketAntiRain;
import org.terpo.waterworks.entity.item.RenderFireworkRocketRain;
import org.terpo.waterworks.init.WaterworksBlocks;
import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.init.WaterworksItems;
import org.terpo.waterworks.tileentity.TileEntityRainTankWood;
import org.terpo.waterworks.tileentity.specialrenderer.TileEntityWaterRenderer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

	}
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);

	}
	@Override
	public EntityPlayer getClientEntityPlayer() {
		return Minecraft.getMinecraft().player;
	}

	@SuppressWarnings("unused")
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		registerItemRenders();
		registerBlockRenders();
		registerEntityRenders();
	}

	// INIT BLOCKS
	public static void registerBlockRenders() {
		if (WaterworksConfig.register.woodenRainTank) {
			registerBlockRender(WaterworksBlocks.rain_tank_wood);
			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRainTankWood.class, new TileEntityWaterRenderer());

		}
		if (WaterworksConfig.register.waterPipe) {
			registerBlockRender(WaterworksBlocks.water_pipe);
		}
		if (WaterworksConfig.register.rainCollectorMultiblock) {
			registerBlockRender(WaterworksBlocks.rain_collector);
			registerBlockRender(WaterworksBlocks.rain_collector_controller);
		}
		if (WaterworksConfig.register.groundwaterPump) {
			registerBlockRender(WaterworksBlocks.groundwater_pump);
		}
	}
	public static void registerBlockRender(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
				new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}

	/*
	 * INIT ITEMS
	 */
	public static void registerItemRenders() {
		registerItemRender(WaterworksItems.pipe_wrench);
		if (WaterworksConfig.register.rainRocket) {
			registerItemRender(WaterworksItems.firework_rain);
		}
		if (WaterworksConfig.register.antiRainRocket) {
			registerItemRender(WaterworksItems.firework_anti_rain);
		}
		registerItemRenderMaterials(WaterworksItems.materials);
	}

	public static void registerItemRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0,
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	public static void registerItemRenderMaterials(Item item) {
		for (int i = 0; i < EnumItemMaterials.VALUES.length; i++) {
			final ResourceLocation loc = item.getRegistryName();
			ModelLoader.setCustomModelResourceLocation(item, i,
					new ModelResourceLocation(loc + "_" + EnumItemMaterials.VALUES[i], "inventory"));
		}
	}
	/*
	 * INIT ENTITIES
	 */

	public static void registerEntityRenders() {
		RenderingRegistry.registerEntityRenderingHandler(EntityFireworkRocketRain.class,
				new RenderFireworkRocketRain.Factory());
		RenderingRegistry.registerEntityRenderingHandler(EntityFireworkRocketAntiRain.class,
				new RenderFireworkRocketAntiRain.Factory());
	}

}
