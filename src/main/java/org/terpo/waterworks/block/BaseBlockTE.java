package org.terpo.waterworks.block;

import org.terpo.waterworks.compat.top.provider.TOPInfoProvider;
import org.terpo.waterworks.fluid.WaterworksTank;
import org.terpo.waterworks.tileentity.BaseTileEntity;
import org.terpo.waterworks.tileentity.TileWaterworks;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

public class BaseBlockTE<T extends BaseTileEntity> extends BaseBlock implements ITileEntityProvider, TOPInfoProvider {

	public BaseBlockTE(Material materialIn) {
		super(materialIn);
	}
	public BaseBlockTE() {
		this(Material.IRON);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return null;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		super.breakBlock(world, pos, state);
	}

	@SuppressWarnings("unchecked")
	protected T getTE(World world, BlockPos pos) {
		final TileEntity tE = world.getTileEntity(pos);
		if (tE instanceof BaseTileEntity) {
			return (T) tE;
		}
		return null;
	}

	@SuppressWarnings("hiding")
	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world,
			IBlockState blockState, IProbeHitData data) {
		final TileEntity te = world.getTileEntity(data.getPos());
		if (te instanceof TileWaterworks) {
			final TileWaterworks tileWaterworks = (TileWaterworks) te;
			final WaterworksTank tank = tileWaterworks.getFluidTank();

			final int capacity = tank.getCapacity();
			final int amount = tank.getFluidAmount();
			probeInfo.horizontal().icon(FluidRegistry.WATER.getStill(), -1, -1, 16, 16)
					.text(amount + "/" + capacity + " mB");
		}
	}

}
