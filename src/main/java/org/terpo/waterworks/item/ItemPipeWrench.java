package org.terpo.waterworks.item;

import org.terpo.waterworks.Waterworks;

import net.minecraft.item.Item;

public class ItemPipeWrench extends Item {
	public ItemPipeWrench() {
		super((new Item.Properties()).group(Waterworks.CREATIVE_TAB));
	}
}
