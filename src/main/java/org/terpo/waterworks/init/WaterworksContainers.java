package org.terpo.waterworks.init;

import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;
import org.terpo.waterworks.gui.ContainerBase;
import org.terpo.waterworks.gui.pump.PumpContainer;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(WaterworksReference.MODID)
public class WaterworksContainers {
	@ObjectHolder(WaterworksRegistryNames.BLOCK_RAIN_TANK_WOOD)
	public static final ContainerType<ContainerBase> rainTankWood = null;
	@ObjectHolder(WaterworksRegistryNames.BLOCK_RAIN_COLLECTOR_CONTROLLER)
	public static final ContainerType<ContainerBase> rainCollectorController = null;
	@ObjectHolder(WaterworksRegistryNames.BLOCK_GROUNDWATER_PUMP)
	public static final ContainerType<PumpContainer> groundwaterPump = null;

	private WaterworksContainers() {
		// hide me
	}
}
