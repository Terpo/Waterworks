package org.terpo.waterworks.block;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.gui.GuiProxy;
import org.terpo.waterworks.inventory.WaterworksInventoryHelper;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockRainTankWood extends Block implements ITileEntityProvider {

	public BlockRainTankWood() {
		super(Material.IRON);
		this.setResistance(2F);
		this.setHardness(2F);
		this.setHarvestLevel("pickaxe", 2);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (!worldIn.isRemote && hand == EnumHand.MAIN_HAND) {// isRemote true = client
			final TileEntity tileEntity = getTE(worldIn, pos);
			if (tileEntity instanceof TileEntityRainTankWood) {
				final ItemStack heldItem = playerIn.getHeldItem(hand);
				final TileEntityRainTankWood tank = (TileEntityRainTankWood) tileEntity;
				if (heldItem != null) {
					if (heldItem.getItem() == Items.BUCKET) {
						if (tank.onBlockActivated(playerIn, heldItem, playerIn.inventory.getSlotFor(heldItem))) {
							return true;
						}
					}
					playerIn.openGui(Waterworks.instance, GuiProxy.WATERWORKS_RAINTANK_GUI, worldIn, pos.getX(),
							pos.getY(), pos.getZ());
					return true;
				}

				playerIn.openGui(Waterworks.instance, GuiProxy.WATERWORKS_RAINTANK_GUI, worldIn, pos.getX(), pos.getY(),
						pos.getZ());
				return true;

			}
		}
		return true;
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
		final TileEntityRainTankWood tileEntity = getTE(world, pos);
		final IItemHandler handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		WaterworksInventoryHelper.dropItemsFromInventory(world, pos, handler);
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
