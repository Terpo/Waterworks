package org.terpo.waterworks.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWaterPipe extends Block {

	private static final AxisAlignedBB boundingBox = new AxisAlignedBB(.25, 0, .25, .75, 1, .75);
	private static final AxisAlignedBB collisionBox = new AxisAlignedBB(.1875, 0, .1875, .8125, 1, .8125);
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
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, collisionBox);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
}
