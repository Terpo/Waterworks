package org.terpo.waterworks.network;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.helper.FluidHelper;
import org.terpo.waterworks.tileentity.TileWaterworks;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TankPacket implements IMessage {

	private TileWaterworks tileEntity = null;
	BlockPos tileEntityPosition = null;
	int fluidAmount = 0;
	String fluidName = null;
	public TankPacket() {
		// nothing 2do
	}

	public TankPacket(TileWaterworks tileEntity) {
		this.tileEntity = tileEntity;
		this.tileEntityPosition = this.tileEntity.getPos();
		this.fluidAmount = this.tileEntity.getFluidTank().getFluidAmount();
		final FluidStack fluidStack = tileEntity.getFluidTank().getFluid();
		if (this.fluidAmount > 0 && fluidStack != null && fluidStack.getFluid() != null) {
			this.fluidName = fluidStack.getFluid().getName();
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		// Reads the int back from the buf. Note that if you have multiple values, you
		// must read in the same order you wrote.
		int x, y, z;
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		this.fluidAmount = buf.readInt();
		if (this.fluidAmount > 0) {
			this.fluidName = ByteBufUtils.readUTF8String(buf);
		}
		this.tileEntityPosition = new BlockPos(x, y, z);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(this.tileEntityPosition.getX());
		buf.writeInt(this.tileEntityPosition.getY());
		buf.writeInt(this.tileEntityPosition.getZ());
		buf.writeInt(this.fluidAmount);
		if (this.fluidAmount > 0) {
			ByteBufUtils.writeUTF8String(buf, this.fluidName);
		}
	}

	public BlockPos getPos() {
		return this.tileEntityPosition;
	}

	public static class Handler implements IMessageHandler<TankPacket, IMessage> {

		public Handler() {
			// default constructor
		}
		@Override
		public IMessage onMessage(TankPacket message, MessageContext ctx) {
			final EntityPlayer player = Waterworks.proxy.getClientEntityPlayer();
			final TileWaterworks tileEntity = getTileEntity(player.world, message.getPos());
			if (tileEntity == null) {
				return null;
			}
			// write new NBT Values
			if (message.fluidAmount > 0) {
				tileEntity.getFluidTank()
						.setFluid(FluidHelper.getFluidResource(message.fluidName, message.fluidAmount));
			} else {
				tileEntity.getFluidTank().setFluid(null);
			}
			return null;
		}

		public static TileWaterworks getTileEntity(World worldObj, BlockPos pos) {
			if (worldObj == null) {
				return null;
			}
			final TileEntity te = worldObj.getTileEntity(pos);

			if (te instanceof TileWaterworks) {
				return (TileWaterworks) te;
			}
			return null;

		}

	}

}
