package org.terpo.waterworks.compat.top;

public class TOPCompatibility {
	// TODO restore TOP Compatibility
//	public static String modId = "theoneprobe";
//	private static boolean registered;
//
//	public static void register() {
//		if (registered) {
//			return;
//		}
//		registered = true;
//		FMLInterModComms.sendFunctionMessage(TOPCompatibility.modId, "getTheOneProbe",
//				"org.terpo.waterworks.compat.top.TOPCompatibility$GetTheOneProbe");
//	}
//
//	public static class GetTheOneProbe implements com.google.common.base.Function<ITheOneProbe, Void> {
//
//		public static ITheOneProbe probe;
//
//		@Nullable
//		@Override
//		public Void apply(ITheOneProbe theOneProbe) {
//			probe = theOneProbe;
//			Waterworks.LOGGER.info("Enabled support for The One Probe");
//			probe.registerProvider(new IProbeInfoProvider() {
//				@Override
//				public String getID() {
//					return "modtut:default";
//				}
//
//				@Override
//				public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world,
//						BlockState blockState, IProbeHitData data) {
//					if (blockState.getBlock() instanceof TOPInfoProvider) {
//						final TOPInfoProvider provider = (TOPInfoProvider) blockState.getBlock();
//						provider.addProbeInfo(mode, probeInfo, player, world, blockState, data);
//					}
//
//				}
//			});
//			return null;
//		}
//	}
}
