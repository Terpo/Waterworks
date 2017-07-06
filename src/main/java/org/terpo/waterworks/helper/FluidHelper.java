package org.terpo.waterworks.helper;

import org.terpo.waterworks.fluid.WaterworksTank;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

public class FluidHelper {

	public static boolean interactWithFluidHandler(World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand,
			EnumFacing facing, final TileEntity tileEntity, final ItemStack heldItem) {
		if (FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, facing)) {
			worldIn.playSound((EntityPlayer) null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
			return true;
		}
		// Try Glass Bottle handling
		if (heldItem.getItem().equals(Items.GLASS_BOTTLE)) {
			return fillWaterBottle(worldIn, pos, playerIn, heldItem, hand, (TileWaterworks) tileEntity);
		}
		return false;
	}

	public static boolean fillWaterBottle(World worldIn, BlockPos pos, EntityPlayer playerIn, ItemStack itemstack,
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
			return true;
		}
		return false;
	}
}
