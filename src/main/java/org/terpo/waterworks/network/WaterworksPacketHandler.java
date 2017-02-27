package org.terpo.waterworks.network;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class WaterworksPacketHandler {
	private static int packetId = 0;

	public static SimpleNetworkWrapper INSTANCE = null;

	public WaterworksPacketHandler() {
	}

	public static int nextID() {
		return packetId++;
	}

	public static void registerMessages(String channelName) {
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
		registerMessages();
	}

	public static void registerMessages() {
		// Register messages which are sent from the client to the server here:
		INSTANCE.registerMessage(TankPacket.Handler.class, TankPacket.class, nextID(), Side.CLIENT);
	}

	public static void sendToAllAround(IMessage message, TileEntity tileEntity, int range) {
		final BlockPos blockPos = tileEntity.getPos();
		INSTANCE.sendToAllAround(message, new TargetPoint(tileEntity.getWorld().provider.getDimension(),
				blockPos.getX(), blockPos.getY(), blockPos.getZ(), range));
	}

	public static void sendToAllAround(IMessage message, TileEntity te) {
		sendToAllAround(message, te, 8);
	}

}
