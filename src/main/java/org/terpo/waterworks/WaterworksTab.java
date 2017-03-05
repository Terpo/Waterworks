package org.terpo.waterworks;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.init.WaterworksItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class WaterworksTab extends CreativeTabs {

	public WaterworksTab() {
		super(WaterworksReference.MODID);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(WaterworksItems.iron_mesh);
	}

}
