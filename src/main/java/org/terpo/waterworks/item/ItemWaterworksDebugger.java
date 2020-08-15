package org.terpo.waterworks.item;

import java.util.List;

import org.terpo.waterworks.setup.CommonSetup;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public class ItemWaterworksDebugger extends Item {

	private static final String DRAIN_MODE = "drainMode";
	private static final String ENERGY_MODE = "energyMode";

	public ItemWaterworksDebugger() {
		super((new Item.Properties()).maxStackSize(1).group(CommonSetup.CREATIVE_TAB));
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		final CompoundNBT tag = stack.getTag();

		tooltip.add(new StringTextComponent("Right Click in Air to change between Fluid/Energy Mode"));
		tooltip.add(new StringTextComponent("Sneak Right Click in Air to change between Drain/Fill Mode"));

		tooltip.add(currentMode(tag));
	}

	private static String getEnergyMode(final CompoundNBT tag) {
		return (tag != null && tag.contains(ItemWaterworksDebugger.ENERGY_MODE) && tag.getBoolean(ItemWaterworksDebugger.ENERGY_MODE))
				? "energy"
				: "fluid";
	}

	private static String getDrainMode(final CompoundNBT tag) {
		return (tag != null && tag.contains(ItemWaterworksDebugger.DRAIN_MODE) && tag.getBoolean(ItemWaterworksDebugger.DRAIN_MODE))
				? "drain"
				: "fill";
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if (!context.getWorld().isRemote) {
			final TileEntity tileEntity = context.getWorld().getTileEntity(context.getPos());
			final ItemStack stack = context.getItem();
			if (tileEntity != null && context.getPlayer().isSneaking() && !stack.isEmpty()) {
				final CompoundNBT tag = stack.getTag();
				final boolean energyMode = (tag != null && tag.contains(ItemWaterworksDebugger.ENERGY_MODE)
						&& tag.getBoolean(ItemWaterworksDebugger.ENERGY_MODE));
				final boolean drainMode = (tag != null && tag.contains(ItemWaterworksDebugger.DRAIN_MODE)
						&& tag.getBoolean(ItemWaterworksDebugger.DRAIN_MODE));

				if (energyMode) {
					handleEnergy(tileEntity, drainMode);
				} else {
					handleFluid(tileEntity, drainMode);
				}
			}
		}
		return ActionResultType.SUCCESS;
	}

	private static void handleFluid(final TileEntity tileEntity, final boolean drainMode) {
		final LazyOptional<IFluidHandler> capability = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
		capability.ifPresent(b -> {
			if (drainMode) {
				b.drain(1000, FluidAction.EXECUTE);
			} else {
				b.fill(new FluidStack(Fluids.WATER, 1000), FluidAction.EXECUTE);
			}
		});
	}

	private static void handleEnergy(final TileEntity tileEntity, final boolean drainMode) {
		final LazyOptional<IEnergyStorage> capability = tileEntity.getCapability(CapabilityEnergy.ENERGY);
		capability.ifPresent(b -> {
			if (drainMode) {
				b.extractEnergy(50000, false);
			} else {
				b.receiveEnergy(50000, false);
			}
		});
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if (!worldIn.isRemote) {
			final ItemStack stack = playerIn.getHeldItem(Hand.MAIN_HAND);
			if (!stack.isEmpty()) {
				final CompoundNBT tag = stack.getOrCreateTag();

				if (playerIn.isSneaking()) {
					if (tag.contains(ItemWaterworksDebugger.DRAIN_MODE)) {
						tag.putBoolean(ItemWaterworksDebugger.DRAIN_MODE, !tag.getBoolean(ItemWaterworksDebugger.DRAIN_MODE));
					} else {
						tag.putBoolean(ItemWaterworksDebugger.DRAIN_MODE, true);
					}
				} else if (tag.contains(ItemWaterworksDebugger.ENERGY_MODE)) {
					tag.putBoolean(ItemWaterworksDebugger.ENERGY_MODE, !tag.getBoolean(ItemWaterworksDebugger.ENERGY_MODE));
				} else {
					tag.putBoolean(ItemWaterworksDebugger.ENERGY_MODE, true);
				}
				playerIn.sendMessage(currentMode(tag),Util.NIL_UUID);
			}
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	private static StringTextComponent currentMode(final CompoundNBT tag) {
		return new StringTextComponent("Mode: " + getDrainMode(tag) + " " + getEnergyMode(tag));
	}
}
