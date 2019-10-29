package org.terpo.waterworks.block;

import org.terpo.waterworks.api.constants.WaterworksConstants;
import org.terpo.waterworks.compat.top.TOPCompatibility;
import org.terpo.waterworks.compat.top.provider.TOPInfoProvider;
import org.terpo.waterworks.tileentity.BaseTileEntity;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public abstract class BaseBlockTE<T extends BaseTileEntity> extends Block implements TOPInfoProvider {

	protected final ResourceLocation guiIconsTOP = new ResourceLocation(TOPCompatibility.TOP_MOD_ID,
			"textures/gui/icons.png");

	public BaseBlockTE(Block.Properties builder) {
		super(builder);
	}
	public BaseBlockTE() {
		this(createBaseBlockProperties());
	}

	protected abstract T getTileEntity(World world, BlockPos pos);

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world,
			BlockState blockState, IProbeHitData data) {
		// override me
	}

	public static Block.Properties createBaseBlockProperties() {
		return Block.Properties.create(Material.IRON).hardnessAndResistance(2F, 6.0F).sound(SoundType.METAL);
	}

	@Override
	public boolean isToolEffective(BlockState state, ToolType tool) {
		if (WaterworksConstants.WATERWORKS_TOOL_TYPE.equals(tool.getName())) {
			return true;
		}
		return super.isToolEffective(state, tool);
	}
}
