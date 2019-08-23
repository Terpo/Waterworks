package org.terpo.waterworks.item;

import java.util.List;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.entity.item.EntityFireworkRocketAntiRain;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemFireworkAntiRain extends FireworkRocketItem {

	public ItemFireworkAntiRain() {
		super((new Item.Properties()).group(Waterworks.CREATIVE_TAB));
	}

	@Override
	public EnumActionResult onItemUse(ItemUseContext context) {
		final World worldIn = context.getWorld();
		final PlayerEntity player = context.getPlayer();
		final BlockPos pos = context.getPos();

		if (!worldIn.isRemote) {
			final ItemStack itemstack = context.getItem();
			final EntityFireworkRocketAntiRain entityfireworkrocket = new EntityFireworkRocketAntiRain(worldIn,
					pos.getX() + context.getHitX(), pos.getY() + context.getHitY(), pos.getZ() + context.getHitZ(),
					itemstack);
			worldIn.spawnEntity(entityfireworkrocket);

			if (!player.isCreative()) {
				itemstack.shrink(1);
			}
		}
		return EnumActionResult.SUCCESS;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
		if (player.isElytraFlying()) {
			final ItemStack itemstack = player.getHeldItem(handIn);

			if (!worldIn.isRemote) {
				final EntityFireworkRocketAntiRain entityfireworkrocket = new EntityFireworkRocketAntiRain(worldIn,
						itemstack, player);
				worldIn.spawnEntity(entityfireworkrocket);

				if (!player.isCreative()) {
					itemstack.shrink(1);
				}
			}

			return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(handIn));
		}
		return new ActionResult(EnumActionResult.PASS, player.getHeldItem(handIn));
	}
	/**
	 * allows items to add custom lines of information to the mouseover description
	 */

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag advanced) {
		super.addInformation(stack, world, tooltip, advanced);
		if (stack.hasTag()) {
			final CompoundNBT nbttagcompound = stack.getTag();
			if (nbttagcompound.hasKey("ANTIRAIN")) {
				final int multi = nbttagcompound.getInt("ANTIRAIN");
				// FIXME Anti Rain Tooltips
//				tooltip.add(I18n.format("tooltip.anti_rain_rocket.good_weather"));
//				tooltip.add(I18n.format("tooltip.anti_rain_rocket.sunshine_multiplier") + ": " + multi + "/"
//						+ WaterworksConfig.rockets.clearSkyMaxMultiplier);
//				tooltip.add(I18n.format("tooltip.anti_rain_rocket.sunshine_duration") + ": "
//						+ (WaterworksConfig.rockets.clearSkyDuration * multi) + " ticks");
//				tooltip.add(I18n.format("tooltip.anti_rain_rocket.max_additional_days") + ": "
//						+ WaterworksConfig.rockets.clearSkyMaxRandomAdditionalDays);
			}
		}

	}
}
