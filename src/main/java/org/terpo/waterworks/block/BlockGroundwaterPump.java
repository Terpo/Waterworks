package org.terpo.waterworks.block;

import java.util.List;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.gui.GuiProxy;
import org.terpo.waterworks.init.WaterworksBlocks;
import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.inventory.WaterworksInventoryHelper;
import org.terpo.waterworks.tileentity.TileEntityGroundwaterPump;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockGroundwaterPump extends BaseBlockTE<TileEntityGroundwaterPump> {
	private static final AxisAlignedBB boundingBox = new AxisAlignedBB(0, 0, .125, 1, 0.8125, .875);
	private static final AxisAlignedBB collisionBox = new AxisAlignedBB(0, 0, .125, 1, 0.8125, .875);

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
				if (!heldItem.isEmpty()) {
					if (!playerIn.isSneaking()) {
						if (tileEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
							final IFluidHandler tileEntityFluidHandler = tileEntity
									.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
							final FluidActionResult fluidActionResult = FluidUtil.interactWithFluidHandler(heldItem,
									tileEntityFluidHandler, playerIn);
							if (fluidActionResult.isSuccess()) {
								playerIn.setHeldItem(hand, fluidActionResult.getResult());
								return true;
							}
							// Try Glass Bottle handling
							if (heldItem.getItem().equals(Items.GLASS_BOTTLE)) {
								fillWaterBottle(worldIn, pos, playerIn, heldItem, hand,
										(TileEntityGroundwaterPump) tileEntity);
								return true;
							}
						}
					}
				}
				playerIn.openGui(Waterworks.instance, GuiProxy.WATERWORKS_GROUNDWATER_PUMP_GUI, worldIn, pos.getX(),
						pos.getY(), pos.getZ());
				return true;
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
		BlockGroundwaterPump.breakPipes(world, pos);
		super.breakBlock(world, pos, state);
	}

	@SuppressWarnings("deprecation")
	private static void breakPipes(World world, BlockPos pos) {
		int y = pos.getY() - 1;
		int count = 0;
		while (y > 0) {
			final BlockPos position = new BlockPos(pos.getX(), y, pos.getZ());
			final IBlockState state = world.getBlockState(position);
			if (state.getBlock().equals(WaterworksBlocks.water_pipe)) {
				world.destroyBlock(position, false);
				count++;
				y--;
			} else {
				break;
			}
		}
		if (count > 0) {
			spawnAsEntity(world, pos, new ItemStack(WaterworksBlocks.water_pipe, count));
			if (WaterworksConfig.GROUNDWATER_PUMP_SAFETY) {
				// TODO remove deprecated call
				world.setBlockState(pos.down(),
						Blocks.STONE_SLAB.getStateFromMeta(BlockStoneSlab.EnumType.COBBLESTONE.getMetadata()));
			}
		}

	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return boundingBox;
	}

	@SuppressWarnings("static-access")
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185477_7_) {
		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, collisionBox);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
}
