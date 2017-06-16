package org.terpo.waterworks.proxy;

import static org.terpo.waterworks.init.WaterworksItems.firework_anti_rain;
import static org.terpo.waterworks.init.WaterworksItems.firework_rain;
import static org.terpo.waterworks.init.WaterworksItems.materials;
import static org.terpo.waterworks.init.WaterworksItems.pipe_wrench;

import org.terpo.waterworks.api.constants.EnumItemMaterials;
import org.terpo.waterworks.entity.item.EntityFireworkRocketAntiRain;
import org.terpo.waterworks.entity.item.EntityFireworkRocketRain;
import org.terpo.waterworks.entity.item.RenderFireworkRocketAntiRain;
import org.terpo.waterworks.entity.item.RenderFireworkRocketRain;
import org.terpo.waterworks.init.WaterworksBlocks;
import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.tileentity.TileEntityRainTankWood;
import org.terpo.waterworks.tileentity.specialrenderer.TileEntityWaterRenderer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements IProxy {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		registerItemRenders();
		registerBlockRenders();
		registerEntityRenders();

	}
	@Override
	public void init(FMLInitializationEvent event) {
		//
	}
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		//
	}
	@Override
	public EntityPlayer getClientEntityPlayer() {
		return Minecraft.getMinecraft().player;
	}

	// INIT BLOCKS
	public static void registerBlockRenders() {
		if (WaterworksConfig.REGISTER_RAIN_TANK) {
			registerBlockRender(WaterworksBlocks.rain_tank_wood);
			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRainTankWood.class, new TileEntityWaterRenderer());

		}
		if (WaterworksConfig.REGISTER_WATER_PIPE) {
			registerBlockRender(WaterworksBlocks.water_pipe);
		}
		if (WaterworksConfig.REGISTER_RAIN_COLLECTING_MULTIBLOCK) {
			registerBlockRender(WaterworksBlocks.rain_collector);
			registerBlockRender(WaterworksBlocks.rain_collector_controller);
		}
		if (WaterworksConfig.REGISTER_GROUNDWATER_PUMP) {
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
		registerItemRender(pipe_wrench);
		if (WaterworksConfig.REGISTER_RAIN_ROCKET) {
			registerItemRender(firework_rain);
		}
		if (WaterworksConfig.REGISTER_ANTI_RAIN_ROCKET) {
			registerItemRender(firework_anti_rain);
		}
		registerItemRenderMaterials(materials);
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
