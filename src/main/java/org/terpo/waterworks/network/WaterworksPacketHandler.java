package org.terpo.waterworks.network;

import org.terpo.waterworks.api.constants.WaterworksReference;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class WaterworksPacketHandler {
	private static int packetId = 0;

	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(WaterworksReference.MODID, WaterworksReference.NETWORK), () -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

	private WaterworksPacketHandler() {
		// hide me
	}

	public static void registerMessages() {
		INSTANCE.registerMessage(packetId++, TankPacket.class, TankPacket::encode, TankPacket::decode,
				TankPacket::consume);
		INSTANCE.registerMessage(packetId++, PumpPacket.class, PumpPacket::encode, PumpPacket::decode,
				PumpPacket::consume);
		INSTANCE.registerMessage(packetId++, ControllerRefreshPacket.class, ControllerRefreshPacket::encode,
				ControllerRefreshPacket::decode, ControllerRefreshPacket::consume);
	}

	public static void sendToAllAround(BasePacket packet, TileEntity tE, int range) {
		final BlockPos blockPos = tE.getPos();
		if (tE.getWorld().isAreaLoaded(blockPos, 0)) {
			INSTANCE.send(PacketDistributor.NEAR.with(() -> new TargetPoint(blockPos.getX(), blockPos.getY(),
					blockPos.getZ(), range, tE.getWorld().dimension.getType())), packet);
		}
	}

	public static void sendToAllAround(BasePacket packet, TileEntity tE) {
		sendToAllAround(packet, tE, 8);
	}

}
