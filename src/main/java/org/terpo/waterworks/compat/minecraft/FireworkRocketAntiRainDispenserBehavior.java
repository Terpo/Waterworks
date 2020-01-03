package org.terpo.waterworks.compat.minecraft;

import org.terpo.waterworks.entity.item.EntityFireworkRocketAntiRain;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FireworkRocketAntiRainDispenserBehavior extends BehaviorDefaultDispenseItem {

	private final WeatherRocketFunction<EntityFireworkRocketAntiRain, World, Vec3d, ItemStack> createEntityFunction;

	public FireworkRocketAntiRainDispenserBehavior(
			WeatherRocketFunction<EntityFireworkRocketAntiRain, World, Vec3d, ItemStack> createEntityFunction) {
		this.createEntityFunction = createEntityFunction;
	}

	@Override
	public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		final EnumFacing direction = source.getBlockState().getValue(BlockDispenser.FACING);
		source.getWorld()
				.spawnEntity(this.createEntityFunction.apply(
						source.getWorld(), new Vec3d(source.getX() + direction.getFrontOffsetX(),
								source.getBlockPos().getY() + 0.2D, source.getZ() + direction.getFrontOffsetZ()),
						stack));
		stack.shrink(1);
		return stack;
	}

	@Override
	protected void playDispenseSound(IBlockSource source) {
		source.getWorld().playEvent(1004, source.getBlockPos(), 0);
	}
}