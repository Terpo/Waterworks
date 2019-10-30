package org.terpo.waterworks.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public interface IProxy {

	public void init();
	public void setup(FMLCommonSetupEvent event);

	public PlayerEntity getClientPlayerEntity();

	public World getClientWorld();
}
