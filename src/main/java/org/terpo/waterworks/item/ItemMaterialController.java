package org.terpo.waterworks.item;

import org.terpo.waterworks.setup.CommonSetup;

import net.minecraft.item.Item;

public class ItemMaterialController extends Item {
	public ItemMaterialController() {
		super((new Item.Properties()).group(CommonSetup.CREATIVE_TAB));
	}

}
