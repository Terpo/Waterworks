package org.terpo.waterworks.block;

import java.util.List;

import javax.annotation.Nullable;

import org.terpo.waterworks.init.WaterworksItems;
import org.terpo.waterworks.tileentity.BaseTileEntity;
import org.terpo.waterworks.tileentity.TileEntityRainCollector;

import mcjty.theoneprobe.api.IIconStyle;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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

public class BlockRainCollector extends BaseBlockTE<BaseTileEntity> {

	public BlockRainCollector() {
		super();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(new StringTextComponent(I18n.format("tooltip.rain_collector")));
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityRainCollector();
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand,
			BlockRayTraceResult facing) {
		if (!worldIn.isRemote && hand == Hand.MAIN_HAND) {// isRemote true = client
			final TileEntity tileEntity = getTE(worldIn, pos);
			if (tileEntity instanceof TileEntityRainCollector) {
				final ItemStack heldItem = playerIn.getHeldItem(hand);
				if (heldItem.getItem() == WaterworksItems.itemPipeWrench) {
					final TileEntityRainCollector collector = (TileEntityRainCollector) tileEntity;
					String out;
					if (collector.hasController()) {
						final BlockPos controllerPos = collector.getController().getPos();
						out = "Found Controller at" + " " + controllerPos.getX() + "," + controllerPos.getY() + ","
								+ controllerPos.getZ();
					} else {
						out = "No Controller found";
					}
					playerIn.sendMessage(new StringTextComponent(out));
				}
			}
		}
		return super.onBlockActivated(state, worldIn, pos, playerIn, hand, facing);
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		final TileEntity tileEntity = getTE(world, pos);
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
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world,
			BlockState blockState, IProbeHitData data) {
		if (player.isSneaking()) {
			final TileEntity te = world.getTileEntity(data.getPos());
			if (te instanceof TileEntityRainCollector) {
				final IIconStyle iconStyle = probeInfo.defaultIconStyle().textureWidth(32).textureHeight(32);
				final TileEntityRainCollector tile = (TileEntityRainCollector) te;
				if (tile.hasController()) {
					final BlockPos pos = tile.getController().getPos();
					probeInfo.horizontal().text("Controller ").icon(this.guiIconsTOP, 0, 16, 16, 16, iconStyle)
							.text("@" + pos.getX() + "," + pos.getY() + "," + pos.getZ());
				} else {
					probeInfo.horizontal().text("Controller ").icon(this.guiIconsTOP, 16, 16, 16, 16, iconStyle);
				}
			}
		}
	}
}
