package org.terpo.waterworks.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class BaseTileEntity extends TileEntity {
	protected boolean isDirty = false;

	public BaseTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	@Override
	public CompoundNBT getUpdateTag() {
		// called whenever the chunk data is sent to the client
		final CompoundNBT nbtTagCompound = new CompoundNBT();
		write(nbtTagCompound);
		return nbtTagCompound;
	}

	@Override
	public void handleUpdateTag(CompoundNBT tag) { // on chunk load CLIENT
		this.read(tag);
	}
	// Client Sync End

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}
}
