package org.terpo.waterworks.proxy;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.gui.GuiProxy;
import org.terpo.waterworks.init.InitBlocks;
import org.terpo.waterworks.init.InitEntities;
import org.terpo.waterworks.init.InitItems;
import org.terpo.waterworks.init.InitModCompat;
import org.terpo.waterworks.init.InitTileEntities;
import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.init.WaterworksCrafting;
import org.terpo.waterworks.network.WaterworksPacketHandler;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityEntry;

@Mod.EventBusSubscriber
public class CommonProxy {
	@SuppressWarnings("static-method")
	public void preInit(FMLPreInitializationEvent event) {
		WaterworksConfig.init(event);
		WaterworksConfig.load();
		WaterworksPacketHandler.registerMessages(WaterworksReference.MODID);
		InitModCompat.init("pre");
	}

	@SuppressWarnings({"static-method", "unused"})
	public void init(FMLInitializationEvent event) {
		InitModCompat.init("init");
		WaterworksCrafting.register();

		NetworkRegistry.INSTANCE.registerGuiHandler(Waterworks.instance, new GuiProxy());
	}

	@SuppressWarnings({"static-method", "unused"})
	public void postInit(FMLPostInitializationEvent event) {
		WaterworksConfig.save();
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		InitBlocks.initBlocks(event);
		InitTileEntities.init();
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		InitBlocks.initItemBlocks(event);
		InitItems.init(event);
	}

	@SubscribeEvent
	public static void registerEntities(final RegistryEvent.Register<EntityEntry> event) {
		InitEntities.init(event);
	}

	@SuppressWarnings("static-method")
	public EntityPlayer getClientEntityPlayer() {
		return null;
	}

}
