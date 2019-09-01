package org.terpo.waterworks.item.crafting;

import java.util.function.Function;

import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.init.WaterworksItems;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
public class RainRocketRecipe extends SpecialRecipeSerializer<IRecipe<?>> implements IRecipe {

	public RainRocketRecipe(Function<ResourceLocation, IRecipe<?>> p_i50024_1_) {
		super(p_i50024_1_);
	}

	private ItemStack resultItem = ItemStack.EMPTY;

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
	public boolean matches(IInventory inv, World worldIn) {
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
			if (multiplierOld + multiplierAdd > WaterworksConfig.rockets.rainMaxMultiplier) {
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
				if (nbtCompound.hasUniqueId("RAIN")) {
					multiplierOld = nbtCompound.getInt("RAIN");
				}
			}
			if ((multiplierOld + multiplierAdd) > WaterworksConfig.rockets.rainMaxMultiplier) {
				return false;
			}
			newTag.putInt("RAIN", multiplierOld + multiplierAdd);
			this.resultItem.setTag(newTag);
			return true;
		}

		return false;
	}

	@Override
	public ResourceLocation getId() {
		return null;
	}

	@Override
	public IRecipeSerializer getSerializer() {
		return this;
	}

	@Override
	public IRecipeType getType() {
		// TODO Auto-generated method stub
		return null;
	}
}
