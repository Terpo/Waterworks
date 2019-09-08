package org.terpo.waterworks.proxy;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(Dist.DEDICATED_SERVER)
public class ServerProxy implements IProxy {

	@Override
	public void setup(FMLCommonSetupEvent event) {
		//
	}

	@Override
	public ClientPlayerEntity getClientPlayerEntity() {
		throw new IllegalStateException("Only run this on the client!");
	}

	@Override
	public World getClientWorld() {
		throw new IllegalStateException("Only run this on the client!");
	}

	@Override
	public void init() {
		//
	}
}
