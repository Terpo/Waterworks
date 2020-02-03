package org.terpo.waterworks.entity.item;

import java.util.OptionalInt;

import org.terpo.waterworks.Config;
import org.terpo.waterworks.setup.Registration;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class EntityFireworkRocketAntiRain extends EntityWeatherFireworkRocket implements IRendersAsItem {

	private static final DataParameter<ItemStack> ANTI_RAINROCKET_ITEM = EntityDataManager.createKey(EntityFireworkRocketAntiRain.class,
			DataSerializers.ITEMSTACK);
	private static final DataParameter<OptionalInt> BOOSTED_ANTI_RAINROCKET_ENTITY_ID = EntityDataManager
			.createKey(EntityFireworkRocketAntiRain.class, DataSerializers.OPTIONAL_VARINT);
	private static final DataParameter<Boolean> SHOT_AT_ANGLE_ANTI_RAINROCKET_BOOLEAN = EntityDataManager
			.createKey(EntityFireworkRocketAntiRain.class, DataSerializers.BOOLEAN);

	public EntityFireworkRocketAntiRain(EntityType<? extends EntityFireworkRocketAntiRain> entity, World world) {
		super(entity, world);
	}

	public EntityFireworkRocketAntiRain(World worldIn, double x, double y, double z, ItemStack itemstack) {
		super(Registration.fireworkAntiRainEntity.get(), worldIn, x, y, z, itemstack);
	}

	public EntityFireworkRocketAntiRain(World worldIn, ItemStack itemstack, LivingEntity entity) {
		super(Registration.fireworkAntiRainEntity.get(), worldIn, itemstack, entity);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public String getAnnouncementText(int time, final int days, final int hours, final int min) {
		return new TranslationTextComponent("entity.anti_rain_rocket.announcement", Integer.valueOf(time), Integer.valueOf(days),
				Integer.valueOf(hours), Integer.valueOf(min)).getFormattedText();
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
			worldInfo.setClearWeatherTime(this.duration);
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
		return itemstack.isEmpty() ? new ItemStack(Registration.fireworkAntiRainItem.get()) : itemstack;
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
	protected int calculateDurationFromMultiplier(int antiRainMultiplier) {
		final int minimumClearSky = Config.rockets.getClearSkyDuration() * antiRainMultiplier;
		return minimumClearSky + calculateAdditionalTime(antiRainMultiplier);
	}

	@Override
	protected String getRocketTypeTag() {
		return "ANTIRAIN";
	}

	@Override
	protected int getConfiguredDuration() {
		return Config.rockets.getClearSkyDuration();
	}

	private int calculateAdditionalTime(int multiplier) {
		final int maximumClearAdditionalDays = Config.rockets.getClearSkyMaxRandomAdditionalDays();
		if (maximumClearAdditionalDays == 0) {
			return 0;
		}
		final int maxClearTicks = maximumClearAdditionalDays * 24000; // 24000 day ticks
		final float multi = (Config.rockets.getClearSkyMaxMultiplier()) / ((multiplier + 0.001f) * 6);
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
								pos.getY() + this.rand.nextDouble() * 2 - 1.0d, pos.getZ() + this.rand.nextDouble() * 2 - 1.0d,
								this.getEntityWorld().isRaining() ? new ItemStack(Blocks.WET_SPONGE, 1) : new ItemStack(Blocks.SPONGE, 1)));
			}
		}
	}
	@OnlyIn(Dist.CLIENT)
	@Override
	public ItemStack getItemFromEntity() {
		return getItem();
	}
}
