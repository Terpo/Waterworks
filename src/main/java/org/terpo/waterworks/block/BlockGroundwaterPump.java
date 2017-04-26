package org.terpo.waterworks.block;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.gui.GuiProxy;
import org.terpo.waterworks.inventory.WaterworksInventoryHelper;
import org.terpo.waterworks.tileentity.TileEntityGroundwaterPump;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockGroundwaterPump extends BaseBlockTE<TileEntityGroundwaterPump> {
	public BlockGroundwaterPump() {
		super(Material.IRON);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (!worldIn.isRemote && hand == EnumHand.MAIN_HAND) {// isRemote true = client
			final TileEntity tileEntity = getTE(worldIn, pos);
			if (tileEntity instanceof TileEntityGroundwaterPump) {
				final ItemStack heldItem = playerIn.getHeldItem(hand);
				if (!playerIn.isSneaking()) {
					if (!heldItem.isEmpty()) {
						if (tileEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
							final IFluidHandler tileEntityFluidHandler = tileEntity
									.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
							final FluidActionResult fluidActionResult = FluidUtil.interactWithFluidHandler(heldItem,
									tileEntityFluidHandler, playerIn);
							if (fluidActionResult.isSuccess()) {
								playerIn.setHeldItem(hand, fluidActionResult.getResult());
								return true;
							}
						}
					}
					playerIn.openGui(Waterworks.instance, GuiProxy.WATERWORKS_RAINTANK_GUI, worldIn, pos.getX(),
							pos.getY(), pos.getZ());
					return true;
				}
			}
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityGroundwaterPump();
	}

	@Override
	public boolean hasTileEntity() {
		return true;
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState bs) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState bs, World world, BlockPos pos) {
		final TileEntity te = getTE(world, pos);
		if (te instanceof TileEntityGroundwaterPump) {
			return getTE(world, pos).getComparatorOutput();
		}
		return 0;
	}
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		final TileEntity tileEntity = getTE(world, pos);
		if (tileEntity instanceof TileWaterworks) {
			final IItemHandler handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			WaterworksInventoryHelper.dropItemsFromInventory(world, pos, handler);
		}
		super.breakBlock(world, pos, state);
	}
}