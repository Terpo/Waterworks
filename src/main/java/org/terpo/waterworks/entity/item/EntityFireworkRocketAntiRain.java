package org.terpo.waterworks.entity.item;

import java.util.OptionalInt;

import org.terpo.waterworks.init.WaterworksConfig;
import org.terpo.waterworks.init.WaterworksEntities;
import org.terpo.waterworks.init.WaterworksItems;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityFireworkRocketAntiRain extends EntityWeatherFireworkRocket {

	private static final DataParameter<ItemStack> ANTI_RAINROCKET_ITEM = EntityDataManager
			.createKey(EntityFireworkRocketAntiRain.class, DataSerializers.ITEMSTACK);
	private static final DataParameter<OptionalInt> BOOSTED_ANTI_RAINROCKET_ENTITY_ID = EntityDataManager
			.createKey(EntityFireworkRocketAntiRain.class, DataSerializers.OPTIONAL_VARINT);
	private static final DataParameter<Boolean> SHOT_AT_ANGLE_ANTI_RAINROCKET_BOOLEAN = EntityDataManager
			.createKey(EntityFireworkRocketAntiRain.class, DataSerializers.BOOLEAN);

	private int realClearSky = WaterworksConfig.rockets.getClearSkyDuration();

	public EntityFireworkRocketAntiRain(EntityType<? extends EntityFireworkRocketAntiRain> entity, World world) {
		super(entity, world);
		this.duration = WaterworksConfig.rockets.getClearSkyDuration();
	}

	public EntityFireworkRocketAntiRain(World worldIn, double x, double y, double z, ItemStack itemstack) {
		super(WaterworksEntities.itemFireworkAntiRain, worldIn, x, y, z, itemstack);
	}

	public EntityFireworkRocketAntiRain(World worldIn, ItemStack itemstack, LivingEntity entity) {
		super(WaterworksEntities.itemFireworkAntiRain, worldIn, itemstack, entity);
	}

	@Override
	public String getAnnouncementText(int time, final int days, final int hours, final int min) {
		return "Anti Rain Rocket was launched. Clear sky for the next " + time + " Ticks (" + days + " Days " + hours
				+ " Hours " + min + " Minutes)";
	}

	@Override
	protected void registerData() {
		this.dataManager.register(ANTI_RAINROCKET_ITEM, ItemStack.EMPTY);
		this.dataManager.register(BOOSTED_ANTI_RAINROCKET_ENTITY_ID, OptionalInt.empty());
		this.dataManager.register(SHOT_AT_ANGLE_ANTI_RAINROCKET_BOOLEAN, Boolean.valueOf(false));
	}

	@Override
	public void remove() {
		if (!this.getEntityWorld().isRemote) {
			final WorldInfo worldInfo = this.getEntityWorld().getWorldInfo();
			worldInfo.setClearWeatherTime(this.realClearSky);
			worldInfo.setRainTime(0);
			worldInfo.setThunderTime(0);
			worldInfo.setRaining(false);
			worldInfo.setThundering(false);

			final BlockPos pos = this.getPosition();
			dropSponge(pos);
		}
		super.remove();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public ItemStack getItem() {
		final ItemStack itemstack = getRocketItem();
		return itemstack.isEmpty() ? new ItemStack(WaterworksItems.itemFireworkAntiRain) : itemstack;
	}

	@Override
	public boolean isShotAtAngle() {
		final Boolean shotAtAngle = this.dataManager.get(SHOT_AT_ANGLE_ANTI_RAINROCKET_BOOLEAN);
		return shotAtAngle != null && shotAtAngle.booleanValue();
	}

	@Override
	public void setShotAtAngle(Boolean shotAtAngle) {
		this.dataManager.set(SHOT_AT_ANGLE_ANTI_RAINROCKET_BOOLEAN, shotAtAngle);
	}

	@Override
	public ItemStack getRocketItem() {
		return this.dataManager.get(ANTI_RAINROCKET_ITEM);
	}

	@Override
	public void setRocketItem(ItemStack givenItem) {
		this.dataManager.set(ANTI_RAINROCKET_ITEM, givenItem.copy());
	}

	@Override
	public OptionalInt getBoostedEntity() {
		return this.dataManager.get(BOOSTED_ANTI_RAINROCKET_ENTITY_ID);
	}

	@Override
	public void setBoostedEntity(LivingEntity entity) {
		this.dataManager.set(BOOSTED_ANTI_RAINROCKET_ENTITY_ID, OptionalInt.of(entity.getEntityId()));
	}

	@Override
	protected int calculateDurationFromMultiplier(int rainMultiplier) {
		final int minimumClearSky = WaterworksConfig.rockets.getClearSkyDuration() * this.durationMultiplier;
		this.realClearSky = minimumClearSky + calculateRealClearSky(this.durationMultiplier);
		return this.realClearSky;
	}

	@Override
	protected String getRocketTypeTag() {
		return "ANTIRAIN";
	}

	@Override
	protected int getConfiguredDuration() {
		return WaterworksConfig.rockets.getClearSkyDuration();
	}

	private int calculateRealClearSky(int multiplier) {
		final int MAX_CLEAR_DAYS = WaterworksConfig.rockets.getClearSkyMaxRandomAdditionalDays();
		if (MAX_CLEAR_DAYS == 0) {
			return 0;
		}
		final int DAYTICKS = 24000;
		final int maxClearTicks = MAX_CLEAR_DAYS * DAYTICKS;
		final float multi = (WaterworksConfig.rockets.getClearSkyMaxMultiplier()) / ((multiplier + 0.001f) * 6);
		final float randomMultiplier = multi * (this.rand.nextFloat() * 48) + 1;
		double log = Math.log(randomMultiplier) / 4;
		if (log > 1) {
			log = 1;
		}
		return (int) Math.round((maxClearTicks - maxClearTicks * log));
	}

	private void dropSponge(BlockPos pos) {
		if (this.durationMultiplier >= 1) {
			for (int i = 0; i < this.durationMultiplier; i++) {
				this.getEntityWorld()
						.addEntity(new ItemEntity(this.getEntityWorld(), pos.getX() + this.rand.nextDouble() * 2 - 1.0d,
								pos.getY() + this.rand.nextDouble() * 2 - 1.0d,
								pos.getZ() + this.rand.nextDouble() * 2 - 1.0d,
								this.getEntityWorld().isRaining()
										? new ItemStack(Blocks.WET_SPONGE, 1)
										: new ItemStack(Blocks.SPONGE, 1)));
			}
		}
	}
}
