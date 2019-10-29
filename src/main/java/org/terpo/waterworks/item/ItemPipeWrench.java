package org.terpo.waterworks.item;

import java.util.Set;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.api.constants.WaterworksConstants;
import org.terpo.waterworks.init.WaterworksBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.ToolItem;
import net.minecraftforge.common.ToolType;

public class ItemPipeWrench extends ToolItem {

	private final Set<Block> effectiveOn;
	public ItemPipeWrench() {
		super(6.0F, -3.1F, ItemTier.IRON, WaterworksBlocks.getAllWaterworksBlocks(),
				new Item.Properties().setNoRepair().maxStackSize(1).group(Waterworks.CREATIVE_TAB).addToolType(
						ToolType.get(WaterworksConstants.WATERWORKS_TOOL_TYPE), ItemTier.IRON.getHarvestLevel()));
		this.effectiveOn = WaterworksBlocks.getAllWaterworksBlocks();
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}
	@Override
	public int getItemEnchantability() {
		return 0;
	}

	@Override
	public boolean canHarvestBlock(BlockState blockIn) {
		if (this.effectiveOn.contains(blockIn.getBlock())) {
			return true;
		}
		return super.canHarvestBlock(blockIn);
	}
}
