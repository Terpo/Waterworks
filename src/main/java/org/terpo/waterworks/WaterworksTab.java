package org.terpo.waterworks;

import org.terpo.waterworks.init.WaterworksItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class WaterworksTab extends CreativeTabs {

	public WaterworksTab() {
		super(WaterworksReference.MODID);
	}

	@Override
	public Item getTabIconItem() {
		return WaterworksItems.iron_mesh;
	}

}
