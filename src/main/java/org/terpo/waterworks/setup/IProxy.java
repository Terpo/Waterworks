package org.terpo.waterworks.setup;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface IProxy {

	public PlayerEntity getClientPlayerEntity();

	public World getClientWorld();
}
