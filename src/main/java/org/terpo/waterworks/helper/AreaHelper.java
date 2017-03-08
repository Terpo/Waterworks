package org.terpo.waterworks.helper;

import net.minecraft.util.math.BlockPos;

public class AreaHelper {

	public static boolean isInRange2D(BlockPos p1, BlockPos p2, int r) {
		return AreaHelper.isInRange2D(p1.getX(), p1.getZ(), p2.getX() - r, p2.getX() + r, p2.getZ() - r, p2.getZ() + r);
	}

	public static boolean isInRange2D(int posX, int posZ, int tEPosXmin, int tEPosXmax, int tEPosZmin, int tEPosZmax) {
		return (posX >= tEPosXmin && posX <= tEPosXmax && posZ >= tEPosZmin && posZ <= tEPosZmax);
	}
}
