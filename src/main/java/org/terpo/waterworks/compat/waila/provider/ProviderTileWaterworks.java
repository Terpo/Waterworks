package org.terpo.waterworks.compat.waila.provider;

//TODO restore Waila Compatibility
//public class ProviderTileWaterworks implements IServerDataProvider<TileEntity> {
//
//	@Override
//	public void appendServerData(CompoundNBT nbt, ServerPlayerEntity player, World world, TileEntity tile) {
//		
//
//	}
//
//	@Override
//	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
//		return null;
//	}
//
//	@Override
//	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
//			IWailaConfigHandler config) {
//		return null;
//	}
//
//	@Override
//	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
//			IWailaConfigHandler config) {
//
//		if (!config.getConfig(Compat.WAILA_CONFIG_TANKINFO)) {
//			return currenttip;
//		}
//		if (accessor.getBlock() instanceof BaseBlockTE) {
//			final TileEntity tileEntity = accessor.getTileEntity();
//			TileWaterworks tileWaterworks;
//			if (tileEntity instanceof TileWaterworks) {
//				tileWaterworks = (TileWaterworks) tileEntity;
//				final WaterworksTank tank = tileWaterworks.getFluidTank();
//				final int capacity = tank.getCapacity();
//				final int amount = tank.getFluidAmount();
//				currenttip.add(amount + "/" + capacity + " mB");
//			}
//			if (tileEntity instanceof TileEntityGroundwaterPump) {
//				final TileEntityGroundwaterPump pump = (TileEntityGroundwaterPump) tileEntity;
//				final WaterworksBattery battery = pump.getBattery();
//
//				final int capacity = battery.getMaxEnergyStored();
//				final int amount = battery.getEnergyStored();
//				currenttip.add(amount + "/" + capacity + " RF");
//			}
//		}
//		return currenttip;
//	}
//
//	@Override
//	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
//			IWailaConfigHandler config) {
//		return null;
//	}
//
//	@Override
//	public CompoundNBT getNBTData(PlayerEntityMP player, TileEntity te, CompoundNBT tag, World world,
//			BlockPos pos) {
//		if (te != null) {
//			te.writeToNBT(tag);
//		}
//		return tag;
//	}

//}
