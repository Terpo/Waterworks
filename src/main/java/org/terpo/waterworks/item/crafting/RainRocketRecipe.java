package org.terpo.waterworks.item.crafting;

import java.util.Arrays;
import java.util.List;

import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.init.WaterworksItems;
import org.terpo.waterworks.init.WaterworksRecipes;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RainRocketRecipe extends SpecialRecipe {
	private ItemStack resultItem = ItemStack.EMPTY;
	private final List<Item> validItems;
	public RainRocketRecipe(ResourceLocation idIn) {
		super(idIn);
		this.validItems = Arrays.asList(Items.FIREWORK_ROCKET, WaterworksItems.itemFireworkRain, Items.GHAST_TEAR);
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		this.resultItem = ItemStack.EMPTY;

		int rocketStack = -1;
		int isFireworks = 0;
		int isRainRocket = 0;
		int multiplierAdd = 0;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			final ItemStack stack = inv.getStackInSlot(i);
			final Item item = stack.getItem();

			if (validateStack(stack, item)) {
				return false;
			}

			if (item == Items.FIREWORK_ROCKET) {
				isFireworks++;
			}

			if (item == WaterworksItems.itemFireworkRain) {
				isRainRocket++;
			}

			if (item == Items.FIREWORK_ROCKET || item == WaterworksItems.itemFireworkRain) {
				rocketStack = i;
			} else if (item == Items.GHAST_TEAR) {
				multiplierAdd++;
			}
		}

		if ((isRainRocket + isFireworks) > 1 || multiplierAdd == 0) {
			return false;
		}

		this.resultItem = new ItemStack(WaterworksItems.itemFireworkRain);

		if (isFireworks > 0) {
			return handleFireworksRocket(inv, rocketStack, multiplierAdd);
		}

		if (isRainRocket > 0) {
			return handleRainRocket(inv, rocketStack, multiplierAdd);
		}

		return false;
	}

	protected boolean validateStack(ItemStack stack, final Item item) {
		return !stack.isEmpty() && !this.validItems.contains(item);
	}

	protected boolean handleRainRocket(CraftingInventory inv, int rocketStack, int multiplierAdd) {
		int multiplierOld = 0;
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

	protected boolean handleFireworksRocket(CraftingInventory inv, int rocketStack, int multiplierAdd) {
		final int multiplierOld = 0;
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
