package org.terpo.waterworks.compat.top;

import java.util.function.Function;

import javax.annotation.Nullable;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.api.constants.Reference;
import org.terpo.waterworks.compat.top.provider.TOPInfoProvider;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class TOPRegistration implements Function<ITheOneProbe, Void> {

	@Nullable
	@Override
	public Void apply(ITheOneProbe theOneProbe) {
		TOPCompatibility.probe = theOneProbe;
		Waterworks.LOGGER.info("Enabled support for The One Probe");
		TOPCompatibility.probe.registerProvider(new IProbeInfoProvider() {
			@Override
			public String getID() {
				return Reference.DOMAIN + "top";
			}

			@Override
			public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world,
					BlockState blockState, IProbeHitData data) {
				if (blockState.getBlock() instanceof TOPInfoProvider) {
					final TOPInfoProvider provider = (TOPInfoProvider) blockState.getBlock();
					provider.addProbeInfo(mode, probeInfo, player, world, blockState, data);
				}

			}
		});
		return null;
	}
}