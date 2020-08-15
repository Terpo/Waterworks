package org.terpo.waterworks.compat.minecraft;

import org.terpo.waterworks.entity.item.EntityFireworkRocketAntiRain;
import org.terpo.waterworks.entity.item.EntityFireworkRocketRain;
import org.terpo.waterworks.setup.Registration;

import net.minecraft.block.DispenserBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class MinecraftCompatibility {

	public static void registerWeatherRocketDispenserBehavior() {
		DispenserBlock.registerDispenseBehavior(Registration.fireworkAntiRainItem.get(),
				new WeatherRocketDispenserBehavior(MinecraftCompatibility::spawnAntiRainRocket));
		DispenserBlock.registerDispenseBehavior(Registration.fireworkRainItem.get(),
				new WeatherRocketDispenserBehavior(MinecraftCompatibility::spawnRainRocket));
	}

	private static EntityFireworkRocketRain spawnRainRocket(World worldIn, Vector3d v, ItemStack itemstack) {
		return new EntityFireworkRocketRain(worldIn, v.x, v.y, v.z, itemstack);
	}
	private static EntityFireworkRocketAntiRain spawnAntiRainRocket(World worldIn, Vector3d v, ItemStack itemstack) {
		return new EntityFireworkRocketAntiRain(worldIn, v.x, v.y, v.z, itemstack);
	}

	private MinecraftCompatibility() {
		// hidden
	}

}
