package org.terpo.waterworks.setup;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.api.constants.Reference;
import org.terpo.waterworks.network.WaterworksPacketHandler;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonSetup {

	public static final ItemGroup CREATIVE_TAB = new ItemGroup(Reference.MODID) {

		@Override
		public ItemStack createIcon() {
			return new ItemStack(Registration.groundwaterPumpBlock.get());
		}
	};

	public static void init(final FMLCommonSetupEvent event) { // NOSONAR
		Waterworks.LOGGER.info("Waterworks Setup starting");
		WaterworksPacketHandler.registerMessages();
		Waterworks.LOGGER.info("Waterworks Setup complete");
	}

	private CommonSetup() {
		// hidden
	}
}
