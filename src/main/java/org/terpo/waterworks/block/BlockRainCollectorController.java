package org.terpo.waterworks.block;

import java.util.List;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.gui.GuiProxy;
import org.terpo.waterworks.init.WaterworksItems;
import org.terpo.waterworks.inventory.WaterworksInventoryHelper;
import org.terpo.waterworks.tileentity.TileEntityRainCollectorController;
import org.terpo.waterworks.tileentity.TileWaterworks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockRainCollectorController extends BaseBlockTE<TileWaterworks> {

	public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 4);
	public BlockRainCollectorController() {
		super(Material.IRON);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, player, tooltip, advanced);
		tooltip.add(I18n.format("tooltip.rain_collector_controller"));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityRainCollectorController();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote && hand == EnumHand.MAIN_HAND) {// isRemote true = client
			final TileEntity tileEntity = getTE(worldIn, pos);
			if (tileEntity instanceof TileEntityRainCollectorController) {
				final ItemStack heldItem = playerIn.getHeldItem(hand);
				if (heldItem.getItem() == WaterworksItems.pipe_wrench) {
					final int collectors = ((TileEntityRainCollectorController) tileEntity).findRainCollectors();
					final String out = collectors - 1 + " " + "Collectors found";
					playerIn.sendMessage(new TextComponentString(out));
					return true;
				}
				if (!playerIn.isSneaking()) {
					if (!heldItem.isEmpty()) {
						if (tileEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
							final IFluidHandler tileEntityFluidHandler = tileEntity
									.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
							final FluidActionResult fluidActionResult = FluidUtil.interactWithFluidHandler(heldItem,
									tileEntityFluidHandler, playerIn);
							if (fluidActionResult.isSuccess()) {
								playerIn.setHeldItem(hand, fluidActionResult.getResult());
								return true;
							}
							// Try Glass Bottle handling
							if (heldItem.getItem().equals(Items.GLASS_BOTTLE)) {
								fillWaterBottle(worldIn, pos, playerIn, heldItem, hand,
										(TileEntityRainCollectorController) tileEntity);
							}
							return true;
						}
					}
					playerIn.openGui(Waterworks.instance, GuiProxy.WATERWORKS_RAINTANK_GUI, worldIn, pos.getX(),
							pos.getY(), pos.getZ());
					return true;
				}
			}
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		final TileEntity tileEntity = getTE(world, pos);
		if (tileEntity instanceof TileWaterworks) {
			final IItemHandler handler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			WaterworksInventoryHelper.dropItemsFromInventory(world, pos, handler);
		}
		if (tileEntity instanceof TileEntityRainCollectorController) {
			((TileEntityRainCollectorController) tileEntity).resetController();
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean hasTileEntity() {
		return true;
	}

}
