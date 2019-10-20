package org.terpo.waterworks.item.crafting;

import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.init.WaterworksItems;
import org.terpo.waterworks.init.WaterworksRecipes;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RainRocketRecipe extends SpecialRecipe {
	private ItemStack resultItem = ItemStack.EMPTY;

	public RainRocketRecipe(ResourceLocation idIn) {
		super(idIn);
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		this.resultItem = ItemStack.EMPTY;

		int rocketStack = -1;
		boolean isFireworks = false;
		boolean isRainRocket = false;
		int multiplierAdd = 0;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			final ItemStack itemstack = inv.getStackInSlot(i);

			if (itemstack.getItem() == Items.FIREWORK_ROCKET) {
				isFireworks = true;
				rocketStack = i;
				continue;
			}
			if (itemstack.getItem() == WaterworksItems.itemFireworkRain) {
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
		this.resultItem = new ItemStack(WaterworksItems.itemFireworkRain);

		int multiplierOld = 0;
		if (isFireworks) {
			final ItemStack rocket = inv.getStackInSlot(rocketStack);
			CompoundNBT nbtCompound = rocket.getTag();

			if (nbtCompound == null) {
				nbtCompound = new CompoundNBT();
			}
			if (multiplierOld + multiplierAdd > WaterworksConfig.rockets.getRainMaxMultiplier()) {
				return false;
			}
			nbtCompound.putInt("RAIN", multiplierOld + multiplierAdd);
			this.resultItem.setTag(nbtCompound);
			return true;
		}

		if (isRainRocket) {
			final ItemStack rocket = inv.getStackInSlot(rocketStack);
			final CompoundNBT nbtCompound = rocket.getTag();
			CompoundNBT newTag = new CompoundNBT();
			if (nbtCompound != null) {
				newTag = nbtCompound.copy();
				if (nbtCompound.contains("RAIN")) {
					multiplierOld = nbtCompound.getInt("RAIN");
				}
			}
			if ((multiplierOld + multiplierAdd) > WaterworksConfig.rockets.getRainMaxMultiplier()) {
				return false;
			}
			newTag.putInt("RAIN", multiplierOld + multiplierAdd);
			this.resultItem.setTag(newTag);
			return true;
		}

		return false;
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		return this.resultItem.copy();
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 1;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return WaterworksRecipes.recipeFireworkRain;
	}
}
