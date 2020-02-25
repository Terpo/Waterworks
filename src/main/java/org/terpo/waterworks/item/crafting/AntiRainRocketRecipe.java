package org.terpo.waterworks.item.crafting;

import java.util.Arrays;
import java.util.List;

import org.terpo.waterworks.Config;
import org.terpo.waterworks.setup.Registration;

import net.minecraft.block.Blocks;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class AntiRainRocketRecipe extends SpecialRecipe {

	private static final String NBT_ANTIRAIN = "ANTIRAIN";

	private final boolean recipeEnabled;

	private ItemStack resultItem = ItemStack.EMPTY;
	private final List<Item> validItems;

	public AntiRainRocketRecipe(ResourceLocation idIn) {
		super(idIn);
		this.validItems = Arrays.asList(Items.FIREWORK_ROCKET, Registration.fireworkAntiRainItem.get(),
				Item.BLOCK_TO_ITEM.get(Blocks.SPONGE));
		this.recipeEnabled = Config.recipes.getRecipeAntiRainRocket();
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		this.resultItem = ItemStack.EMPTY;
		if (!this.recipeEnabled) {
			return false;
		}

		int rocketStack = -1;
		int isFireworks = 0;
		int isAntiRainRocket = 0;
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

			if (item == Registration.fireworkAntiRainItem.get()) {
				isAntiRainRocket++;
			}

			if (item == Items.FIREWORK_ROCKET || item == Registration.fireworkAntiRainItem.get()) {
				rocketStack = i;
			} else if (item == Item.BLOCK_TO_ITEM.get(Blocks.SPONGE)) {
				multiplierAdd++;
			}
		}

		if ((isAntiRainRocket + isFireworks) > 1 || multiplierAdd == 0) {
			return false;
		}

		this.resultItem = new ItemStack(Registration.fireworkAntiRainItem.get());

		if (isFireworks > 0) {
			return handleFireworksRocket(inv, rocketStack, multiplierAdd);
		}

		if (isAntiRainRocket > 0) {
			return handleAntiRainRocket(inv, rocketStack, multiplierAdd);
		}

		return false;
	}

	protected boolean validateStack(final ItemStack stack, final Item item) {
		return !stack.isEmpty() && !this.validItems.contains(item);
	}

	protected boolean handleFireworksRocket(CraftingInventory inv, int rocketStack, int multiplierAdd) {
		final int multiplierOld = 0;
		final ItemStack rocket = inv.getStackInSlot(rocketStack);
		CompoundNBT nbtCompound = rocket.getTag();

		if (nbtCompound == null) {
			nbtCompound = new CompoundNBT();
		}
		if (multiplierOld + multiplierAdd > Config.rockets.getClearSkyMaxMultiplier()) {
			return false;
		}
		nbtCompound.putInt(NBT_ANTIRAIN, multiplierOld + multiplierAdd);
		this.resultItem.setTag(nbtCompound);
		return true;
	}

	protected boolean handleAntiRainRocket(CraftingInventory inv, int rocketStack, int multiplierAdd) {
		int multiplierOld = 0;
		final ItemStack rocket = inv.getStackInSlot(rocketStack);
		final CompoundNBT nbtCompound = rocket.getTag();
		CompoundNBT newTag = new CompoundNBT();
		if (nbtCompound != null) {
			newTag = nbtCompound.copy();
			if (nbtCompound.contains(NBT_ANTIRAIN)) {
				multiplierOld = nbtCompound.getInt(NBT_ANTIRAIN);
			}
		}
		if ((multiplierOld + multiplierAdd) > Config.rockets.getClearSkyMaxMultiplier()) {
			return false;
		}
		newTag.putInt(NBT_ANTIRAIN, multiplierOld + multiplierAdd);
		this.resultItem.setTag(newTag);
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
		return Registration.fireworkAntiRainRecipe.get();
	}

}
