package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;
import org.terpo.waterworks.gui.ContainerBase;
import org.terpo.waterworks.gui.pump.PumpContainer;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.registries.ObjectHolder;

public class WaterworksContainers {
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.CONTAINER_WATERWORKS)
	public static ContainerType<ContainerBase> waterworks;
	@ObjectHolder(WaterworksReference.DOMAIN + WaterworksRegistryNames.CONTAINER_GROUNDWATER_PUMP)
	public static ContainerType<PumpContainer> groundwaterPump;

	private WaterworksContainers() {
		// hidme
	}
}
