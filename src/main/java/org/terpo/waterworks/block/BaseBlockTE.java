package org.terpo.waterworks.block;

import org.terpo.waterworks.compat.top.provider.TOPInfoProvider;
import org.terpo.waterworks.fluid.WaterworksTank;
import org.terpo.waterworks.tileentity.BaseTileEntity;
import org.terpo.waterworks.tileentity.TileEntityRainCollector;
import org.terpo.waterworks.tileentity.TileEntityRainCollectorController;
import org.terpo.waterworks.tileentity.TileWaterworks;

import mcjty.theoneprobe.api.IIconStyle;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BaseBlockTE<T extends BaseTileEntity> extends BaseBlock implements ITileEntityProvider, TOPInfoProvider {

	private final ResourceLocation guiIcons = new ResourceLocation("theoneprobe", "textures/gui/icons.png");

	public BaseBlockTE(Material materialIn) {
		super(materialIn);
	}
	public BaseBlockTE() {
		this(Material.IRON);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return null;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		super.breakBlock(world, pos, state);
	}

	@SuppressWarnings("unchecked")
	protected T getTE(World world, BlockPos pos) {
		final TileEntity tE = world.getTileEntity(pos);
		if (tE instanceof BaseTileEntity) {
			return (T) tE;
		}
		return null;
	}

	@SuppressWarnings("hiding")
	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world,
			IBlockState blockState, IProbeHitData data) {
		final TileEntity te = world.getTileEntity(data.getPos());
		if (player.isSneaking()) {
			if (te instanceof TileEntityRainCollectorController) {
				final TileEntityRainCollectorController tile = (TileEntityRainCollectorController) te;
				probeInfo.horizontal().text(tile.getConnectedCollectors() + " Collectors");
			}
			final IIconStyle iconStyle = probeInfo.defaultIconStyle().textureWidth(32).textureHeight(32);
			if (te instanceof TileEntityRainCollector) {
				final TileEntityRainCollector tile = (TileEntityRainCollector) te;
				if (tile.hasController()) {
					final BlockPos pos = tile.getController().getPos();
					probeInfo.horizontal().text("Controller ").icon(this.guiIcons, 0, 16, 16, 16, iconStyle)
							.text("@" + pos.getX() + "," + pos.getY() + "," + pos.getZ());
				} else {
					probeInfo.horizontal().text("Controller ").icon(this.guiIcons, 16, 16, 16, 16, iconStyle);
				}
			}
		}
	}

	protected static void fillWaterBottle(World worldIn, BlockPos pos, EntityPlayer playerIn, ItemStack itemstack,
			EnumHand hand, TileWaterworks tileEntity) {
		final WaterworksTank tank = tileEntity.getFluidTank();
		if (tank.getFluidAmount() >= 1000) {
			if (!playerIn.capabilities.isCreativeMode) {
				tank.drainInternal(1000, true);
				final ItemStack stackWaterBottle = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM),
						PotionTypes.WATER);
				itemstack.shrink(1);
				if (itemstack.isEmpty()) {
					playerIn.setHeldItem(hand, stackWaterBottle);
				} else if (!playerIn.inventory.addItemStackToInventory(stackWaterBottle)) {
					playerIn.dropItem(stackWaterBottle, false);
				} else if (playerIn instanceof EntityPlayerMP) {
					((EntityPlayerMP) playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
				}
			}
			worldIn.playSound((EntityPlayer) null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	}

}
