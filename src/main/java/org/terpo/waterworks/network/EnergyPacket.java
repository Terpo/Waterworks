package org.terpo.waterworks.network;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.tileentity.TileEntityGroundwaterPump;
import org.terpo.waterworks.tileentity.TileWaterworks;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class EnergyPacket implements IMessage {

	private TileEntityGroundwaterPump tileEntity = null;
	BlockPos tileEntityPosition = null;
	int energyAmount = 0;
	public EnergyPacket() {
		// nothing 2do
	}

	public EnergyPacket(TileEntityGroundwaterPump tileEntity) {
		this.tileEntity = tileEntity;
		this.tileEntityPosition = this.tileEntity.getPos();
		this.energyAmount = this.tileEntity.getBattery().getEnergyStored();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		// Reads the int back from the buf. Note that if you have multiple values, you
		// must read in the same order you wrote.
		int x, y, z;
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		this.energyAmount = buf.readInt();
		this.tileEntityPosition = new BlockPos(x, y, z);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(this.tileEntityPosition.getX());
		buf.writeInt(this.tileEntityPosition.getY());
		buf.writeInt(this.tileEntityPosition.getZ());
		buf.writeInt(this.energyAmount);
	}

	public BlockPos getPos() {
		return this.tileEntityPosition;
	}

	public static class Handler implements IMessageHandler<EnergyPacket, IMessage> {

		public Handler() {
			// default constructor
		}
		@Override
		public IMessage onMessage(EnergyPacket message, MessageContext ctx) {
			final EntityPlayer player = Waterworks.proxy.getClientEntityPlayer();
			final TileEntityGroundwaterPump pump = getTileEntity(player.world, message.getPos());
			// write new NBT Values
			if (pump != null) {
				pump.getBattery().setEnergyAmount(message.energyAmount);
			}
			return null;
		}

		public static TileEntityGroundwaterPump getTileEntity(World worldObj, BlockPos pos) {
			if (worldObj == null) {
				return null;
			}
			final TileEntity te = worldObj.getTileEntity(pos);
			if (te instanceof TileWaterworks) {
				return (TileEntityGroundwaterPump) te;
			}
			return null;

		}

	}

}
