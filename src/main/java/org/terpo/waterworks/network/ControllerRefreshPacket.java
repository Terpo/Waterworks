package org.terpo.waterworks.network;

import java.util.function.Supplier;

import org.terpo.waterworks.tileentity.TileEntityRainCollectorController;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class ControllerRefreshPacket extends BasePacket {

	public ControllerRefreshPacket() {
		super();
	}

	public ControllerRefreshPacket(TileEntity tileEntity) {
		super(tileEntity);
	}

	public static ControllerRefreshPacket decode(PacketBuffer buf) {
		// Reads the int back from the buf. Note that if you have multiple values, you
		// must read in the same order you wrote.
		final ControllerRefreshPacket pumpPacket = new ControllerRefreshPacket();
		readPosition(buf, pumpPacket);
		return pumpPacket;
	}

	public static void encode(ControllerRefreshPacket packet, PacketBuffer buf) {
		writePosition(packet, buf);
	}

	public static void consume(ControllerRefreshPacket message, Supplier<Context> ctx) {
		ctx.get().enqueueWork(() -> {
			final BlockPos pos = message.tileEntityPosition;

			final TileEntity tileEntity = getTileEntity(ctx.get().getSender().world.getServer().getWorld(message.type),
					pos);
			if (tileEntity instanceof TileEntityRainCollectorController) {
				((TileEntityRainCollectorController) tileEntity).findRainCollectors();
			}

		});
		ctx.get().setPacketHandled(true);
	}
}
