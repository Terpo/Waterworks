package org.terpo.waterworks.entity.item;

import java.util.OptionalInt;

import org.terpo.waterworks.Config;
import org.terpo.waterworks.setup.Registration;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class EntityFireworkRocketRain extends EntityWeatherFireworkRocket implements IRendersAsItem {

	private static final DataParameter<ItemStack> RAINROCKET_ITEM = EntityDataManager.createKey(EntityFireworkRocketRain.class,
			DataSerializers.ITEMSTACK);
	private static final DataParameter<OptionalInt> BOOSTED_RAINROCKET_ENTITY_ID = EntityDataManager
			.createKey(EntityFireworkRocketRain.class, DataSerializers.OPTIONAL_VARINT);
	private static final DataParameter<Boolean> SHOT_AT_ANGLE_RAINROCKET_BOOLEAN = EntityDataManager
			.createKey(EntityFireworkRocketRain.class, DataSerializers.BOOLEAN);

	public EntityFireworkRocketRain(EntityType<? extends EntityFireworkRocketRain> entity, World world) {
		super(entity, world);
	}

	public EntityFireworkRocketRain(World worldIn, double x, double y, double z, ItemStack itemstack) {
		super(Registration.fireworkRainEntity.get(), worldIn, x, y, z, itemstack);
	}

	public EntityFireworkRocketRain(World worldIn, ItemStack itemstack, LivingEntity entity) {
		super(Registration.fireworkRainEntity.get(), worldIn, itemstack, entity);
	}

	@Override
	protected void registerData() {
		this.dataManager.register(RAINROCKET_ITEM, ItemStack.EMPTY);
		this.dataManager.register(BOOSTED_RAINROCKET_ENTITY_ID, OptionalInt.empty());
		this.dataManager.register(SHOT_AT_ANGLE_RAINROCKET_BOOLEAN, Boolean.valueOf(false));
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public ItemStack getItem() {
		final ItemStack itemstack = getRocketItem();
		return itemstack.isEmpty() ? new ItemStack(Registration.fireworkRainItem.get()) : itemstack;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public String getAnnouncementText(int time, final int days, final int hours, final int min) {
		return new TranslationTextComponent("entity.rain_rocket.announcement", Integer.valueOf(time), Integer.valueOf(days),
				Integer.valueOf(hours), Integer.valueOf(min)).getFormattedText();
	}

	@Override
	public void remove() {
		if (!this.getEntityWorld().isRemote) {
			final WorldInfo worldInfo = this.getEntityWorld().getWorldInfo();
			worldInfo.setClearWeatherTime(0);
			worldInfo.setRainTime(this.duration);
			worldInfo.setThunderTime(this.duration);
			worldInfo.setRaining(true);
			worldInfo.setThundering(this.rand.nextInt(10) > 6);
		}
		super.remove();
	}

	@Override
	public boolean isShotAtAngle() {
		final Boolean shotAtAngle = this.dataManager.get(SHOT_AT_ANGLE_RAINROCKET_BOOLEAN);
		return shotAtAngle != null && shotAtAngle.booleanValue();
	}

	@Override
	public void setShotAtAngle(Boolean shotAtAngle) {
		this.dataManager.set(SHOT_AT_ANGLE_RAINROCKET_BOOLEAN, shotAtAngle);
	}

	@Override
	public ItemStack getRocketItem() {
		return this.dataManager.get(RAINROCKET_ITEM);
	}

	@Override
	public void setRocketItem(ItemStack givenItem) {
		this.dataManager.set(RAINROCKET_ITEM, givenItem.copy());
	}

	@Override
	public OptionalInt getBoostedEntity() {
		return this.dataManager.get(BOOSTED_RAINROCKET_ENTITY_ID);
	}

	@Override
	public void setBoostedEntity(LivingEntity entity) {
		this.dataManager.set(BOOSTED_RAINROCKET_ENTITY_ID, OptionalInt.of(entity.getEntityId()));
	}

	@Override
	protected int calculateDurationFromMultiplier(int rainMultiplier) {
		return Config.rockets.getRainDuration() * rainMultiplier;
	}

	@Override
	protected String getRocketTypeTag() {
		return "RAIN";
	}

	@Override
	protected int getConfiguredDuration() {
		return Config.rockets.getRainDuration();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public ItemStack getItemFromEntity() {
		return getItem();
	}
}
