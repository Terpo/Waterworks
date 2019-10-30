package org.terpo.waterworks.network;

import java.util.function.Supplier;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class TankPacket extends BasePacket {

	private TileWaterworks tileEntity = null;

	int fluidAmount = 0;
	public TankPacket() {
		// nothing 2do
	}

	public TankPacket(TileWaterworks tileEntity) {
		super(tileEntity);
		this.tileEntity = tileEntity;
		this.fluidAmount = this.tileEntity.getFluidTank().getFluidAmount();
	}

	public static TankPacket decode(PacketBuffer buf) {
		// Reads the int back from the buf. Note that if you have multiple values, you
		// must read in the same order you wrote.
		final TankPacket tankPacket = new TankPacket();
		readPosition(buf, tankPacket);
		readTankInformation(buf, tankPacket);
		return tankPacket;
	}

	protected static void readTankInformation(PacketBuffer buf, final TankPacket tankPacket) {
		tankPacket.fluidAmount = buf.readInt();
	}

	public static void encode(TankPacket tankPacket, PacketBuffer buf) {
		// Writes the int into the buf
		writePosition(tankPacket, buf);
		writeTankInformation(tankPacket, buf);
	}

	protected static void writeTankInformation(TankPacket tankPacket, PacketBuffer buf) {
		buf.writeInt(tankPacket.fluidAmount);
	}

	public static void consume(TankPacket message, Supplier<Context> ctx) {
		ctx.get().enqueueWork(() -> {
			final BlockPos pos = message.tileEntityPosition;
			final PlayerEntity clientPlayerEntity = Waterworks.proxy.getClientPlayerEntity();
			if (clientPlayerEntity.world.isAreaLoaded(pos, 0)) {
				final TileEntity tileEntity = getTileEntity(clientPlayerEntity.world, pos);
				if (tileEntity instanceof TileWaterworks) {
					handleTankInformation(message, (TileWaterworks) tileEntity);
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}

	protected static void handleTankInformation(TankPacket message, final TileWaterworks tileEntity) {
		// write new NBT Values
		tileEntity.getFluidTank()
				.setFluid(new FluidStack(Fluids.WATER, message.fluidAmount > 0 ? message.fluidAmount : 0));
	}
}
