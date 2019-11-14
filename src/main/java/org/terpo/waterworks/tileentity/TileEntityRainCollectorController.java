package org.terpo.waterworks.tileentity;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.gui.ContainerBase;
import org.terpo.waterworks.helper.AreaHelper;
import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.init.WaterworksContainers;
import org.terpo.waterworks.init.WaterworksTileEntities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class TileEntityRainCollectorController extends TileEntityRainTankWood {
	private static final String NBT_VALID_COLLECTORS = "validCollectors";
	private static final String NBT_COLLECTOR_POS_LIST = "collectorPosList";
	private static final String NBT_CONNECTED_BLOCKS = "connectedBlocks";

	private final int controllerRange;
	private final int areaCount;
	private BlockPos[] rainCollectorBlocks;
	protected int connectedCollectors;

	private boolean isReset = false;

	protected int validCollectors;
	private int currentValidationPos;
	private int countValidCollectors;

	public TileEntityRainCollectorController() {
		super(WaterworksTileEntities.rainCollectorController, 0,
				WaterworksConfig.rainCollection.getRainCollectorCapacity());

		this.controllerRange = WaterworksConfig.rainCollection.getRainCollectorRange();
		this.areaCount = (int) Math.pow(this.controllerRange * 2.0d + 1, 2);
		this.rainCollectorBlocks = new BlockPos[this.areaCount];
		this.connectedCollectors = 1;
		this.isReset = false;
		this.validCollectors = 0;
		this.currentValidationPos = 0;
		this.countValidCollectors = 0;
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity entity) {
		return new ContainerBase(WaterworksContainers.rainCollectorController, windowId, inventory, this);
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
			getFluidTank().fillInternal(this.fluidResource, FluidAction.EXECUTE);
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
			this.fluidResource = getWaterFluidStack(
					this.validCollectors * WaterworksConfig.rainCollection.getRainCollectorFillrate());
			this.countValidCollectors = 0;
			this.currentValidationPos = 0;
		}
	}

	public int findRainCollectors() {
		resetController();
		this.connectedCollectors = getAllConnectedBlocks();
		this.fluidResource = getWaterFluidStack(
				this.connectedCollectors * WaterworksConfig.rainCollection.getRainCollectorFillrate());
		return this.connectedCollectors;
	}

	public void debugCollectors() { // for debug only
		Waterworks.LOGGER.info("Fill Multiplier @" + this.connectedCollectors); // NOSONAR
		for (final BlockPos blockPos : this.rainCollectorBlocks) {
			if (blockPos != null) {
				Waterworks.LOGGER.info("Collector @" + blockPos.toString()); // NOSONAR
			}
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		int connectedBlocks = 0;
		// TagList of Integer Tags:

		final ListNBT list = new ListNBT();
		for (int i = 0; i < this.areaCount; i++) {
			if (this.rainCollectorBlocks[i] != null) {
				final CompoundNBT nbt = new CompoundNBT();
				nbt.putLong("collectorPos", this.rainCollectorBlocks[i].toLong());
				list.add(nbt);
				connectedBlocks++;
			}
		}
		compound.putInt(NBT_CONNECTED_BLOCKS, connectedBlocks);
		compound.put(NBT_COLLECTOR_POS_LIST, list);
		compound.putInt(NBT_VALID_COLLECTORS, this.validCollectors);
		return compound;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		if (compound.contains(NBT_CONNECTED_BLOCKS)) {
			this.connectedCollectors = compound.getInt(NBT_CONNECTED_BLOCKS);
			if (compound.contains(NBT_COLLECTOR_POS_LIST)) {
				final ListNBT list = compound.getList(NBT_COLLECTOR_POS_LIST, 10);
				if (list.size() > this.areaCount) {
					this.rainCollectorBlocks = new BlockPos[list.size()];
					this.isReset = true;
				}
				for (int i = 0; i < list.size(); i++) {
					final CompoundNBT nbt = list.getCompound(i);
					this.rainCollectorBlocks[i] = (BlockPos.fromLong(nbt.getLong("collectorPos")));
				}
			}
			if (compound.contains(NBT_VALID_COLLECTORS)) {
				this.validCollectors = compound.getInt(NBT_VALID_COLLECTORS);
				this.fluidResource = getWaterFluidStack(
						this.validCollectors * WaterworksConfig.rainCollection.getRainCollectorFillrate());
			}
		}
	}

	private boolean isValidBlock(BlockPos blockPos, int currentIndex) {
		final TileEntity tileEntity = this.world.getTileEntity(blockPos);
		// not correct class, not in range or already has a controller?
		if (!(tileEntity instanceof TileEntityRainCollector)
				|| !(AreaHelper.isInRange2D(blockPos, this.pos, this.controllerRange))
				|| ((TileEntityRainCollector) tileEntity).hasController()) {
			return false;
		}

		// it is valid
		for (int i = 0; i < currentIndex; i++) {
			if (this.rainCollectorBlocks[i].equals(blockPos)) {
				return false;
			}
		}

		return true;

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

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("block.waterworks.rain_collector");
	}
}
