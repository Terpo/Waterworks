package org.terpo.waterworks.proxy;

import org.terpo.waterworks.init.InitBlocks;
import org.terpo.waterworks.init.InitItems;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements IProxy {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		InitItems.registerRenders();
		InitBlocks.registerRenders();
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
		return Minecraft.getMinecraft().thePlayer;
	}
}
