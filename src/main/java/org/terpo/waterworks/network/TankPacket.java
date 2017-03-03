package org.terpo.waterworks.network;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.tileentity.TileEntityRainTankWood;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TankPacket implements IMessage {

	private TileEntityRainTankWood tileEntity = null;
	BlockPos tileEntityPosition = null;
	int fluidAmount = 0;
	public TankPacket() {
		// nothing 2do
	}

	public TankPacket(TileEntityRainTankWood tileEntity) {
		this.tileEntity = tileEntity;
		this.tileEntityPosition = this.tileEntity.getPos();
		this.fluidAmount = this.tileEntity.getFluidTank().getFluidAmount();
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
		this.tileEntityPosition = new BlockPos(x, y, z);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(this.tileEntityPosition.getX());
		buf.writeInt(this.tileEntityPosition.getY());
		buf.writeInt(this.tileEntityPosition.getZ());
		buf.writeInt(this.fluidAmount);
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
			final TileEntityRainTankWood tileEntity = getTileEntity(player.world, message.getPos());
			if (tileEntity == null) {
				return null;
			}
			// write new NBT Values
			if (message.fluidAmount > 0) {
				tileEntity.getFluidTank().setFluid(new FluidStack(FluidRegistry.WATER, message.fluidAmount));
			} else {
				tileEntity.getFluidTank().setFluid(null);
			}
			return null;
		}

		public static TileEntityRainTankWood getTileEntity(World worldObj, BlockPos pos) {
			if (worldObj == null) {
				return null;
			}
			final TileEntity te = worldObj.getTileEntity(pos);
			if (te == null) {
				return null;
			}
			if (te instanceof TileEntityRainTankWood) {
				return (TileEntityRainTankWood) te;
			}
			return null;

		}

	}

}
