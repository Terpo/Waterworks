package org.terpo.waterworks.compat.jei.rockets;

import java.util.ArrayList;
import java.util.List;

import org.terpo.waterworks.init.WaterworksItems;
import org.terpo.waterworks.item.crafting.AntiRainRocketRecipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class JEIAntiRainRocketRecipeWrapperFactory implements IRecipeWrapperFactory<AntiRainRocketRecipe> {

	private class RecipeWrapper implements IRecipeWrapper {

		public RecipeWrapper() {
			//
		}

		@Override
		public void getIngredients(IIngredients ingredients) {
			final List<List<ItemStack>> inputs = new ArrayList<>();
			final List<ItemStack> inputFirst = new ArrayList<>();
			final List<ItemStack> inputOthers = new ArrayList<>();

			inputFirst.add(new ItemStack(Items.FIREWORKS));
			inputFirst.add(new ItemStack(WaterworksItems.firework_anti_rain));
			inputOthers.add(new ItemStack(Blocks.SPONGE));
			inputs.add(inputFirst);
			inputs.add(inputOthers);

			ingredients.setInputLists(ItemStack.class, inputs);

			final ItemStack rocket = new ItemStack(WaterworksItems.firework_anti_rain);
			final List<ItemStack> outputList = new ArrayList<>();
			final NBTTagCompound newTag = new NBTTagCompound();
			newTag.setInteger("ANTIRAIN", 1);
			rocket.setTagCompound(newTag);
			outputList.add(rocket);

			ingredients.setOutputs(ItemStack.class, outputList);
		}

	}

	@Override
	public IRecipeWrapper getRecipeWrapper(AntiRainRocketRecipe recipe) {
		return new RecipeWrapper();
	}
}
