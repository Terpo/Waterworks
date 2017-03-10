package org.terpo.waterworks.item;

import java.util.List;

import org.terpo.waterworks.entity.item.EntityFireworkRocketRain;
import org.terpo.waterworks.init.WaterworksConfig;

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

public class ItemFireworkRain extends ItemFirework {

	public ItemFireworkRain() {

	}
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			final ItemStack itemstack = player.getHeldItem(hand);
			final EntityFireworkRocketRain entityfireworkrocket = new EntityFireworkRocketRain(worldIn,
					pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ, itemstack);
//			final EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(worldIn, pos.getX() + hitX,
//					pos.getY() + hitY, pos.getZ() + hitZ, itemstack);
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
				final EntityFireworkRocketRain entityfireworkrocket = new EntityFireworkRocketRain(worldIn, itemstack,
						playerIn);
//				final EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(worldIn, itemstack,
//						playerIn);
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
			if (nbttagcompound.hasKey("RAIN")) {
				final int multi = nbttagcompound.getInteger("RAIN");

				tooltip.add("Rain duration x" + multi);
				tooltip.add("Rain duration " + (WaterworksConfig.RAIN_DURATION * multi) + " ticks");
			}
		}
	}
}
