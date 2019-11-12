package org.terpo.waterworks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.terpo.waterworks.api.constants.WaterworksReference;
import org.terpo.waterworks.compat.minecraft.MinecraftCompatibility;
import org.terpo.waterworks.compat.top.TOPCompatibility;
import org.terpo.waterworks.init.InitBlocks;
import org.terpo.waterworks.init.InitContainers;
import org.terpo.waterworks.init.InitEntities;
import org.terpo.waterworks.init.InitItems;
import org.terpo.waterworks.init.InitRecipes;
import org.terpo.waterworks.init.InitTileEntities;
import org.terpo.waterworks.init.WaterworksBlocks;
import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.network.WaterworksPacketHandler;
import org.terpo.waterworks.proxy.ClientProxy;
import org.terpo.waterworks.proxy.IProxy;
import org.terpo.waterworks.proxy.ServerProxy;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(WaterworksReference.MODID)
public class Waterworks {
	public static final Logger LOGGER = LogManager.getLogger(WaterworksReference.NAME);
	// do not use a lambda method reference here
	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy()); // NOSONAR

	public static final ItemGroup CREATIVE_TAB = new ItemGroup(WaterworksReference.MODID) {

		@Override
		public ItemStack createIcon() {
			return new ItemStack(WaterworksBlocks.groundwaterPump);
		}
	};

	// FIXME universal bucket
//	static {
//		FluidRegistry.enableUniversalBucket(); // Must be called before preInit
//	}
	public Waterworks() {

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WaterworksConfig.commonConfig);

		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		// Register the enqueueIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		// Register the doClientStuff method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

		WaterworksConfig.loadConfig(WaterworksConfig.commonConfig,
				FMLPaths.CONFIGDIR.get().resolve("waterworks-common.toml"));
	}

	private void setup(final FMLCommonSetupEvent event) {
		LOGGER.info("Waterworks Setup starting");
		WaterworksPacketHandler.registerMessages();
		proxy.setup(event);
		proxy.init();
		LOGGER.info("Waterworks Setup complete");
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		LOGGER.info("Waterworks Client Setup");
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		LOGGER.info("Waterworks IMC to other mods");
		TOPCompatibility.register();

		MinecraftCompatibility.registerWeatherRocketDispenserBehavior();
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
			InitBlocks.initBlocks(event);
		}
		@SubscribeEvent
		public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
			final IForgeRegistry<Item> itemRegistry = event.getRegistry();
			InitBlocks.initItemBlocks(itemRegistry);
			InitItems.init(itemRegistry);
		}

		@SubscribeEvent
		public static void onTileEntityRegistry(RegistryEvent.Register<TileEntityType<?>> event) {
			InitTileEntities.register(event.getRegistry());
		}

		@SubscribeEvent
		public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> event) {
			InitEntities.register(event.getRegistry());
		}

		@SubscribeEvent
		public static void onRecipeRegistry(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
			InitRecipes.register(event.getRegistry());
		}

		@SubscribeEvent
		public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
			// registry of the client side container
			InitContainers.register(event.getRegistry());
		}
		private RegistryEvents() {
			// hidden
		}
	}
}
