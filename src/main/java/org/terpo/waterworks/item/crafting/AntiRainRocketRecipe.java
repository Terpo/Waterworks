package org.terpo.waterworks.item.crafting;

import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.init.WaterworksItems;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
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

			if (itemstack.getItem() == Items.FIREWORK_ROCKET) {
				isFireworks = true;
				rocketStack = i;
				continue;
			}
			if (itemstack.getItem() == WaterworksItems.itemFireworkAntiRain) {
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
		this.resultItem = new ItemStack(WaterworksItems.itemFireworkAntiRain);

		int multiplierOld = 0;
		if (isFireworks) {
			final ItemStack rocket = inv.getStackInSlot(rocketStack);
			CompoundNBT nbtCompound = rocket.getTag();

			if (nbtCompound == null) {
				nbtCompound = new CompoundNBT();
			}
			if (multiplierOld + multiplierAdd > WaterworksConfig.rockets.clearSkyMaxMultiplier) {
				return false;
			}
			nbtCompound.setInt("ANTIRAIN", multiplierOld + multiplierAdd);
			this.resultItem.setTag(nbtCompound);
			return true;
		}

		if (isAntiRainRocket) {
			final ItemStack rocket = inv.getStackInSlot(rocketStack);
			final CompoundNBT nbtCompound = rocket.getTag();
			CompoundNBT newTag = new CompoundNBT();
			if (nbtCompound != null) {
				newTag = nbtCompound.copy();
				if (nbtCompound.hasKey("ANTIRAIN")) {
					multiplierOld = nbtCompound.getInt("ANTIRAIN");
				}
			}
			if ((multiplierOld + multiplierAdd) > WaterworksConfig.rockets.clearSkyMaxMultiplier) {
				return false;
			}
			newTag.setInt("ANTIRAIN", multiplierOld + multiplierAdd);
			this.resultItem.setTag(newTag);
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
