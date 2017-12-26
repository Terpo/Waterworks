package org.terpo.waterworks.item;

import org.terpo.waterworks.api.constants.EnumItemMaterials;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemMaterials extends Item {
	public ItemMaterials() {
		super();
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		for (int i = 0; i < EnumItemMaterials.VALUES.length; i++) {
			items.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack) + "." + EnumItemMaterials.VALUES[stack.getItemDamage()];
	}

}
