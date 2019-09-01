package org.terpo.waterworks.item.crafting;

import java.util.function.Function;

import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.init.WaterworksItems;

import net.minecraft.block.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class AntiRainRocketRecipe extends SpecialRecipeSerializer<IRecipe<?>> implements IRecipe {
	public AntiRainRocketRecipe(Function<ResourceLocation, IRecipe<?>> function) {
		super(function);
	}

	private ItemStack resultItem = ItemStack.EMPTY;
	@Override
	public boolean matches(IInventory inv, World worldIn) {
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
			if (itemstack.getItem() == Item.BLOCK_TO_ITEM.get(Blocks.SPONGE)) {
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
			nbtCompound.putInt("ANTIRAIN", multiplierOld + multiplierAdd);
			this.resultItem.setTag(nbtCompound);
			return true;
		}

		if (isAntiRainRocket) {
			final ItemStack rocket = inv.getStackInSlot(rocketStack);
			final CompoundNBT nbtCompound = rocket.getTag();
			CompoundNBT newTag = new CompoundNBT();
			if (nbtCompound != null) {
				newTag = nbtCompound.copy();
				if (nbtCompound.hasUniqueId("ANTIRAIN")) {
					multiplierOld = nbtCompound.getInt("ANTIRAIN");
				}
			}
			if ((multiplierOld + multiplierAdd) > WaterworksConfig.rockets.clearSkyMaxMultiplier) {
				return false;
			}
			newTag.putInt("ANTIRAIN", multiplierOld + multiplierAdd);
			this.resultItem.setTag(newTag);
			return true;
		}

		return false;
	}

	@Override
	public ItemStack getCraftingResult(IInventory inv) {
		return this.resultItem.copy();
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.resultItem;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 1;
	}

	@Override
	public ResourceLocation getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRecipeSerializer getSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRecipeType getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
