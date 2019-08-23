package org.terpo.waterworks;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.init.WaterworksBlocks;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class WaterworksTab extends ItemGroup {

	public WaterworksTab() {
		super(WaterworksReference.MODID);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(WaterworksBlocks.groundwaterPump);
	}

}
