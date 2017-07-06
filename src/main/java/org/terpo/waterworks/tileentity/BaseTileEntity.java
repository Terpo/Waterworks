package org.terpo.waterworks.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class BaseTileEntity extends TileEntity {
	protected boolean isDirty = false;

	public BaseTileEntity() {
		// constructor
	}
	// Client Sync
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		// called when the tile entity itself wants to sync to the client
		final NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		final int metadata = getBlockMetadata();
		return new SPacketUpdateTileEntity(this.pos, metadata, nbtTagCompound);
	}
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		// called whenever the chunkdata is sent to the client
		final NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		return nbtTagCompound;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) { // on chunk load CLIENT
		this.readFromNBT(tag);
	}
	// Client Sync End

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}
}
