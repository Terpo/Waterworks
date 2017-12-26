package org.terpo.waterworks.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.SERVER)
public class ServerProxy extends CommonProxy {

	/**
	 * @param item sided Item
	 */
	public void registerItemSided(Item item) {
		//
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		//
	}

	@Override
	public void init(FMLInitializationEvent event) {
		//
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		//
	}
}
