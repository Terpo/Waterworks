package org.terpo.waterworks.block;

import javax.annotation.Nullable;

import org.terpo.waterworks.helper.FluidHelper;
import org.terpo.waterworks.inventory.WaterworksInventoryHelper;
import org.terpo.waterworks.tileentity.TileEntityRainTankWood;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockRainTankWood extends BaseBlockTE<TileWaterworks> {
	public BlockRainTankWood() {
		super(Block.Properties.create(Material.WOOD));
	}

//	@Override
//	public boolean isOpaqueCube(BlockState state) {
//		return false;
//	}

	// isNormalCube and isFullCube could also help here for the TESR

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand,
			BlockRayTraceResult facing) {
		if (!worldIn.isRemote && hand == Hand.MAIN_HAND) {// isRemote true = client
			final TileEntity tileEntity = getTE(worldIn, pos);
			if (tileEntity instanceof TileEntityRainTankWood) {
				final ItemStack heldItem = playerIn.getHeldItem(hand);
				if (!heldItem.isEmpty() && !playerIn.isSneaking()
						&& tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null).isPresent()
						&& FluidHelper.interactWithFluidHandler(worldIn, pos, playerIn, hand, facing, tileEntity,
								heldItem)) {
					return true;
				}
				NetworkHooks.openGui((ServerPlayerEntity) playerIn, (INamedContainerProvider) tileEntity,
						tileEntity.getPos());
				return true;
			}
		}
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityRainTankWood();
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
		if (te instanceof TileEntityRainTankWood) {
			return getTE(world, pos).getComparatorOutput();
		}
		return 0;
	}

	@Override
	public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te,
			ItemStack stack) {
		final TileEntity tileEntity = getTE(world, pos);
		if (tileEntity instanceof TileWaterworks) {
			final LazyOptional<IItemHandler> capability = tileEntity
					.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			capability.ifPresent(handler -> WaterworksInventoryHelper.dropItemsFromInventory(world, pos, handler));
		}
		super.harvestBlock(world, player, pos, state, te, stack);
	}

}
