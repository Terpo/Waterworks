package org.terpo.waterworks.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class BasePacket {
	BlockPos tileEntityPosition = null;
	RegistryKey<World> type;

	public BasePacket() {
		// nothing 2do
	}
	public BasePacket(TileEntity tileEntity) {
		this.tileEntityPosition = tileEntity.getPos();
		this.type = tileEntity.getWorld().getRegistryKey();

	}

	protected static void readPosition(PacketBuffer buf, final BasePacket packet) {
		final int x = buf.readInt();
		final int y = buf.readInt();
		final int z = buf.readInt();
		packet.tileEntityPosition = new BlockPos(x, y, z);
		packet.type = RegistryKey.of(Registry.field_239699_ae_,buf.readResourceLocation());
	}

	protected static void writePosition(BasePacket packet, PacketBuffer buf) {
		buf.writeInt(packet.tileEntityPosition.getX());
		buf.writeInt(packet.tileEntityPosition.getY());
		buf.writeInt(packet.tileEntityPosition.getZ());
		buf.writeResourceLocation(packet.type.getValue());
	}

	protected static TileEntity getTileEntity(World worldObj, BlockPos pos) {
		if (worldObj == null) {
			return null;
		}
		return worldObj.getTileEntity(pos);
	}
}
