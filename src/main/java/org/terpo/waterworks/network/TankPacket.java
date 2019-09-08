package org.terpo.waterworks.network;

//public class TankPacket implements IMessage {
//
//	private TileWaterworks tileEntity = null;
//	BlockPos tileEntityPosition = null;
//	int fluidAmount = 0;
//	public TankPacket() {
//		// nothing 2do
//	}
//
//	public TankPacket(TileWaterworks tileEntity) {
//		this.tileEntity = tileEntity;
//		this.tileEntityPosition = this.tileEntity.getPos();
//		this.fluidAmount = this.tileEntity.getFluidTank().getFluidAmount();
//	}
//
//	@Override
//	public void fromBytes(ByteBuf buf) {
//		// Reads the int back from the buf. Note that if you have multiple values, you
//		// must read in the same order you wrote.
//		int x, y, z;
//		x = buf.readInt();
//		y = buf.readInt();
//		z = buf.readInt();
//		this.fluidAmount = buf.readInt();
//		this.tileEntityPosition = new BlockPos(x, y, z);
//	}
//
//	@Override
//	public void toBytes(ByteBuf buf) {
//		// Writes the int into the buf
//		buf.writeInt(this.tileEntityPosition.getX());
//		buf.writeInt(this.tileEntityPosition.getY());
//		buf.writeInt(this.tileEntityPosition.getZ());
//		buf.writeInt(this.fluidAmount);
//	}
//
//	public BlockPos getPos() {
//		return this.tileEntityPosition;
//	}
//
//	public static class Handler implements IMessageHandler<TankPacket, IMessage> {
//
//		public Handler() {
//			// default constructor
//		}
//		@Override
//		public IMessage onMessage(TankPacket message, MessageContext ctx) {
//			final PlayerEntity player = Waterworks.proxy.getClientPlayerEntity();
//			final TileWaterworks tileEntity = getTileEntity(player.world, message.getPos());
//			if (tileEntity == null) {
//				return null;
//			}
//			// write new NBT Values
//			if (message.fluidAmount > 0) {
//				tileEntity.getFluidTank().setFluid(new FluidStack(FluidRegistry.WATER, message.fluidAmount));
//			} else {
//				tileEntity.getFluidTank().setFluid(null);
//			}
//			return null;
//		}
//
//		public static TileWaterworks getTileEntity(World worldObj, BlockPos pos) {
//			if (worldObj == null) {
//				return null;
//			}
//			final TileEntity te = worldObj.getTileEntity(pos);
//
//			if (te instanceof TileWaterworks) {
//				return (TileWaterworks) te;
//			}
//			return null;
//
//		}
//
//	}
//
//}
