package org.terpo.waterworks.compat.jei;

import java.util.ArrayList;
import java.util.List;

import org.terpo.waterworks.Config;
import org.terpo.waterworks.api.constants.Reference;
import org.terpo.waterworks.api.constants.RegistryNames;
import org.terpo.waterworks.setup.Registration;

import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class JEIRocketRecipes {

	private static final String JEI = ".jei";
	private static final String ROCKET_GROUP = Reference.MODID + ".rocket.crafting";

	static void addRocketRecipes(IRecipeRegistration registration) {
		if (Config.rockets.getFireworkRocketsJEIRecipes()) {
			final List<ShapelessRecipe> recipes = new ArrayList<>(2);
			recipes.add(createAntiRainRocketRecipe());
			recipes.add(createRainRocketRecipe());
			registration.addRecipes(recipes, VanillaRecipeCategoryUid.CRAFTING);
		}

	}

	protected static ShapelessRecipe createRainRocketRecipe() {
		final Ingredient rocketInput = Ingredient.fromItems(Registration.fireworkRainItem.get(), Items.FIREWORK_ROCKET);
		final Ingredient rainItem = Ingredient.fromItems(Items.GHAST_TEAR);
		final NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY, rocketInput, rainItem);
		final ItemStack output = new ItemStack(Registration.fireworkRainItem.get());

		final ResourceLocation id2 = new ResourceLocation(Reference.MODID, RegistryNames.FIREWORK_RAIN + JEI);

		return new ShapelessRecipe(id2, ROCKET_GROUP, output, inputs);

	}

	protected static ShapelessRecipe createAntiRainRocketRecipe() {
		final Ingredient rocketInput = Ingredient.fromItems(Registration.fireworkAntiRainItem.get(), Items.FIREWORK_ROCKET);
		final Ingredient antiRainItem = Ingredient.fromItems(Item.BLOCK_TO_ITEM.get(Blocks.SPONGE));
		final NonNullList<Ingredient> inputs = NonNullList.from(Ingredient.EMPTY, rocketInput, antiRainItem);
		final ItemStack output = new ItemStack(Registration.fireworkAntiRainItem.get());

		final ResourceLocation id = new ResourceLocation(Reference.MODID, RegistryNames.FIREWORK_ANTI_RAIN + JEI);

		return new ShapelessRecipe(id, ROCKET_GROUP, output, inputs);
	}

	private JEIRocketRecipes() {
		// hidden
	}
}
