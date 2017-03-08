package org.terpo.waterworks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
		WaterworksConfig.init(event);
		WaterworksConfig.load();
		WaterworksPacketHandler.registerMessages(WaterworksReference.MODID);
		InitItems.init();
		InitBlocks.init();
		InitEntities.init();
		InitModCompat.init("pre");
		proxy.preInit(event);
	}
	@Mod.EventHandler
	public static void init(FMLInitializationEvent event) {
		proxy.init(event);
		InitModCompat.init("init");
		WaterworksCrafting.register();
		InitTileEntities.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiProxy());

	}
	@Mod.EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		WaterworksConfig.save();
		proxy.postInit(event);
	}

}
