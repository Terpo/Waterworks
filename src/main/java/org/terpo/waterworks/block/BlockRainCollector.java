package org.terpo.waterworks.block;

import java.util.List;

import org.terpo.waterworks.tileentity.BaseTileEntity;
import org.terpo.waterworks.tileentity.TileEntityRainCollector;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRainCollector extends BaseBlockTE<BaseTileEntity> {

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, player, tooltip, advanced);
		tooltip.add("Used to increase size of multiblock rain collector");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityRainCollector();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote && hand == EnumHand.MAIN_HAND) {// isRemote true = client
			final TileEntity tileEntity = getTE(worldIn, pos);
			if (tileEntity instanceof TileEntityRainCollector) {
				final ItemStack heldItem = playerIn.getHeldItem(hand);
				if (heldItem.getItem() == Items.STICK) {
//					Waterworks.LOGGER.info("Has controller: " + ((TileEntityRainCollector) tileEntity).hasController());
				}
			}
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		final TileEntity tileEntity = getTE(world, pos);
		if (tileEntity instanceof TileEntityRainCollector) {
			((TileEntityRainCollector) tileEntity).informAboutBlockBreak();
		}
		super.breakBlock(world, pos, state);
	}
	@Override
	public boolean hasTileEntity() {
		return true;
	}
}
