package org.terpo.waterworks.compat.waila.provider;

import java.util.List;

import org.terpo.waterworks.api.constants.Compat;
import org.terpo.waterworks.block.BaseBlockTE;
import org.terpo.waterworks.energy.WaterworksBattery;
import org.terpo.waterworks.fluid.WaterworksTank;
import org.terpo.waterworks.tileentity.TileEntityGroundwaterPump;
import org.terpo.waterworks.tileentity.TileWaterworks;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ProviderTileWaterworks implements IWailaDataProvider {

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {

		if (!config.getConfig(Compat.WAILA_CONFIG_TANKINFO)) {
			return currenttip;
		}
		if (accessor.getBlock() instanceof BaseBlockTE) {
			final TileEntity tileEntity = accessor.getTileEntity();
			TileWaterworks tileWaterworks;
			if (tileEntity instanceof TileWaterworks) {
				tileWaterworks = (TileWaterworks) tileEntity;
				final WaterworksTank tank = tileWaterworks.getFluidTank();
				final int capacity = tank.getCapacity();
				final int amount = tank.getFluidAmount();
				currenttip.add(amount + "/" + capacity + " mB");
			}
			if (tileEntity instanceof TileEntityGroundwaterPump) {
				final TileEntityGroundwaterPump pump = (TileEntityGroundwaterPump) tileEntity;
				final WaterworksBattery battery = pump.getBattery();

				final int capacity = battery.getMaxEnergyStored();
				final int amount = battery.getEnergyStored();
				currenttip.add(amount + "/" + capacity + " RF");
			}
		}
		return currenttip;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		return null;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
			BlockPos pos) {
		if (te != null) {
			te.writeToNBT(tag);
		}
		return tag;
	}

}
