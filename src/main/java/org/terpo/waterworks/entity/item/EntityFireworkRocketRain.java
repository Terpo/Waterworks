package org.terpo.waterworks.entity.item;

import org.terpo.waterworks.init.WaterworksConfig;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityFireworkRocketRain extends EntityFireworkRocket {
	private int rainDuration = WaterworksConfig.RAIN_DURATION;

	public EntityFireworkRocketRain(World worldIn, double x, double y, double z, ItemStack givenItem) {
		super(worldIn, x, y, z, givenItem);
		if (givenItem.hasTagCompound()) {
			final NBTTagCompound tag = givenItem.getTagCompound();
			int rainMultiplier = -1;
			if (tag.hasKey("RAIN")) {
				rainMultiplier = tag.getInteger("RAIN");
			}
			if (rainMultiplier != -1) {
				this.rainDuration = WaterworksConfig.RAIN_DURATION * rainMultiplier;
			}
		}
	}

	public EntityFireworkRocketRain(World worldIn, ItemStack givenItem, EntityLivingBase entityLivingBase) {
		super(worldIn, givenItem, entityLivingBase);
	}

	public EntityFireworkRocketRain(World worldIn) {
		super(worldIn);
	}

	@Override
	public void setDead() {
		final World worldIn = this.getEntityWorld();
		worldIn.getWorldInfo().setRaining(true);
		worldIn.getWorldInfo().setRainTime(this.rainDuration);
		this.isDead = true;
	}
}
