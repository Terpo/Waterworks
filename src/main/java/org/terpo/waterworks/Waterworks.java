package org.terpo.waterworks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.init.InitBlocks;
import org.terpo.waterworks.init.InitEntities;
import org.terpo.waterworks.init.InitItems;
import org.terpo.waterworks.init.InitModCompat;
import org.terpo.waterworks.init.InitTileEntities;
import org.terpo.waterworks.init.WaterworksCrafting;
import org.terpo.waterworks.network.WaterworksPacketHandler;
import org.terpo.waterworks.proxy.ClientProxy;
import org.terpo.waterworks.proxy.IProxy;
import org.terpo.waterworks.proxy.ServerProxy;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WaterworksReference.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Waterworks {
	public static final Logger LOGGER = LogManager.getLogger(WaterworksReference.NAME);
	// do not use a lambda method reference here
	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

	public static final ItemGroup CREATIVE_TAB = new WaterworksTab();

	static {
		FluidRegistry.enableUniversalBucket(); // Must be called before preInit
	}
	public Waterworks() {
		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		// Register the enqueueIMC method for modloading
//		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		// Register the processIMC method for modloading
//		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		// Register the doClientStuff method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

		// Register ourselves for server and other game events we are interested in
//		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup(final FMLCommonSetupEvent event) {
		LOGGER.info("Waterworks Setup starting");

		InitModCompat.init("pre");
		WaterworksPacketHandler.registerMessages();

		InitModCompat.init("init");
		WaterworksCrafting.register();
//		NetworkRegistry.INSTANCE.registerGuiHandler(Waterworks.instance, new GuiProxy()); //FIXME network registry
		proxy.setup(event);

		LOGGER.info("Waterworks Setup complete");
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		LOGGER.info("Waterworks Client Setup");
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		// some example code to dispatch IMC to another mod
//		InterModComms.sendTo("modid", "method", () -> "IMC");
	}

	private void processIMC(final InterModProcessEvent event) {
		// some example code to receive and process InterModComms from other mods
//		LOGGER.info("Got IMC {}",
//				event.getIMCStream().map(m -> m.getMessageSupplier().get()).collect(Collectors.toList()));
	}
	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {
		// do something when the server starts
		LOGGER.info("HELLO from server starting");
	}

	// You can use EventBusSubscriber to automatically subscribe events on the contained
	// class (this is subscribing to the MOD
	// Event bus for receiving Registry Events)
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
			InitBlocks.initBlocks(event);
		}
		@SubscribeEvent
		public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
			InitBlocks.initItemBlocks(event);
			InitItems.init(event.getRegistry());
		}

		@SubscribeEvent
		public static void onTileEntityRegistry(RegistryEvent.Register<TileEntityType<?>> event) {
			InitTileEntities.register(event.getRegistry());
		}

		@SubscribeEvent
		public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> event) {
			InitEntities.register(event.getRegistry());
		}
	}
}
