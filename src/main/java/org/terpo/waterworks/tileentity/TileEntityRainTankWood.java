package org.terpo.waterworks.tileentity;

import org.terpo.waterworks.fluid.WaterworksTank;
import org.terpo.waterworks.gui.ContainerBase;
import org.terpo.waterworks.helper.GeneralItemStackHandler;
import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.init.WaterworksContainers;
import org.terpo.waterworks.init.WaterworksTileEntities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class TileEntityRainTankWood extends TileWaterworks {

	protected FluidStack fluidResource = null;
	private static final int INVENTORY_SLOT_COUNT = 2;

	public TileEntityRainTankWood() {
		this(WaterworksTileEntities.rainTankWood, WaterworksConfig.rainCollection.getWoodenRainTankFillrate(),
				WaterworksConfig.rainCollection.getWoodenRainTankCapacity());
	}

	public TileEntityRainTankWood(TileEntityType<?> tileEntityTypeIn) {
		this(tileEntityTypeIn, WaterworksConfig.rainCollection.getWoodenRainTankFillrate(),
				WaterworksConfig.rainCollection.getWoodenRainTankCapacity());
	}
	public TileEntityRainTankWood(TileEntityType<?> tileEntityTypeIn, int fillrate, int capacity) {
		super(tileEntityTypeIn, INVENTORY_SLOT_COUNT, capacity);
		this.fluidResource = new FluidStack(Fluids.WATER, fillrate);
	}

	@Override
	protected GeneralItemStackHandler createItemHandler() {
		final GeneralItemStackHandler handler = new GeneralItemStackHandler(this.inventorySize, this);
		handler.setInputFlagForIndex(0, true);
		handler.setOutputFlagForIndex(1, true);
		return handler;
	}
	@Override
	protected void updateServerSide() {

		if (fillFluid()) {
			this.isDirty = true;
		}

		if (needsUpdate(20) && isRefilling()) {
			this.isDirty = true;
		}
		super.updateServerSide();
	}

	public int getStateLevel() {
		final WaterworksTank tank = getFluidTank();
		return Math.round((tank.getFluidAmount() * 4.0f / tank.getCapacity()));
	}

	protected boolean isRefilling() {
		final BlockPos position = getPos().up();

		if (this.world.isRainingAt(position)) {
			getFluidTank().fillInternal(this.fluidResource, FluidAction.EXECUTE);
			return true;
		}
		return false;
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity entity) {
		return new ContainerBase(WaterworksContainers.rainTankWood, windowId, inventory, this);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("block.waterworks.rain_tank_wood");
	}
}
