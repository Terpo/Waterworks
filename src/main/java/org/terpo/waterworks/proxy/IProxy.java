package org.terpo.waterworks.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IProxy {
	/**
	 * @param event PreInit Event
	 */
	public void preInit(FMLPreInitializationEvent event);

	/**
	 * @param event Init Event
	 */
	public void init(FMLInitializationEvent event);

	/**
	 * @param event PostInit Event
	 */
	public void postInit(FMLPostInitializationEvent event);

}
