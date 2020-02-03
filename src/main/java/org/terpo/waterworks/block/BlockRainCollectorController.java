package org.terpo.waterworks.block;

import java.util.List;

import javax.annotation.Nullable;

import org.terpo.waterworks.helper.FluidHelper;
import org.terpo.waterworks.helper.WaterworksInventoryHelper;
import org.terpo.waterworks.setup.Registration;
import org.terpo.waterworks.tileentity.TileEntityRainCollectorController;
import org.terpo.waterworks.tileentity.TileWaterworks;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockRainCollectorController extends BaseBlockTE<TileWaterworks> {

	public BlockRainCollectorController() {
		super();
	}
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(new TranslationTextComponent("tooltip.rain_collector_controller"));
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityRainCollectorController();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, // NOSONAR
			Hand hand, BlockRayTraceResult facing) {
		if (!worldIn.isRemote && hand == Hand.MAIN_HAND) {// isRemote true = client
			final TileEntity tileEntity = getTileEntity(worldIn, pos);
			if (tileEntity instanceof TileEntityRainCollectorController) {
				final ItemStack heldItem = playerIn.getHeldItem(hand);
				if (heldItem.getItem() == Registration.pipeWrenchItem.get()) { // TODO instanceof
					final int collectors = ((TileEntityRainCollectorController) tileEntity).findRainCollectors();
					playerIn.sendMessage(new TranslationTextComponent("block.waterworks.rain_collector_controller.controllers",
							Integer.valueOf((collectors - 1))));
					return ActionResultType.SUCCESS;
				}
				if (!heldItem.isEmpty() && !playerIn.isShiftKeyDown()
						&& tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).isPresent()
						&& FluidHelper.interactWithFluidHandler(worldIn, pos, playerIn, hand, facing, tileEntity, heldItem)) {
					return ActionResultType.SUCCESS;

				}
				NetworkHooks.openGui((ServerPlayerEntity) playerIn, (INamedContainerProvider) tileEntity, tileEntity.getPos());
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		final TileEntity tileEntity = getTileEntity(world, pos);
		if (tileEntity != null) {
			final LazyOptional<IItemHandler> capability = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			capability.ifPresent(handler -> WaterworksInventoryHelper.dropItemsFromInventory(world, pos, handler));
		}
		if (tileEntity instanceof TileEntityRainCollectorController) {
			((TileEntityRainCollectorController) tileEntity).resetController();
		}
		super.onBlockHarvested(world, pos, state, player);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState bs) { // NOSONAR
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState bs, World world, BlockPos pos) { // NOSONAR
		final TileEntity te = getTileEntity(world, pos);
		if (te instanceof TileWaterworks) {
			return getTileEntity(world, pos).getComparatorOutput();
		}
		return 0;
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState,
			IProbeHitData data) {
		if (player.isShiftKeyDown()) {
			final TileEntity te = world.getTileEntity(data.getPos());
			if (te instanceof TileEntityRainCollectorController) {
				final TileEntityRainCollectorController tile = (TileEntityRainCollectorController) te;
				probeInfo.horizontal()
						.text(new TranslationTextComponent("tooltip.collectors", Integer.valueOf(tile.getConnectedCollectors()))
								.getFormattedText());
			}
		}
	}

	@Override
	protected TileWaterworks getTileEntity(World world, BlockPos pos) {
		final TileEntity tE = world.getTileEntity(pos);
		if (tE instanceof TileWaterworks) {
			return (TileWaterworks) tE;
		}
		return null;
	}
}
