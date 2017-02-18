package org.terpo.waterworks.tileentity;

import java.util.Random;

import org.terpo.waterworks.Waterworks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileWaterworksBackup extends TileEntity implements ITickable, IInventory {

	private static final Random random = new Random();
	private int currentTick = random.nextInt(256);

	private final ItemStack[] inv;
	private final int invSize = 2;
	private String invName = "Tank Inventory";

	public TileWaterworksBackup() {
		super();
		this.inv = new ItemStack[this.invSize]; // 0 Input, 1 Output
	}

	// TileEntity
	@Override
	public String getName() {
		return this.invName;
	}

	public void setName(String name) {
		this.invName = name;
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		final NBTTagList list = new NBTTagList();
		for (int i = 0; i < this.getSizeInventory(); ++i) {
			final ItemStack itemstack = this.getStackInSlot(i);
			if (itemstack != null) {
				final NBTTagCompound stackTag = new NBTTagCompound();
				stackTag.setByte("Slot", (byte) i);
				itemstack.writeToNBT(stackTag);
				list.appendTag(stackTag);
			}
		}
		compound.setTag("Items", list);

		if (this.hasCustomName()) {
			compound.setString("CustomName", this.getName());
		}
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		final NBTTagList list = compound.getTagList("Items", 10);
		for (int i = 0; i < list.tagCount(); ++i) {
			final NBTTagCompound stackTag = list.getCompoundTagAt(i);
			final int slot = stackTag.getByte("Slot") & 255;
			this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
		}

		if (compound.hasKey("CustomName", 8)) {
			this.setName(compound.getString("CustomName"));
		}
	}
	// Client Sync
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
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
	public NBTTagCompound getUpdateTag() { // on chunk load SERVER
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

	// IInventory
	@Override
	public int getSizeInventory() {
		return this.invSize;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return (index <= (this.invSize - 1) && index >= 0) ? this.inv[index] : null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (getStackInSlot(index) != null) {
			ItemStack itemstack;
			if (this.getStackInSlot(index).stackSize <= count) {
				itemstack = this.getStackInSlot(index);
				this.setInventorySlotContents(index, null);
				this.markDirty();
				return itemstack;
			}

			itemstack = this.getStackInSlot(index).splitStack(count);

			if (this.getStackInSlot(index).stackSize <= 0) {
				this.setInventorySlotContents(index, null);
			} else {
				// Just to show that changes happened
				this.setInventorySlotContents(index, this.getStackInSlot(index));
			}

			this.markDirty();
			return itemstack;
		}
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		final ItemStack stack = this.getStackInSlot(index);
		this.setInventorySlotContents(index, null);
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index < 0 || index >= this.getSizeInventory()) {
			return;
		}

		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}

		this.inv[index] = stack;
		if (stack != null && stack.stackSize == 0) {
			this.inv[index] = null;
		}
		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.getPos()) == this
				&& player.getDistanceSq(this.pos.add(0.5, 0.5, 0.5)) <= 64;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		//
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		//
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true; // TODO FluidContainers only
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		//
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for (int i = 0; i < this.getSizeInventory(); i++) {
			this.setInventorySlotContents(i, null);
		}
	}
	// IInventory End
}
