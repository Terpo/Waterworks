package org.terpo.waterworks.compat.minecraft;

import org.terpo.waterworks.entity.item.EntityWeatherFireworkRocket;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class WeatherRocketDispenserBehavior extends DefaultDispenseItemBehavior {

	private final WeatherRocketFunction<EntityWeatherFireworkRocket, World, Vector3d, ItemStack> createEntityFunction;

	public WeatherRocketDispenserBehavior(
			WeatherRocketFunction<EntityWeatherFireworkRocket, World, Vector3d, ItemStack> createEntityFunction) {
		this.createEntityFunction = createEntityFunction;
	}

	@Override
	public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		final Direction direction = source.getBlockState().get(DispenserBlock.FACING);
		source.getWorld().addEntity(this.createEntityFunction.apply(source.getWorld(), new Vector3d(source.getX() + direction.getXOffset(),
				source.getBlockPos().getY() + 0.2D, source.getZ() + direction.getZOffset()), stack));
		stack.shrink(1);
		return stack;
	}

	@Override
	protected void playDispenseSound(IBlockSource source) {
		source.getWorld().playEvent(1004, source.getBlockPos(), 0);
	}
}