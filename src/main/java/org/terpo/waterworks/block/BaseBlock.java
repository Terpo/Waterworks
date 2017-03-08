package org.terpo.waterworks.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BaseBlock extends Block {

	public BaseBlock(Material materialIn) {
		super(materialIn);
		this.setResistance(2F);
		this.setHardness(2F);
		this.setHarvestLevel("pickaxe", 2);
	}

	public BaseBlock() {
		this(Material.IRON);
	}
}
