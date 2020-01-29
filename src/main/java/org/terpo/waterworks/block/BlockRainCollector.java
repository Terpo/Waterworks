package org.terpo.waterworks.block;

import java.util.List;

import javax.annotation.Nullable;

import org.terpo.waterworks.helper.FluidHelper;
import org.terpo.waterworks.init.WaterworksItems;
import org.terpo.waterworks.tileentity.BaseTileEntity;
import org.terpo.waterworks.tileentity.TileEntityRainCollector;
import org.terpo.waterworks.tileentity.TileEntityRainCollectorController;

import mcjty.theoneprobe.api.IIconStyle;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockRainCollector extends BaseBlockTE<BaseTileEntity> {

	public BlockRainCollector() {
		super();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(new TranslationTextComponent("tooltip.rain_collector"));
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityRainCollector();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn,
			Hand hand, BlockRayTraceResult facing) {
		if (!worldIn.isRemote && hand == Hand.MAIN_HAND) {// isRemote true = client
			final TileEntity tileEntity = getTileEntity(worldIn, pos);
			if (tileEntity instanceof TileEntityRainCollector) {
				final ItemStack heldItem = playerIn.getHeldItem(hand);
				final TileEntityRainCollector collector = (TileEntityRainCollector) tileEntity;
				final Item item = heldItem.getItem();
				if (item == WaterworksItems.itemPipeWrench) {
					handleRightClickWithWrench(playerIn, collector);
					return ActionResultType.SUCCESS;
				}
				if (collector.hasController()) {
					final TileEntityRainCollectorController controller = collector.getController();

					if (!heldItem.isEmpty() && !playerIn.isSneaking()
							&& controller.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).isPresent()
							&& FluidHelper.interactWithFluidHandler(worldIn, pos, playerIn, hand, facing, controller,
									heldItem)) {
						return ActionResultType.SUCCESS;
					}

					NetworkHooks.openGui((ServerPlayerEntity) playerIn, (INamedContainerProvider) controller,
							controller.getPos());
					return ActionResultType.SUCCESS;
				}
			}
		}
		return ActionResultType.SUCCESS;
	}

	protected static void handleRightClickWithWrench(PlayerEntity playerIn, final TileEntityRainCollector collector) {
		ITextComponent out;
		if (collector.hasController()) {
			final BlockPos controllerPos = collector.getController().getPos();
			out = new TranslationTextComponent("block.waterworks.rain_collector.has_controller",
					Integer.valueOf(controllerPos.getX()), Integer.valueOf(controllerPos.getY()),
					Integer.valueOf(controllerPos.getZ()));
		} else {
			out = new TranslationTextComponent("block.waterworks.rain_collector.no_controller");
		}
		playerIn.sendMessage(out);
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		final TileEntity tileEntity = getTileEntity(world, pos);
		if (tileEntity instanceof TileEntityRainCollector) {
			((TileEntityRainCollector) tileEntity).informAboutBlockBreak();
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
		final TileEntity tileEntity = getTileEntity(world, pos);
		if (tileEntity instanceof TileEntityRainCollector) {
			final TileEntityRainCollector collector = (TileEntityRainCollector) tileEntity;
			if (collector.hasController()) {
				return collector.getController().getComparatorOutput();
			}
		}
		return 0;
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world,
			BlockState blockState, IProbeHitData data) {
		if (player.isSneaking()) {
			final TileEntity te = world.getTileEntity(data.getPos());
			if (te instanceof TileEntityRainCollector) {
				final IIconStyle iconStyle = probeInfo.defaultIconStyle().textureWidth(32).textureHeight(32);
				final TileEntityRainCollector tile = (TileEntityRainCollector) te;
				final String tooltip = new TranslationTextComponent("tooltip.controller").getFormattedText();
				if (tile.hasController()) {
					final BlockPos pos = tile.getController().getPos();
					probeInfo.horizontal().text(tooltip).icon(this.guiIconsTOP, 0, 16, 16, 16, iconStyle)
							.text("@" + pos.getX() + "," + pos.getY() + "," + pos.getZ());
				} else {
					probeInfo.horizontal().text(tooltip).icon(this.guiIconsTOP, 16, 16, 16, 16, iconStyle);
				}
			}
		}
	}

	@Override
	protected BaseTileEntity getTileEntity(World world, BlockPos pos) {
		final TileEntity tE = world.getTileEntity(pos);
		if (tE instanceof BaseTileEntity) {
			return (BaseTileEntity) tE;
		}
		return null;
	}
}
