package org.terpo.waterworks.block;

import javax.annotation.Nullable;

import org.terpo.waterworks.helper.FluidHelper;
import org.terpo.waterworks.init.WaterworksBlocks;
import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.init.WaterworksItems;
import org.terpo.waterworks.inventory.WaterworksInventoryHelper;
import org.terpo.waterworks.tileentity.TileEntityGroundwaterPump;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockGroundwaterPump extends BaseBlockTE<TileEntityGroundwaterPump> {
	private static final AxisAlignedBB boundingBox = new AxisAlignedBB(0, 0, .125, 1, 0.8125, .875);
	private static final AxisAlignedBB collisionBox = new AxisAlignedBB(0, 0, .125, 1, 0.8125, .875);
	public BlockGroundwaterPump() {
		super(Block.Properties.create(Material.IRON));
		this.setDefaultState(getDefaultState().with(BlockStateProperties.FACING, Direction.NORTH));
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand,
			BlockRayTraceResult facing) {

		if (!worldIn.isRemote && hand == Hand.MAIN_HAND) {// isRemote true = client
			final TileEntity tileEntity = getTE(worldIn, pos);
			if (tileEntity instanceof TileEntityGroundwaterPump) {
				final ItemStack heldItem = playerIn.getHeldItem(hand);
				if (heldItem.getItem() == WaterworksItems.itemPipeWrench) {
					turnPumpModel(worldIn, pos, state);
					return true;
				}
				if (!heldItem.isEmpty() && !playerIn.isSneaking()
						&& tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).isPresent()
						&& FluidHelper.interactWithFluidHandler(worldIn, pos, playerIn, hand, facing, tileEntity,
								heldItem)) {
					return true;
				}

//				playerIn.openGui(Waterworks.instance, GuiProxy.WATERWORKS_GROUNDWATER_PUMP_GUI, worldIn, pos.getX(),
//						pos.getY(), pos.getZ());
				return true;
			}
		}
		return true;
	}

	// FIXME Placement
//	The way that the direction state is set for FirstBlock is not standard and leads to strange result that are not similar to how vanilla blocks work. 
//	To get the vanilla behaviour, instead of using onBlockPlacedBy, use getStateForPlacement(BlockItemUseContext context) along with setting a default state in the constructor. I suggest you look at the ObserverBlock vanilla class as an example.
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
			ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if (placer != null) {
			worldIn.setBlockState(pos, state.with(BlockStateProperties.FACING, placer.getHorizontalFacing()), 2);
		}
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityGroundwaterPump();
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState bs) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState bs, World world, BlockPos pos) {
		final TileEntity te = getTE(world, pos);
		if (te instanceof TileEntityGroundwaterPump) {
			return getTE(world, pos).getComparatorOutput();
		}
		return 0;
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		final TileEntity tileEntity = getTE(world, pos);
		if (tileEntity instanceof TileWaterworks) {
			final LazyOptional<IItemHandler> capability = tileEntity
					.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			capability.ifPresent(handler -> WaterworksInventoryHelper.dropItemsFromInventory(world, pos, handler));
		}
		BlockGroundwaterPump.breakPipes(world, pos);
		super.onBlockHarvested(world, pos, state, player);
	}

	private static void breakPipes(World world, BlockPos pos) {
		int y = pos.getY() - 1;
		int count = 0;
		while (y >= 0) {
			final BlockPos position = new BlockPos(pos.getX(), y, pos.getZ());
			final BlockState state = world.getBlockState(position);
			if (state.getBlock().equals(WaterworksBlocks.waterPipe)) {
				world.destroyBlock(position, false);
				count++;
				y--;
			} else {
				break;
			}
		}
		if (count > 0) {
			spawnAsEntity(world, pos, new ItemStack(WaterworksBlocks.waterPipe, count));
			if (WaterworksConfig.pump.groundwaterPumpSafety) {
				world.setBlockState(pos.down(), Blocks.COBBLESTONE_SLAB.getDefaultState());
			}

		}

	}

//	@Override
//	public boolean isFullBlock(BlockState state) {
//		return false;
//	}
//	@Override
//	public boolean isOpaqueCube(BlockState state) {
//		return false;
//	}
//	@Override
//	public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
//		return boundingBox;
//	}
//
//	@SuppressWarnings("static-access")
//	@Override
//	public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
//			List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185477_7_) {
//		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, collisionBox);
//	}
//
//	@Override
//	public boolean isFullCube(BlockState state) {
//		return false;
//	}

	// ModelTurning
	private void turnPumpModel(World worldIn, BlockPos pos, BlockState state) {
		worldIn.setBlockState(pos,
				state.with(BlockStateProperties.FACING, state.get(BlockStateProperties.FACING).rotateY()), 2);
	}
//	@Override
//	public BlockState getStateFromMeta(int meta) {
//		final Direction facing = Direction.byHorizontalIndex(meta);
//		return this.getDefaultState().with(BlockStateProperties.FACING, facing);
//	}
//	@Override
//	public int getMetaFromState(BlockState state) {
//		final Direction facing = state.get(BlockStateProperties.FACING);
//		return facing.getHorizontalIndex();
//	}
//	@Override
//	protected BlockStateContainer createBlockState() {
//		return new BlockStateContainer(this, new IProperty[]{PROPERTYFACING});
//	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.FACING);
	}
}
