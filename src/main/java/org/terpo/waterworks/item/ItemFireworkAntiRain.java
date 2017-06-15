package org.terpo.waterworks.item;

import java.util.List;

import org.terpo.waterworks.entity.item.EntityFireworkRocketAntiRain;
import org.terpo.waterworks.init.WaterworksConfig;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFirework;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFireworkAntiRain extends ItemFirework {

	public ItemFireworkAntiRain() {

	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			final ItemStack itemstack = player.getHeldItem(hand);
			final EntityFireworkRocketAntiRain entityfireworkrocket = new EntityFireworkRocketAntiRain(worldIn,
					pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ, itemstack);
			worldIn.spawnEntity(entityfireworkrocket);

			if (!player.capabilities.isCreativeMode) {
				itemstack.shrink(1);
			}
		}
		return EnumActionResult.SUCCESS;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (playerIn.isElytraFlying()) {
			final ItemStack itemstack = playerIn.getHeldItem(handIn);

			if (!worldIn.isRemote) {
				final EntityFireworkRocketAntiRain entityfireworkrocket = new EntityFireworkRocketAntiRain(worldIn,
						itemstack, playerIn);
				worldIn.spawnEntity(entityfireworkrocket);

				if (!playerIn.capabilities.isCreativeMode) {
					itemstack.shrink(1);
				}
			}

			return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
		return new ActionResult(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
	}
	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		if (stack.hasTagCompound()) {
			final NBTTagCompound nbttagcompound = stack.getTagCompound();
			if (nbttagcompound.hasKey("ANTIRAIN")) {
				final int multi = nbttagcompound.getInteger("ANTIRAIN");
				tooltip.add(I18n.format("tooltip.anti_rain_rocket.good_weather"));
				tooltip.add(I18n.format("tooltip.anti_rain_rocket.sunshine_multiplier") + ": " + multi + "/"
						+ WaterworksConfig.ANTI_RAIN_DURATION_MULTIPLIER_MAX);
				tooltip.add(I18n.format("tooltip.anti_rain_rocket.sunshine_duration") + ": "
						+ (WaterworksConfig.ANTI_RAIN_DURATION * multi) + " ticks");
				tooltip.add(I18n.format("tooltip.anti_rain_rocket.max_additional_days") + ": "
						+ WaterworksConfig.ANTI_RAIN_MAX_RANDOM_ADDITIONAL_DAYS);
			}
		}

	}
}
