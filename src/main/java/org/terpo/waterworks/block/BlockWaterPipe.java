package org.terpo.waterworks.block;

import java.util.List;

import org.terpo.waterworks.init.WaterworksBlocks;
import org.terpo.waterworks.tileentity.TileEntityGroundwaterPump;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWaterPipe extends Block {

	private static final AxisAlignedBB boundingBox = new AxisAlignedBB(.375, 0, .375, .625, 1, .625);
	private static final AxisAlignedBB collisionBox = new AxisAlignedBB(.3125, 0, .3125, .7375, 1, .7375);
	public BlockWaterPipe() {
		super(Material.IRON);
		this.setResistance(2F);
		this.setHardness(2F);
		this.setHarvestLevel("pickaxe", 2);
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

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185477_7_) {
		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, collisionBox);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
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
		super.breakBlock(worldIn, pos, state);
	}

}
