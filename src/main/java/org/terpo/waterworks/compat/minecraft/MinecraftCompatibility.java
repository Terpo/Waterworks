package org.terpo.waterworks.compat.minecraft;

import org.terpo.waterworks.entity.item.EntityFireworkRocketAntiRain;
import org.terpo.waterworks.entity.item.EntityFireworkRocketRain;
import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.init.WaterworksItems;

import net.minecraft.block.BlockDispenser;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MinecraftCompatibility {

	public static void registerWeatherRocketDispenserBehavior() {
		if (WaterworksConfig.register.antiRainRocket) {
			BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(WaterworksItems.firework_anti_rain,
					new FireworkRocketAntiRainDispenserBehavior(MinecraftCompatibility::spawnAntiRainRocket));
		}
		if (WaterworksConfig.register.rainRocket) {
			BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(WaterworksItems.firework_rain,
					new FireworkRocketRainDispenserBehavior(MinecraftCompatibility::spawnRainRocket));
		}
	}

	private static EntityFireworkRocketRain spawnRainRocket(World worldIn, Vec3d v, ItemStack itemstack) {
		return new EntityFireworkRocketRain(worldIn, v.x, v.y, v.z, itemstack);
	}
	private static EntityFireworkRocketAntiRain spawnAntiRainRocket(World worldIn, Vec3d v, ItemStack itemstack) {
		return new EntityFireworkRocketAntiRain(worldIn, v.x, v.y, v.z, itemstack);
	}

	private MinecraftCompatibility() {
		// hidden
	}

}