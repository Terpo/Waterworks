package org.terpo.waterworks.item;

import org.terpo.waterworks.Waterworks;

import net.minecraft.item.Item;

public class ItemMaterialEnergyAdapter extends Item {
	public ItemMaterialEnergyAdapter() {
		super((new Item.Properties()).group(Waterworks.CREATIVE_TAB));
	}
}
