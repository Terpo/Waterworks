package org.terpo.waterworks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.terpo.waterworks.gui.GuiProxy;
import org.terpo.waterworks.init.InitBlocks;
import org.terpo.waterworks.init.InitItems;
import org.terpo.waterworks.init.InitTileEntities;
import org.terpo.waterworks.init.WaterworksCrafting;
import org.terpo.waterworks.proxy.IProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = WaterworksReference.MODID, name = WaterworksReference.NAME, version = WaterworksReference.VERSION)
public class Waterworks {
	@SidedProxy(clientSide = WaterworksReference.CLIENTPROXY, serverSide = WaterworksReference.SERVERPROXY)
	public static IProxy proxy;

	@Instance
	public static Waterworks instance;

	public static Logger LOGGER = LogManager.getLogger(WaterworksReference.NAME);
	public static final CreativeTabs CREATIVE_TAB = new WaterworksTab();

	@Mod.EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		LOGGER.info("PreInit");
		InitItems.init();
		InitBlocks.init();
		proxy.preInit(event);
	}
	@Mod.EventHandler
	public static void init(FMLInitializationEvent event) {
		LOGGER.info("Init");
		proxy.init(event);

		WaterworksCrafting.register();
		InitTileEntities.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiProxy());

	}
	@Mod.EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		LOGGER.info("PostInit");
		proxy.postInit(event);
	}

}
