package org.terpo.waterworks.network;

import org.terpo.waterworks.api.constants.WaterworksReference;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint;

public class WaterworksPacketHandler {
	private static int packetId = 0;

	public static SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(WaterworksReference.MODID);

	public WaterworksPacketHandler() {
	}

	public static int nextID() {
		return packetId++;
	}

	public static void registerMessages() {
		// Register messages which are sent from the client to the server here:
		INSTANCE.registerMessage(TankPacket.Handler.class, TankPacket.class, nextID(), Dist.CLIENT);
		INSTANCE.registerMessage(EnergyPacket.Handler.class, EnergyPacket.class, nextID(), Dist.CLIENT);
	}

	public static void sendToAllAround(IMessage message, TileEntity tileEntity, int range) {
		final World world = tileEntity.getWorld();
		final BlockPos blockPos = tileEntity.getPos();
		if (world.isBlockLoaded(blockPos)) {
			INSTANCE.sendToAllAround(message, new TargetPoint(world.provider.getDimension(), blockPos.getX(),
					blockPos.getY(), blockPos.getZ(), range));
		}
	}

	public static void sendToAllAround(IMessage message, TileEntity te) {
		sendToAllAround(message, te, 8);
	}

}
