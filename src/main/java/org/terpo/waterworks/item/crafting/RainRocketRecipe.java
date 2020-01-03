package org.terpo.waterworks.item.crafting;

import java.util.Arrays;
import java.util.List;

import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.init.WaterworksItems;

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

public class RainRocketRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
	private static final String NBT_RAIN = "RAIN";
	private ItemStack resultItem = ItemStack.EMPTY;
	private final List<Item> validItems;

	public RainRocketRecipe() {
		super();
		this.validItems = Arrays.asList(Items.FIREWORKS, WaterworksItems.firework_rain, Items.GHAST_TEAR);
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
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

			if (item == Items.FIREWORKS) {
				isFireworks++;
			}

			if (item == WaterworksItems.firework_rain) {
				isRainRocket++;
			}

			if (item == Items.FIREWORKS || item == WaterworksItems.firework_rain) {
				rocketStack = i;
			} else if (item == Items.GHAST_TEAR) {
				multiplierAdd++;
			}
		}

		if ((isRainRocket + isFireworks) > 1 || multiplierAdd == 0) {
			return false;
		}

		this.resultItem = new ItemStack(WaterworksItems.firework_rain);

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

	protected boolean handleRainRocket(InventoryCrafting inv, int rocketStack, int multiplierAdd) {
		int multiplierOld = 0;
		final ItemStack rocket = inv.getStackInSlot(rocketStack);
		final NBTTagCompound nbtCompound = rocket.getTagCompound();
		NBTTagCompound newTag = new NBTTagCompound();
		if (nbtCompound != null) {
			newTag = nbtCompound.copy();
			if (nbtCompound.hasKey(NBT_RAIN)) {
				multiplierOld = nbtCompound.getInteger(NBT_RAIN);
			}
		}
		if ((multiplierOld + multiplierAdd) > WaterworksConfig.rockets.rainMaxMultiplier) {
			return false;
		}
		newTag.setInteger(NBT_RAIN, multiplierOld + multiplierAdd);
		this.resultItem.setTagCompound(newTag);
		return true;
	}

	protected boolean handleFireworksRocket(InventoryCrafting inv, int rocketStack, int multiplierAdd) {
		final int multiplierOld = 0;
		final ItemStack rocket = inv.getStackInSlot(rocketStack);
		NBTTagCompound nbtCompound = rocket.getTagCompound();

		if (nbtCompound == null) {
			nbtCompound = new NBTTagCompound();
		}
		if (multiplierOld + multiplierAdd > WaterworksConfig.rockets.rainMaxMultiplier) {
			return false;
		}
		nbtCompound.setInteger(NBT_RAIN, multiplierOld + multiplierAdd);
		this.resultItem.setTagCompound(nbtCompound);
		return true;
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
