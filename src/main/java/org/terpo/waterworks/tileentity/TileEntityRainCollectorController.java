package org.terpo.waterworks.tileentity;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.helper.AreaHelper;
import org.terpo.waterworks.helper.FluidHelper;
import org.terpo.waterworks.init.WaterworksConfig;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityRainCollectorController extends TileEntityRainTankWood {
	private final int controllerRange = WaterworksConfig.rainCollection.rainCollectorRange;
	private final int areaCount = (int) Math.pow(this.controllerRange * 2.0d + 1, 2);
	private BlockPos[] rainCollectorBlocks = new BlockPos[this.areaCount];
	protected int connectedCollectors = 1;

	private boolean isReset = false;

	protected int validCollectors = 0;
	private int currentValidationPos = 0;
	private int countValidCollectors = 0;

	public TileEntityRainCollectorController() {
		super(FluidHelper.getFluidResource(WaterworksConfig.rainCollection.rainCollectorCollectedFluidName,
				WaterworksConfig.rainCollection.rainCollectorFillrate),
				WaterworksConfig.rainCollection.rainCollectorCapacity);
	}

	@Override
	protected void updateServerSide() {
		if (this.isReset && needsUpdate(10)) {
			this.isDirty = true;
			resetController();
			findRainCollectors();
			this.isReset = false;
		}
		if (needsUpdate(15) && this.world.isRaining()) {
			countValidCollectors();
		}
		super.updateServerSide();
	}

	@Override
	protected boolean isRefilling() {
		if (this.world.isRaining()) {
			this.fluidTank.fillInternal(this.fluidResource, true);
			return true;
		}
		return false;
	}

	private void countValidCollectors() {
		final int maxValid = (this.currentValidationPos + 5) > this.areaCount
				? this.areaCount
				: (this.currentValidationPos + 5);
		int i;
		for (i = this.currentValidationPos; i < maxValid; i++) {
			if (this.rainCollectorBlocks[i] != null && this.world.isRainingAt(this.rainCollectorBlocks[i].up())) {
				this.countValidCollectors++;
			}
		}
		this.currentValidationPos = maxValid;
		if (this.currentValidationPos == this.areaCount) {
			this.validCollectors = this.countValidCollectors;
			this.fluidResource = FluidHelper.recalculateFillrate(this.fluidResource,
					this.validCollectors * WaterworksConfig.rainCollection.rainCollectorFillrate,
					WaterworksConfig.rainCollection.rainCollectorCollectedFluidName);
			this.countValidCollectors = 0;
			this.currentValidationPos = 0;
		}
	}

	public int findRainCollectors() {
		resetController();
		this.connectedCollectors = getAllConnectedBlocks();
		this.fluidResource = FluidHelper.recalculateFillrate(this.fluidResource,
				this.validCollectors * WaterworksConfig.rainCollection.rainCollectorFillrate,
				WaterworksConfig.rainCollection.rainCollectorCollectedFluidName);
		return this.connectedCollectors;
	}

	public void debugCollectors() {
		Waterworks.LOGGER.info("Fill Multiplier @" + this.connectedCollectors);
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
		compound.setInteger("validCollectors", this.validCollectors);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("connectedBlocks")) {
			this.connectedCollectors = compound.getInteger("connectedBlocks");
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
			if (compound.hasKey("validCollectors")) {
				this.validCollectors = compound.getInteger("validCollectors");
				this.fluidResource = FluidHelper.recalculateFillrate(this.fluidResource,
						this.validCollectors * WaterworksConfig.rainCollection.rainCollectorFillrate,
						WaterworksConfig.rainCollection.rainCollectorCollectedFluidName);
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

	public int getConnectedCollectors() {
		return this.connectedCollectors;
	}

	public boolean isCollectorListed(BlockPos collectorPos) {
		if (this.rainCollectorBlocks != null) {
			for (int i = 0; i < this.rainCollectorBlocks.length; i++) {
				if (this.rainCollectorBlocks[i] != null && this.rainCollectorBlocks[i].equals(collectorPos)) {
					return true;
				}
			}
			return false;
		}
		return false;
	}

}
