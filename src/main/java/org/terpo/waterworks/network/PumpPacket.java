package org.terpo.waterworks.network;

import java.util.function.Supplier;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.energy.WaterworksBattery;
import org.terpo.waterworks.tileentity.TileEntityGroundwaterPump;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PumpPacket extends TankPacket {

	private TileEntityGroundwaterPump tileEntityPump = null;
	int energyAmount = 0;
	public PumpPacket() {
		// nothing 2do
	}

	public PumpPacket(TileEntityGroundwaterPump tileEntity) {
		super(tileEntity);
		this.tileEntityPump = tileEntity;
		this.tileEntityPump.getCapability(CapabilityEnergy.ENERGY)
				.ifPresent(e -> this.energyAmount = ((WaterworksBattery) e).getEnergyStored());
	}

	public static PumpPacket decode(PacketBuffer buf) {
		// Reads the int back from the buf. Note that if you have multiple values, you
		// must read in the same order you wrote.
		final PumpPacket pumpPacket = new PumpPacket();
		readPosition(buf, pumpPacket);
		readTankInformation(buf, pumpPacket);
		pumpPacket.energyAmount = buf.readInt();
		return pumpPacket;
	}

	public static void encode(PumpPacket tankPacket, PacketBuffer buf) {
		// Writes the int into the buf
		writePosition(tankPacket, buf);
		writeTankInformation(tankPacket, buf);
		buf.writeInt(tankPacket.energyAmount);
	}

	public static void consume(PumpPacket message, Supplier<Context> ctx) {
		ctx.get().enqueueWork(() -> {
			final BlockPos pos = message.tileEntityPosition;
			final PlayerEntity clientPlayerEntity = Waterworks.proxy.getClientPlayerEntity();
			if (clientPlayerEntity.world.isAreaLoaded(pos, 0)) {
				final TileEntity tileEntity = getTileEntity(clientPlayerEntity.world, pos);
				if (tileEntity instanceof TileWaterworks) {
					handleTankInformation(message, (TileWaterworks) tileEntity);
				}
				if (tileEntity instanceof TileEntityGroundwaterPump) {
					tileEntity.getCapability(CapabilityEnergy.ENERGY)
							.ifPresent(e -> ((WaterworksBattery) e).setEnergyAmount(message.energyAmount));
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
