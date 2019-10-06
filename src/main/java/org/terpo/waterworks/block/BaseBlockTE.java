package org.terpo.waterworks.block;

import javax.annotation.Nullable;

import org.terpo.waterworks.tileentity.BaseTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BaseBlockTE<T extends BaseTileEntity> extends Block {
//The One Probe: TOPInfoProvider
//	private final ResourceLocation guiIcons = new ResourceLocation("theoneprobe", "textures/gui/icons.png");

	public BaseBlockTE(Block.Properties builder) {
		super(builder);
	}
	public BaseBlockTE() {
		this(createBaseBlockProperties());
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return null;
	}

	@SuppressWarnings("unchecked")
	protected T getTE(World world, BlockPos pos) {
		final TileEntity tE = world.getTileEntity(pos);
		if (tE instanceof BaseTileEntity) {
			return (T) tE;
		}
		return null;
	}

//	@SuppressWarnings("hiding")
//	@Override
//	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world,
//			BlockState blockState, IProbeHitData data) {
//		final TileEntity te = world.getTileEntity(data.getPos());
//		if (player.isSneaking()) {
//			if (te instanceof TileEntityRainCollectorController) {
//				final TileEntityRainCollectorController tile = (TileEntityRainCollectorController) te;
//				probeInfo.horizontal().text(tile.getConnectedCollectors() + " Collectors");
//			}
//			final IIconStyle iconStyle = probeInfo.defaultIconStyle().textureWidth(32).textureHeight(32);
//			if (te instanceof TileEntityRainCollector) {
//				final TileEntityRainCollector tile = (TileEntityRainCollector) te;
//				if (tile.hasController()) {
//					final BlockPos pos = tile.getController().getPos();
//					probeInfo.horizontal().text("Controller ").icon(this.guiIcons, 0, 16, 16, 16, iconStyle)
//							.text("@" + pos.getX() + "," + pos.getY() + "," + pos.getZ());
//				} else {
//					probeInfo.horizontal().text("Controller ").icon(this.guiIcons, 16, 16, 16, 16, iconStyle);
//				}
//			}
//		}
//	}

	public static Block.Properties createBaseBlockProperties() {
		return Block.Properties.create(Material.IRON).hardnessAndResistance(2F, 6.0F).sound(SoundType.METAL);
	}
}
