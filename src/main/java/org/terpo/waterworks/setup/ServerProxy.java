package org.terpo.waterworks.setup;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.DEDICATED_SERVER)
public class ServerProxy implements IProxy {

	@Override
	public PlayerEntity getClientPlayerEntity() {
		throw new IllegalStateException("Only run this on the client!");
	}

	@Override
	public World getClientWorld() {
		throw new IllegalStateException("Only run this on the client!");
	}
}
