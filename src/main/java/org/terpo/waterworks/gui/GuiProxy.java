package org.terpo.waterworks.gui;

//public class GuiProxy implements IGuiHandler {

//	public static final int WATERWORKS_RAINTANK_GUI = 0;
//	public static final int WATERWORKS_GROUNDWATER_PUMP_GUI = 1;
//
//	@Override
//	public Object getServerGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
//		if (ID == WATERWORKS_RAINTANK_GUI) {
//			final TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
//			if (tileEntity instanceof TileWaterworks) {
//				return new ContainerBase(player.inventory, (TileWaterworks) tileEntity);
//			}
//		}
//		if (ID == WATERWORKS_GROUNDWATER_PUMP_GUI) {
//			final TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
//			if (tileEntity instanceof TileWaterworks) {
//				return new PumpContainer(player.inventory, (TileWaterworks) tileEntity);
//			}
//		}
//		return null;
//	}
//
//	@Override
//	public Object getClientGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
//		if (ID == WATERWORKS_RAINTANK_GUI) {
//			final TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
//			if (tileEntity instanceof TileWaterworks) {
//				return new CollectorsContainerScreen(new ContainerBase(player.inventory, (TileWaterworks) tileEntity),
//						(TileWaterworks) tileEntity);
//			}
//		}
//		if (ID == WATERWORKS_GROUNDWATER_PUMP_GUI) {
//			final TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
//			if (tileEntity instanceof TileWaterworks) {
//				return new PumpContainerScreen(new PumpContainer(player.inventory, (TileWaterworks) tileEntity),
//						(TileWaterworks) tileEntity);
//			}
//		}
//		return null;
//	}

//}
