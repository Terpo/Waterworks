package org.terpo.waterworks.item;

import java.util.List;

import org.terpo.waterworks.Config;
import org.terpo.waterworks.entity.item.EntityFireworkRocketAntiRain;
import org.terpo.waterworks.setup.CommonSetup;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemFireworkAntiRain extends FireworkRocketItem {

	public ItemFireworkAntiRain() {
		super((new Item.Properties()).group(CommonSetup.CREATIVE_TAB));
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if (!context.getWorld().isRemote) {
			final ItemStack itemstack = context.getItem();
			final Vec3d vec3d = context.getHitVec();
			context.getWorld().addEntity(new EntityFireworkRocketAntiRain(context.getWorld(), vec3d.x, vec3d.y, vec3d.z, itemstack));
			if (!context.getPlayer().isCreative()) {
				itemstack.shrink(1);
			}

		}
		return ActionResultType.SUCCESS;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
		if (player.isElytraFlying()) {
			final ItemStack itemstack = player.getHeldItem(handIn);

			if (!worldIn.isRemote) {
				final EntityFireworkRocketAntiRain entityfireworkrocket = new EntityFireworkRocketAntiRain(worldIn, itemstack, player);
				worldIn.addEntity(entityfireworkrocket);

				if (!player.isCreative()) {
					itemstack.shrink(1);
				}
			}

			return new ActionResult(ActionResultType.SUCCESS, player.getHeldItem(handIn));
		}
		return new ActionResult(ActionResultType.PASS, player.getHeldItem(handIn));
	}
	/**
	 * allows items to add custom lines of information to the mouse over description
	 */

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag advanced) {
		super.addInformation(stack, world, tooltip, advanced);
		if (stack.hasTag()) {
			final CompoundNBT nbttagcompound = stack.getTag();
			if (nbttagcompound.contains("ANTIRAIN")) {
				final int multi = nbttagcompound.getInt("ANTIRAIN");
				tooltip.add(new TranslationTextComponent("tooltip.anti_rain_rocket.good_weather"));
				tooltip.add(new TranslationTextComponent("tooltip.anti_rain_rocket.sunshine_multiplier")
						.appendText(": " + multi + "/" + Config.rockets.getClearSkyMaxMultiplier()));
				tooltip.add(new TranslationTextComponent("tooltip.anti_rain_rocket.sunshine_duration")
						.appendText(": " + (Config.rockets.getClearSkyDuration() * multi) + " ticks"));
				tooltip.add(new TranslationTextComponent("tooltip.anti_rain_rocket.max_additional_days")
						.appendText(": " + Config.rockets.getClearSkyMaxRandomAdditionalDays()));
			}
		}

	}
}
