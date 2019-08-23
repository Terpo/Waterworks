package org.terpo.waterworks.proxy;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public interface IProxy {
	public void setup(FMLCommonSetupEvent event);

	public ClientPlayerEntity getClientPlayerEntity();

	public World getClientWorld();
}
