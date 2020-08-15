package org.terpo.waterworks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.terpo.waterworks.api.constants.Reference;
import org.terpo.waterworks.setup.ClientProxy;
import org.terpo.waterworks.setup.ClientSetup;
import org.terpo.waterworks.setup.CommonSetup;
import org.terpo.waterworks.setup.IProxy;
import org.terpo.waterworks.setup.InterModEnqueueSetup;
import org.terpo.waterworks.setup.Registration;
import org.terpo.waterworks.setup.ServerProxy;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MODID)
public class Waterworks {
	public static final Logger LOGGER = LogManager.getLogger(Reference.NAME);
	// do not use a lambda method reference here
	public static IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new); // NOSONAR

	public Waterworks() { // NOSONAR

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.commonConfig);

		Registration.init();

		FMLJavaModLoadingContext.get().getModEventBus().addListener(CommonSetup::init);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(InterModEnqueueSetup::init);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
	}
}
