package org.terpo.waterworks.item;

import java.util.List;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.entity.item.EntityFireworkRocketRain;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemFireworkRain extends FireworkRocketItem {

	public ItemFireworkRain() {
		super((new Item.Properties()).group(Waterworks.CREATIVE_TAB));
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		final World worldIn = context.getWorld();
		final PlayerEntity player = context.getPlayer();
		final BlockPos pos = context.getPos();

		if (!worldIn.isRemote) {
			final ItemStack itemstack = context.getItem();

			final Vec3d hitVec = context.getHitVec();
			final EntityFireworkRocketRain entityfireworkrocket = new EntityFireworkRocketRain(worldIn,
					pos.getX() + hitVec.x, pos.getY() + hitVec.y, pos.getZ() + hitVec.z, itemstack);
			worldIn.addEntity(entityfireworkrocket);

			if (!player.isCreative()) {
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
				final EntityFireworkRocketRain entityfireworkrocket = new EntityFireworkRocketRain(worldIn, itemstack,
						player);
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
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag advanced) {
		super.addInformation(stack, world, tooltip, advanced);
		if (stack.hasTag()) {
			final CompoundNBT nbttagcompound = stack.getTag();
			if (nbttagcompound.hasUniqueId("RAIN")) {
				final int multi = nbttagcompound.getInt("RAIN");
				// FIXME Rain Tooltips
//				tooltip.add(I18n.format("tooltip.rain_rocket.bad_weather"));
//				tooltip.add(I18n.format("tooltip.rain_rocket.rain_duration") + ": " + multi + "/"
//						+ WaterworksConfig.rockets.rainMaxMultiplier);
//				tooltip.add(I18n.format("tooltip.rain_rocket.rain_duration") + ": "
//						+ (WaterworksConfig.rockets.rainDuration * multi) + " ticks");
			}
		}
	}
}
