package org.terpo.waterworks.init;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.api.constants.WaterworksRegistryNames;
import org.terpo.waterworks.gui.ContainerBase;
import org.terpo.waterworks.gui.pump.PumpContainer;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.registries.IForgeRegistry;

public class InitContainers {
	public static void register(IForgeRegistry<ContainerType<?>> registry) {
		registry.register(IForgeContainerType
				.create((windowId, inventory, data) -> new ContainerBase(WaterworksContainers.rainTankWood, windowId,
						inventory, Waterworks.proxy.getClientWorld().getTileEntity(data.readBlockPos())))
				.setRegistryName(WaterworksReference.MODID, WaterworksRegistryNames.BLOCK_RAIN_TANK_WOOD));
		registry.register(IForgeContainerType
				.create((windowId, inventory, data) -> new ContainerBase(WaterworksContainers.rainCollectorController,
						windowId, inventory, Waterworks.proxy.getClientWorld().getTileEntity(data.readBlockPos())))
				.setRegistryName(WaterworksReference.MODID, WaterworksRegistryNames.BLOCK_RAIN_COLLECTOR_CONTROLLER));
		registry.register(IForgeContainerType
				.create((windowId, inventory, data) -> new PumpContainer(windowId, inventory,
						Waterworks.proxy.getClientWorld().getTileEntity(data.readBlockPos())))
				.setRegistryName(WaterworksReference.MODID, WaterworksRegistryNames.BLOCK_GROUNDWATER_PUMP));
	}
	private InitContainers() {
		// hidden
	}
}
