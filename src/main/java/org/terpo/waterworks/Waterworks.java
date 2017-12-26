package org.terpo.waterworks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = WaterworksReference.MODID, name = WaterworksReference.NAME, version = WaterworksReference.VERSION)
public class Waterworks {
	@SidedProxy(clientSide = WaterworksReference.CLIENTPROXY, serverSide = WaterworksReference.SERVERPROXY)
	public static CommonProxy proxy;

	@Instance
	public static Waterworks instance;

	public static Logger LOGGER = LogManager.getLogger(WaterworksReference.NAME);
	public static final CreativeTabs CREATIVE_TAB = new WaterworksTab();

	@Mod.EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}
	@Mod.EventHandler
	public static void init(FMLInitializationEvent event) {
		proxy.init(event);
	}
	@Mod.EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

}
