package org.terpo.waterworks.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class BasePacket {
	BlockPos tileEntityPosition = null;
	DimensionType type;

	public BasePacket() {
		// nothing 2do
	}
	public BasePacket(TileEntity tileEntity) {
		this.tileEntityPosition = tileEntity.getPos();
		this.type = tileEntity.getWorld().getDimension().getType();

	}

	protected static void readPosition(PacketBuffer buf, final BasePacket packet) {
		final int x = buf.readInt();
		final int y = buf.readInt();
		final int z = buf.readInt();
		packet.tileEntityPosition = new BlockPos(x, y, z);
		packet.type = DimensionType.getById(buf.readInt());
	}

	protected static void writePosition(BasePacket packet, PacketBuffer buf) {
		buf.writeInt(packet.tileEntityPosition.getX());
		buf.writeInt(packet.tileEntityPosition.getY());
		buf.writeInt(packet.tileEntityPosition.getZ());
		buf.writeInt(packet.type.getId());
	}

	protected static TileEntity getTileEntity(World worldObj, BlockPos pos) {
		if (worldObj == null) {
			return null;
		}
		return worldObj.getTileEntity(pos);
	}
}
