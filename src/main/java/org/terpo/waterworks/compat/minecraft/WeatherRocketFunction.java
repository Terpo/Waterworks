package org.terpo.waterworks.compat.minecraft;

/**
 * @param <R> Rocket Entity
 * @param <W> World
 * @param <V> Vec3D
 * @param <S> ItemStack
 */
@FunctionalInterface
public interface WeatherRocketFunction<R, W, V, S> {
	R apply(W t, V v, S s);
}