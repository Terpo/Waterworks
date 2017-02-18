package org.terpo.waterworks.gui;

import org.terpo.waterworks.tileentity.TileEntityRainTankWood;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

	public static final int WATERWORKS_RAINTANK_GUI = 0;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == WATERWORKS_RAINTANK_GUI) {
			return new ContainerBase(player.inventory,
					(TileEntityRainTankWood) world.getTileEntity(new BlockPos(x, y, z)));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == WATERWORKS_RAINTANK_GUI) {
			final TileEntityRainTankWood te = (TileEntityRainTankWood) world.getTileEntity(new BlockPos(x, y, z));
			return new GuiFluidContainer(new ContainerBase(player.inventory, te), te);
		}
		return null;
	}

}
