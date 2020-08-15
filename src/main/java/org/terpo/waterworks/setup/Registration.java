package org.terpo.waterworks.setup;

import static org.terpo.waterworks.api.constants.Reference.MODID;

import java.util.Set;

import org.terpo.waterworks.Waterworks;
import org.terpo.waterworks.api.constants.RegistryNames;
import org.terpo.waterworks.block.BlockGroundwaterPump;
import org.terpo.waterworks.block.BlockRainCollector;
import org.terpo.waterworks.block.BlockRainCollectorController;
import org.terpo.waterworks.block.BlockRainTankWood;
import org.terpo.waterworks.block.BlockWaterPipe;
import org.terpo.waterworks.entity.item.EntityFireworkRocketAntiRain;
import org.terpo.waterworks.entity.item.EntityFireworkRocketRain;
import org.terpo.waterworks.gui.ContainerBase;
import org.terpo.waterworks.gui.pump.PumpContainer;
import org.terpo.waterworks.item.ItemFireworkAntiRain;
import org.terpo.waterworks.item.ItemFireworkRain;
import org.terpo.waterworks.item.ItemMaterialController;
import org.terpo.waterworks.item.ItemMaterialEnergyAdapter;
import org.terpo.waterworks.item.ItemPipeWrench;
import org.terpo.waterworks.item.ItemWaterworksDebugger;
import org.terpo.waterworks.item.crafting.AntiRainRocketRecipe;
import org.terpo.waterworks.item.crafting.RainRocketRecipe;
import org.terpo.waterworks.tileentity.TileEntityGroundwaterPump;
import org.terpo.waterworks.tileentity.TileEntityRainCollector;
import org.terpo.waterworks.tileentity.TileEntityRainCollectorController;
import org.terpo.waterworks.tileentity.TileEntityRainTankWood;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registration {

	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);
	private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);
	private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
	private static final DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);

	public static void init() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		BLOCKS.register(modEventBus);
		ITEMS.register(modEventBus);
		TILES.register(modEventBus);
		CONTAINERS.register(modEventBus);
		ENTITIES.register(modEventBus);
		RECIPES.register(modEventBus);
	}

	/*
	 * BLOCKS
	 */
	public static final RegistryObject<BlockRainTankWood> rainTankWoodBlock = BLOCKS.register(RegistryNames.RAIN_TANK_WOOD,
			BlockRainTankWood::new);

	public static final RegistryObject<BlockWaterPipe> waterPipeBlock = BLOCKS.register(RegistryNames.WATER_PIPE, BlockWaterPipe::new);

	public static final RegistryObject<BlockRainCollector> rainCollectorBlock = BLOCKS.register(RegistryNames.RAIN_COLLECTOR,
			BlockRainCollector::new);

	public static final RegistryObject<BlockRainCollectorController> rainCollectorControllerBlock = BLOCKS
			.register(RegistryNames.RAIN_COLLECTOR_CONTROLLER, BlockRainCollectorController::new);

	public static final RegistryObject<BlockGroundwaterPump> groundwaterPumpBlock = BLOCKS.register(RegistryNames.GROUNDWATER_PUMP,
			BlockGroundwaterPump::new);

	/*
	 * BLOCK ITEMS
	 */
	public static final RegistryObject<Item> rainTankWoodBlockItem = ITEMS.register(RegistryNames.RAIN_TANK_WOOD,
			() -> new BlockItem(rainTankWoodBlock.get(), new Item.Properties().group(CommonSetup.CREATIVE_TAB)));

	public static final RegistryObject<Item> waterPipeBlockItem = ITEMS.register(RegistryNames.WATER_PIPE,
			() -> new BlockItem(waterPipeBlock.get(), new Item.Properties().group(CommonSetup.CREATIVE_TAB)));

	public static final RegistryObject<Item> rainCollectorBlockItem = ITEMS.register(RegistryNames.RAIN_COLLECTOR,
			() -> new BlockItem(rainCollectorBlock.get(), new Item.Properties().group(CommonSetup.CREATIVE_TAB)));

	public static final RegistryObject<Item> rainCollectorControllerBlockItem = ITEMS.register(RegistryNames.RAIN_COLLECTOR_CONTROLLER,
			() -> new BlockItem(rainCollectorControllerBlock.get(), new Item.Properties().group(CommonSetup.CREATIVE_TAB)));

	public static final RegistryObject<Item> groundwaterPumpBlockItem = ITEMS.register(RegistryNames.GROUNDWATER_PUMP,
			() -> new BlockItem(groundwaterPumpBlock.get(), new Item.Properties().group(CommonSetup.CREATIVE_TAB)));

	/*
	 * TILES
	 */

	public static final RegistryObject<TileEntityType<TileEntityRainTankWood>> rainTankWoodTile = TILES.register(
			RegistryNames.RAIN_TANK_WOOD,
			() -> TileEntityType.Builder.create(TileEntityRainTankWood::new, rainTankWoodBlock.get()).build(null));

	public static final RegistryObject<TileEntityType<TileEntityRainCollector>> rainCollectorTile = TILES.register(
			RegistryNames.RAIN_COLLECTOR,
			() -> TileEntityType.Builder.create(TileEntityRainCollector::new, rainCollectorBlock.get()).build(null));

	public static final RegistryObject<TileEntityType<TileEntityRainCollectorController>> rainCollectorControllerTile = TILES.register(
			RegistryNames.RAIN_COLLECTOR_CONTROLLER,
			() -> TileEntityType.Builder.create(TileEntityRainCollectorController::new, rainCollectorControllerBlock.get()).build(null));

	public static final RegistryObject<TileEntityType<TileEntityGroundwaterPump>> groundwaterPumpTile = TILES.register(
			RegistryNames.GROUNDWATER_PUMP,
			() -> TileEntityType.Builder.create(TileEntityGroundwaterPump::new, groundwaterPumpBlock.get()).build(null));

	/*
	 * ITEMS
	 */
	public static final RegistryObject<ItemWaterworksDebugger> waterworksDebuggerItem = ITEMS.register(RegistryNames.WATERWORKS_DEBUGGER,
			ItemWaterworksDebugger::new);

	public static final RegistryObject<ItemPipeWrench> pipeWrenchItem = ITEMS.register(RegistryNames.PIPE_WRENCH, ItemPipeWrench::new);

	public static final RegistryObject<ItemFireworkRain> fireworkRainItem = ITEMS.register(RegistryNames.FIREWORK_RAIN,
			ItemFireworkRain::new);

	public static final RegistryObject<ItemFireworkAntiRain> fireworkAntiRainItem = ITEMS.register(RegistryNames.FIREWORK_ANTI_RAIN,
			ItemFireworkAntiRain::new);

	public static final RegistryObject<ItemMaterialEnergyAdapter> materialEnergyAdapterItem = ITEMS
			.register(RegistryNames.MATERIAL_ENERGY_ADAPTER, ItemMaterialEnergyAdapter::new);

	public static final RegistryObject<ItemMaterialController> materialControllerItem = ITEMS.register(RegistryNames.MATERIAL_CONTROLLER,
			ItemMaterialController::new);

	/*
	 * ENTITIES
	 */

	public static final RegistryObject<EntityType<EntityFireworkRocketRain>> fireworkRainEntity = ENTITIES
			.register(RegistryNames.FIREWORK_RAIN,
					() -> EntityType.Builder.<EntityFireworkRocketRain>create(EntityFireworkRocketRain::new, EntityClassification.MISC)
							.setTrackingRange(64).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true)
							.build(RegistryNames.FIREWORK_RAIN));

	public static final RegistryObject<EntityType<EntityFireworkRocketAntiRain>> fireworkAntiRainEntity = ENTITIES.register(
			RegistryNames.FIREWORK_ANTI_RAIN,
			() -> EntityType.Builder.<EntityFireworkRocketAntiRain>create(EntityFireworkRocketAntiRain::new, EntityClassification.MISC)
					.setTrackingRange(64).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true).build(RegistryNames.FIREWORK_RAIN));

	/*
	 * CONTAINERS
	 */
	public static final RegistryObject<ContainerType<ContainerBase>> rainTankWoodContainer = CONTAINERS.register(
			RegistryNames.RAIN_TANK_WOOD,
			() -> IForgeContainerType.create((windowId, inventory, data) -> new ContainerBase(Registration.rainTankWoodContainer.get(),
					windowId, inventory, Waterworks.proxy.getClientWorld().getTileEntity(data.readBlockPos()))));

	public static final RegistryObject<ContainerType<ContainerBase>> rainCollectorControllerContainer = CONTAINERS.register(
			RegistryNames.RAIN_COLLECTOR_CONTROLLER,
			() -> IForgeContainerType
					.create((windowId, inventory, data) -> new ContainerBase(Registration.rainCollectorControllerContainer.get(), windowId,
							inventory, Waterworks.proxy.getClientWorld().getTileEntity(data.readBlockPos()))));

	public static final RegistryObject<ContainerType<PumpContainer>> groundwaterPumpContainer = CONTAINERS
			.register(RegistryNames.GROUNDWATER_PUMP, () -> IForgeContainerType.create((windowId, inventory,
					data) -> new PumpContainer(windowId, inventory, Waterworks.proxy.getClientWorld().getTileEntity(data.readBlockPos()))));

	/*
	 * RECIPES
	 */

	public static final RegistryObject<IRecipeSerializer<RainRocketRecipe>> fireworkRainRecipe = RECIPES
			.register(RegistryNames.FIREWORK_RAIN, () -> new SpecialRecipeSerializer<>(RainRocketRecipe::new));
	public static final RegistryObject<IRecipeSerializer<AntiRainRocketRecipe>> fireworkAntiRainRecipe = RECIPES
			.register(RegistryNames.FIREWORK_ANTI_RAIN, () -> new SpecialRecipeSerializer<>(AntiRainRocketRecipe::new));

	private Registration() {
		// hide me
	}

	public static Set<Block> getAllWaterworksBlocks() {
		return Sets.newHashSet(Registration.rainTankWoodBlock.get(), Registration.waterPipeBlock.get(),
				Registration.rainCollectorBlock.get(), Registration.rainCollectorControllerBlock.get(),
				Registration.groundwaterPumpBlock.get());
	}
}
