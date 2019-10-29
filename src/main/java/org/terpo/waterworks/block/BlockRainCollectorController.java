package org.terpo.waterworks.block;

import java.util.List;

import javax.annotation.Nullable;

import org.terpo.waterworks.helper.FluidHelper;
import org.terpo.waterworks.helper.WaterworksInventoryHelper;
import org.terpo.waterworks.init.WaterworksItems;
import org.terpo.waterworks.tileentity.TileEntityRainCollectorController;
import org.terpo.waterworks.tileentity.TileWaterworks;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
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

	public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 4);
	public BlockRainCollectorController() {
		super();
	}
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(new StringTextComponent(I18n.format("tooltip.rain_collector_controller")));
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityRainCollectorController();
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand,
			BlockRayTraceResult facing) {
		if (!worldIn.isRemote && hand == Hand.MAIN_HAND) {// isRemote true = client
			final TileEntity tileEntity = getTE(worldIn, pos);
			if (tileEntity instanceof TileEntityRainCollectorController) {
				final ItemStack heldItem = playerIn.getHeldItem(hand);
				if (heldItem.getItem() == WaterworksItems.itemPipeWrench) {
					final int collectors = ((TileEntityRainCollectorController) tileEntity).findRainCollectors();
					final String out = collectors - 1 + " " + "Collectors found";
					playerIn.sendMessage(new StringTextComponent(out));
					return true;
				}
				if (!heldItem.isEmpty() && !playerIn.isSneaking()
						&& tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).isPresent()
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

	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		final TileEntity tileEntity = getTE(world, pos);
		if (tileEntity instanceof TileWaterworks) {
			final LazyOptional<IItemHandler> capability = tileEntity
					.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
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
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world,
			BlockState blockState, IProbeHitData data) {
		if (player.isSneaking()) {
			final TileEntity te = world.getTileEntity(data.getPos());
			if (te instanceof TileEntityRainCollectorController) {
				final TileEntityRainCollectorController tile = (TileEntityRainCollectorController) te;
				probeInfo.horizontal().text(tile.getConnectedCollectors() + " Collectors");
			}
		}
	}
}
