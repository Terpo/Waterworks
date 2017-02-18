package org.terpo.waterworks.tileentity;

import java.util.Random;

import org.terpo.waterworks.Waterworks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileWaterworks extends TileEntity implements ITickable {

	private static final Random random = new Random();
	private int currentTick = random.nextInt(256);

	public static final int SIZE = 2;

	// TileEntity

	// This item handler will hold our nine inventory slots
	private final ItemStackHandler itemStackHandler = new ItemStackHandler(TileWaterworks.SIZE) {
		@Override
		protected void onContentsChanged(int slot) {
			// We need to tell the tile entity that something has changed so
			// that the chest contents is persisted
			TileWaterworks.this.markDirty();
		}
	};

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("items", this.itemStackHandler.serializeNBT());
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("items")) {
			this.itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
		}

	}

	public boolean canInteractWith(EntityPlayer playerIn) {
		// If we are too far away from this tile entity you cannot use it
		return !isInvalid() && playerIn.getDistanceSq(this.pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked") // TODO NEEDED?
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) this.itemStackHandler;
		}
		return super.getCapability(capability, facing);
	}

	// Client Sync
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		// called when the tile entity itself wants to sync to the client
		final NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		final int metadata = getBlockMetadata();

		Waterworks.LOGGER.fatal("getUpdatePacket");
		return new SPacketUpdateTileEntity(this.pos, metadata, nbtTagCompound);
	}
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
		Waterworks.LOGGER.fatal("onDataPacket");
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		// called whenever the chunkdata is sent to the client
		final NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		Waterworks.LOGGER.fatal("getUpdateTag");
		return nbtTagCompound;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) { // on chunk load CLIENT
		this.readFromNBT(tag);
		Waterworks.LOGGER.fatal("handleUpdateTag");
	}
	// Client Sync End

	// TileEntity End

	// ITackable
	@Override
	public final void update() {
		this.currentTick++;

		if (!this.worldObj.isRemote) {
			updateServerSide();
		} else {
			updateClientSide();
		}
	}

	protected void updateClientSide() {
		// to be implemented
	}

	protected void updateServerSide() {
		// to be implemented
	}

	protected boolean needsUpdate(int tickInterval) {
		return this.currentTick % tickInterval == 0;
	}
	// ITickable End
}
