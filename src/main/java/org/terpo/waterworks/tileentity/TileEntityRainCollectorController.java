package org.terpo.waterworks.tileentity;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.helper.AreaHelper;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityRainCollectorController extends TileEntityRainTankWood {
	private final int controllerRange = 2;
	private final int areaCount = (int) Math.pow(this.controllerRange * 2 + 1, 2);
	private BlockPos[] rainCollectorBlocks = new BlockPos[this.areaCount];
	protected int fillMultiplier = 1;
	private boolean isReset = false;

	public TileEntityRainCollectorController() {
		super(100, 32000);
	}

	@Override
	protected void updateServerSide() {
		if (this.isReset && needsUpdate(10)) {
			this.isDirty = true;
			resetController();
			findRainCollectors();
			this.isReset = false;
		}
		super.updateServerSide();
	}

	public void findRainCollectors() {
		resetController();
		this.fillMultiplier = getAllConnectedBlocks();
		this.RESOURCE_WATER = getWaterFluidStack(this.fillMultiplier * 100);
		// debugCollectors();
	}

	public void debugCollectors() {
		Waterworks.LOGGER.info("Fill Multiplier @" + this.fillMultiplier);
		for (final BlockPos blockPos : this.rainCollectorBlocks) {
			if (blockPos != null) {
				Waterworks.LOGGER.info("Collector @" + blockPos.toString());
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		int connectedBlocks = 0;
		// TagList of Integer Tags:
		final NBTTagList list = new NBTTagList();
		for (int i = 0; i < this.areaCount; i++) {
			if (this.rainCollectorBlocks[i] != null) {
				final NBTTagCompound nbt = new NBTTagCompound();
				nbt.setLong("collectorPos", this.rainCollectorBlocks[i].toLong());
				list.appendTag(nbt);
				connectedBlocks++;
			}
		}
		compound.setInteger("connectedBlocks", connectedBlocks);
		compound.setTag("collectorPosList", list);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("connectedBlocks")) {
			this.fillMultiplier = compound.getInteger("connectedBlocks");
			this.RESOURCE_WATER = getWaterFluidStack(this.fillMultiplier * 100);
			if (compound.hasKey("collectorPosList")) {
				final NBTTagList list = compound.getTagList("collectorPosList", 10);
				if (list.tagCount() > this.areaCount) {
					this.rainCollectorBlocks = new BlockPos[list.tagCount()];
					this.isReset = true;
				}
				for (int i = 0; i < list.tagCount(); i++) {
					final NBTTagCompound nbt = list.getCompoundTagAt(i);
					this.rainCollectorBlocks[i] = (BlockPos.fromLong(nbt.getLong("collectorPos")));
				}
			}
		}
	}

	private boolean isValidBlock(BlockPos blockPos, int currentIndex) {
		final TileEntity tileEntity = this.world.getTileEntity(blockPos);
		// is correct class?
		final boolean ret = tileEntity instanceof TileEntityRainCollector
				&& AreaHelper.isInRange2D(blockPos, this.pos, this.controllerRange);

		if (!ret) {
			return false;
		}

		if (((TileEntityRainCollector) tileEntity).hasController()) {
			return false;
		}

		// if yes, then is it already in our list?
		if (ret) {
			for (int i = 0; i < currentIndex; i++) {
				if (this.rainCollectorBlocks[i].equals(blockPos)) {
					return false;
				}
			}
		}
		return ret;

	}

	private int getAllConnectedBlocks() {

		// our water tank is the first found block
		int foundBlockCount = 1;
		this.rainCollectorBlocks[0] = this.pos;

		// iterate over all blocks in our "found"-list
		int i = 0;
		while (i < foundBlockCount) {
			// left of current block
			BlockPos tempPos = this.rainCollectorBlocks[i].add(-1, 0, 0);
			if (isValidBlock(tempPos, foundBlockCount)) {
				this.rainCollectorBlocks[foundBlockCount++] = tempPos;
				((TileEntityRainCollector) (this.world.getTileEntity(tempPos))).setController(this);
			}

			// right of current block
			tempPos = this.rainCollectorBlocks[i].add(1, 0, 0);
			if (isValidBlock(tempPos, foundBlockCount)) {
				this.rainCollectorBlocks[foundBlockCount++] = tempPos;
				((TileEntityRainCollector) (this.world.getTileEntity(tempPos))).setController(this);
			}

			// in front of current block
			tempPos = this.rainCollectorBlocks[i].add(0, 0, 1);
			if (isValidBlock(tempPos, foundBlockCount)) {
				this.rainCollectorBlocks[foundBlockCount++] = tempPos;
				((TileEntityRainCollector) (this.world.getTileEntity(tempPos))).setController(this);
			}

			// behind current block
			tempPos = this.rainCollectorBlocks[i].add(0, 0, -1);
			if (isValidBlock(tempPos, foundBlockCount)) {
				this.rainCollectorBlocks[foundBlockCount++] = tempPos;
				((TileEntityRainCollector) (this.world.getTileEntity(tempPos))).setController(this);
			}
			i++;
		}
		return foundBlockCount;

	}

	public void removeCollector(BlockPos collectorPos) {
		for (final BlockPos blockPos : this.rainCollectorBlocks) {
			if (blockPos == null) {
				continue;
			}
			if (blockPos.equals(collectorPos)) {
				this.isReset = true;
				return;
			}
		}
	}

	public void resetController() {
		Waterworks.LOGGER.info("resetting for " + this.areaCount);
		for (int i = 1; i < this.rainCollectorBlocks.length; i++) {
			final BlockPos blockPos = this.rainCollectorBlocks[i];
			if (blockPos != null) {
				final TileEntity tile = this.world.getTileEntity(blockPos);
				if (tile instanceof TileEntityRainCollector) {
					((TileEntityRainCollector) tile).breakConnection(this);
				}
			}
		}
		this.rainCollectorBlocks = new BlockPos[this.areaCount];
	}

}
