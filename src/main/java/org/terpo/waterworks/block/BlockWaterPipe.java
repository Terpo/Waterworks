package org.terpo.waterworks.block;

import javax.annotation.Nullable;

import org.terpo.waterworks.init.WaterworksBlocks;
import org.terpo.waterworks.tileentity.TileEntityGroundwaterPump;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockWaterPipe extends Block {

	private static final VoxelShape boundingBox = VoxelShapes.create(new AxisAlignedBB(.375, 0, .375, .625, 1, .625));
	private static final VoxelShape collisionBox = VoxelShapes
			.create(new AxisAlignedBB(.3125, 0, .3125, .7375, 1, .7375));
	public BlockWaterPipe() {
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(2F, 6.0F).sound(SoundType.METAL));
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos,
			ISelectionContext context) {
		return collisionBox;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return boundingBox;
	}

	@Override
	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state,
			@Nullable TileEntity te, ItemStack stack) {
		BlockPos newPos = pos;
		while (true) {
			newPos = newPos.up();
			final Block block = worldIn.getBlockState(newPos).getBlock();
			if (block.equals(WaterworksBlocks.waterPipe)) {
				continue;
			}
			if (block.equals(WaterworksBlocks.groundwaterPump)) {
				if (block.hasTileEntity(state)) {
					final TileEntity tE = worldIn.getTileEntity(newPos);
					if (tE instanceof TileEntityGroundwaterPump) {
						((TileEntityGroundwaterPump) tE).setStructureComplete(false);
					}
					break;
				}
			}
			break;

		}
		super.harvestBlock(worldIn, player, pos, state, te, stack);
	}

}
