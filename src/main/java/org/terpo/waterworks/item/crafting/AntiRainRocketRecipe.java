package org.terpo.waterworks.item.crafting;

import java.util.Arrays;
import java.util.List;

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
	private static final String NBT_ANTIRAIN = "ANTIRAIN";

	private final List<Item> validItems;

	public AntiRainRocketRecipe() {
		super();
		this.validItems = Arrays.asList(Items.FIREWORKS, WaterworksItems.firework_anti_rain,
				Item.getItemFromBlock(Blocks.SPONGE));
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		this.resultItem = ItemStack.EMPTY;

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

			if (item == Items.FIREWORKS) {
				isFireworks++;
			}

			if (item == WaterworksItems.firework_anti_rain) {
				isAntiRainRocket++;
			}

			if (item == Items.FIREWORKS || item == WaterworksItems.firework_anti_rain) {
				rocketStack = i;
			} else if (item == Item.getItemFromBlock(Blocks.SPONGE)) {
				multiplierAdd++;
			}
		}

		if ((isAntiRainRocket + isFireworks) > 1 || multiplierAdd == 0) {
			return false;
		}

		this.resultItem = new ItemStack(WaterworksItems.firework_anti_rain);

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

	protected boolean handleFireworksRocket(InventoryCrafting inv, int rocketStack, int multiplierAdd) {
		final int multiplierOld = 0;
		final ItemStack rocket = inv.getStackInSlot(rocketStack);
		NBTTagCompound nbtCompound = rocket.getTagCompound();

		if (nbtCompound == null) {
			nbtCompound = new NBTTagCompound();
		}
		if (multiplierOld + multiplierAdd > WaterworksConfig.rockets.clearSkyMaxMultiplier) {
			return false;
		}
		nbtCompound.setInteger(NBT_ANTIRAIN, multiplierOld + multiplierAdd);
		this.resultItem.setTagCompound(nbtCompound);
		return true;
	}

	protected boolean handleAntiRainRocket(InventoryCrafting inv, int rocketStack, int multiplierAdd) {
		int multiplierOld = 0;
		final ItemStack rocket = inv.getStackInSlot(rocketStack);
		final NBTTagCompound nbtCompound = rocket.getTagCompound();
		NBTTagCompound newTag = new NBTTagCompound();
		if (nbtCompound != null) {
			newTag = nbtCompound.copy();
			if (nbtCompound.hasKey(NBT_ANTIRAIN)) {
				multiplierOld = nbtCompound.getInteger(NBT_ANTIRAIN);
			}
		}
		if ((multiplierOld + multiplierAdd) > WaterworksConfig.rockets.clearSkyMaxMultiplier) {
			return false;
		}
		newTag.setInteger(NBT_ANTIRAIN, multiplierOld + multiplierAdd);
		this.resultItem.setTagCompound(newTag);
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
