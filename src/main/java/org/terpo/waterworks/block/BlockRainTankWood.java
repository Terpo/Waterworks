package org.terpo.waterworks.block;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.gui.GuiProxy;
import org.terpo.waterworks.init.WaterworksItems;
import org.terpo.waterworks.tileentity.TileEntityRainTankWood;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRainTankWood extends Block implements ITileEntityProvider {

	public BlockRainTankWood() {
		super(Material.IRON);
		this.setResistance(2F);
		this.setHardness(2F);
		this.setHarvestLevel("pickaxe", 2);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand,
			ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {// isRemote true = client
			final TileEntity tileEntity = getTE(world, pos);
			if (tileEntity instanceof TileEntityRainTankWood) {
				final TileEntityRainTankWood tank = (TileEntityRainTankWood) tileEntity;

				if (heldItem != null) {
					if (heldItem.getItem() == WaterworksItems.iron_mesh) {
						Waterworks.LOGGER.info("Current Tank Storage: " + tank.getDebugInfo());
						return true;
					}
					if (heldItem.getItem() == Items.BUCKET) {
						return true;
					}
					playerIn.openGui(Waterworks.instance, GuiProxy.WATERWORKS_RAINTANK_GUI, world, pos.getX(),
							pos.getY(), pos.getZ());
					return true;

				}
				playerIn.openGui(Waterworks.instance, GuiProxy.WATERWORKS_RAINTANK_GUI, world, pos.getX(), pos.getY(),
						pos.getZ());
				return true;

			}
		}
		return false;
	}
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityRainTankWood();
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState bs) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState bs, World world, BlockPos pos) {
		final TileEntity te = getTE(world, pos);
		if (te instanceof TileEntityRainTankWood) {
			return getTE(world, pos).getComparatorOutput();
		}
		return 0;
	}
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		// final TileEntityRainTankWood tileEntity = getTE(world, pos);
		// InventoryHelper.dropInventoryItems(worldIn, pos, tileEntity); //TODO FIX DROP

		super.breakBlock(world, pos, state);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		// TODO FIX
	}

	private static TileEntityRainTankWood getTE(World world, BlockPos pos) {
		return (TileEntityRainTankWood) world.getTileEntity(pos);
	}
}
