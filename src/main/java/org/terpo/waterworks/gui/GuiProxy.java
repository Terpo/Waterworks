package org.terpo.waterworks.gui;

import org.terpo.waterworks.gui.collector.GuiCollectorsContainer;
import org.terpo.waterworks.gui.pump.GuiPumpContainer;
import org.terpo.waterworks.gui.pump.PumpContainer;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

	public static final int WATERWORKS_RAINTANK_GUI = 0;
	public static final int WATERWORKS_GROUNDWATER_PUMP_GUI = 1;

	@Override
	public Object getServerGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
		if (ID == WATERWORKS_RAINTANK_GUI) {
			final TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity instanceof TileWaterworks) {
				return new ContainerBase(player.inventory, (TileWaterworks) tileEntity);
			}
		}
		if (ID == WATERWORKS_GROUNDWATER_PUMP_GUI) {
			final TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity instanceof TileWaterworks) {
				return new PumpContainer(player.inventory, (TileWaterworks) tileEntity);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
		if (ID == WATERWORKS_RAINTANK_GUI) {
			final TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity instanceof TileWaterworks) {
				return new GuiCollectorsContainer(new ContainerBase(player.inventory, (TileWaterworks) tileEntity),
						(TileWaterworks) tileEntity);
			}
		}
		if (ID == WATERWORKS_GROUNDWATER_PUMP_GUI) {
			final TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
			if (tileEntity instanceof TileWaterworks) {
				return new GuiPumpContainer(new PumpContainer(player.inventory, (TileWaterworks) tileEntity),
						(TileWaterworks) tileEntity);
			}
		}
		return null;
	}

}
