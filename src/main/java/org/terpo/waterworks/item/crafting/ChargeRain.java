package org.terpo.waterworks.item.crafting;

import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.init.WaterworksItems;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class ChargeRain implements IRecipe {
	private ItemStack resultItem = ItemStack.EMPTY;
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		this.resultItem = ItemStack.EMPTY;

		int rocketStack = -1;
		boolean isFireworks = false;
		boolean isRainRocket = false;
		int multiplierAdd = 0;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			final ItemStack itemstack = inv.getStackInSlot(i);

			if (itemstack.getItem() == Items.FIREWORKS) {
				isFireworks = true;
				rocketStack = i;
				continue;
			}
			if (itemstack.getItem() == WaterworksItems.firework_rain) {
				isRainRocket = true;
				rocketStack = i;
				continue;
			}
			if (itemstack.getItem() == Items.GHAST_TEAR) {
				multiplierAdd++;
			}
		}

		if ((isRainRocket && isFireworks) || multiplierAdd <= 0) {
			return false;
		}
		this.resultItem = new ItemStack(WaterworksItems.firework_rain);

		int multiplierOld = 0;
		if (isFireworks) {
			final ItemStack rocket = inv.getStackInSlot(rocketStack);
			NBTTagCompound nbtCompound = rocket.getTagCompound();

			if (nbtCompound == null) {
				nbtCompound = new NBTTagCompound();
			}
			if (multiplierOld + multiplierAdd > WaterworksConfig.RAIN_DURATION_MULTIPLIER_MAX) {
				return false;
			}
			nbtCompound.setInteger("RAIN", multiplierOld + multiplierAdd);
			this.resultItem.setTagCompound(nbtCompound);
			return true;
		}

		if (isRainRocket) {
			final ItemStack rocket = inv.getStackInSlot(rocketStack);
			final NBTTagCompound nbtCompound = rocket.getTagCompound();
			NBTTagCompound newTag = new NBTTagCompound();
			if (nbtCompound != null) {
				newTag = nbtCompound.copy();
				if (nbtCompound.hasKey("RAIN")) {
					multiplierOld = nbtCompound.getInteger("RAIN");
				}
			}
			if ((multiplierOld + multiplierAdd) > WaterworksConfig.RAIN_DURATION_MULTIPLIER_MAX) {
				return false;
			}
			newTag.setInteger("RAIN", multiplierOld + multiplierAdd);
			this.resultItem.setTagCompound(newTag);
			return true;
		}

		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return this.resultItem.copy();
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.resultItem;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		return ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}

}
