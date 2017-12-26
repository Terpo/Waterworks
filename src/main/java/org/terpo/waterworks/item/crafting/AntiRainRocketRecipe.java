package org.terpo.waterworks.item.crafting;

import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.init.WaterworksItems;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class AntiRainRocketRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
	private ItemStack resultItem = ItemStack.EMPTY;
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		this.resultItem = ItemStack.EMPTY;

		int rocketStack = -1;
		boolean isFireworks = false;
		boolean isAntiRainRocket = false;
		int multiplierAdd = 0;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			final ItemStack itemstack = inv.getStackInSlot(i);

			if (itemstack.getItem() == Items.FIREWORKS) {
				isFireworks = true;
				rocketStack = i;
				continue;
			}
			if (itemstack.getItem() == WaterworksItems.firework_anti_rain) {
				isAntiRainRocket = true;
				rocketStack = i;
				continue;
			}
			if (itemstack.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && itemstack.getMetadata() == 0) {
				multiplierAdd++;
			}
		}

		if ((isAntiRainRocket && isFireworks) || multiplierAdd <= 0) {
			return false;
		}
		this.resultItem = new ItemStack(WaterworksItems.firework_anti_rain);

		int multiplierOld = 0;
		if (isFireworks) {
			final ItemStack rocket = inv.getStackInSlot(rocketStack);
			NBTTagCompound nbtCompound = rocket.getTagCompound();

			if (nbtCompound == null) {
				nbtCompound = new NBTTagCompound();
			}
			if (multiplierOld + multiplierAdd > WaterworksConfig.ANTI_RAIN_DURATION_MULTIPLIER_MAX) {
				return false;
			}
			nbtCompound.setInteger("ANTIRAIN", multiplierOld + multiplierAdd);
			this.resultItem.setTagCompound(nbtCompound);
			return true;
		}

		if (isAntiRainRocket) {
			final ItemStack rocket = inv.getStackInSlot(rocketStack);
			final NBTTagCompound nbtCompound = rocket.getTagCompound();
			NBTTagCompound newTag = new NBTTagCompound();
			if (nbtCompound != null) {
				newTag = nbtCompound.copy();
				if (nbtCompound.hasKey("ANTIRAIN")) {
					multiplierOld = nbtCompound.getInteger("ANTIRAIN");
				}
			}
			if ((multiplierOld + multiplierAdd) > WaterworksConfig.ANTI_RAIN_DURATION_MULTIPLIER_MAX) {
				return false;
			}
			newTag.setInteger("ANTIRAIN", multiplierOld + multiplierAdd);
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
	public ItemStack getRecipeOutput() {
		return this.resultItem;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		return ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 1;
	}

}
