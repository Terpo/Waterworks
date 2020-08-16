package org.terpo.waterworks.entity.item;

import java.util.OptionalInt;


import javax.annotation.Nullable;

import org.terpo.waterworks.Waterworks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public abstract class EntityWeatherFireworkRocket extends ProjectileEntity
		implements IRendersAsItem, IEntityAdditionalSpawnData {

	public static final String NBT_FIREWORKS = "Fireworks";
	public static final String NBT_SHOT_AT_ANGLE = "ShotAtAngle";

	/** The age of the firework in ticks. */
	protected int fireworkAge;
	/**
	 * The lifetime of the firework in ticks. When the age reaches the lifetime the
	 * firework explodes.
	 */
	protected int lifetime;
	protected LivingEntity boostedEntity;

	protected int duration;
	protected int durationMultiplier;

	public EntityWeatherFireworkRocket(EntityType<? extends EntityWeatherFireworkRocket> entity, World world) {
		super(entity, world);
	}

	public EntityWeatherFireworkRocket(EntityType<? extends EntityWeatherFireworkRocket> entityType, World worldIn,
			double x, double y, double z, ItemStack givenItem) {
		super(entityType, worldIn);
		this.setPosition(x, y, z);
		this.duration = getConfiguredDuration();
		this.durationMultiplier = -1;
		this.fireworkAge = 0;
		int i = 1;
		if (!givenItem.isEmpty() && givenItem.hasTag()) {
			setRocketItem(givenItem);
			i += givenItem.getOrCreateChildTag(NBT_FIREWORKS).getByte("Flight");
		}

		this.setMotion(this.rand.nextGaussian() * 0.001D, 0.05D, this.rand.nextGaussian() * 0.001D);
		this.lifetime = 10 * i + this.rand.nextInt(6) + this.rand.nextInt(7);

		if (givenItem.hasTag()) {
			final CompoundNBT tag = givenItem.getTag();

			if (tag.contains(getRocketTypeTag())) {
				this.durationMultiplier = tag.getInt(getRocketTypeTag());
			}
			if (this.durationMultiplier != -1) {
				this.duration = calculateDurationFromMultiplier(this.durationMultiplier);
			}
		}
	}

	public EntityWeatherFireworkRocket(EntityType<? extends EntityWeatherFireworkRocket> entityType, World world,
			ItemStack itemStack, LivingEntity entity) {
		this(entityType, world, entity.getX(), entity.getY(), entity.getZ(), itemStack);
		setBoostedEntity(entity);
		this.boostedEntity = entity;
	}

	public EntityWeatherFireworkRocket(EntityType<? extends EntityWeatherFireworkRocket> entityType, World world,
			ItemStack itemStack, double x, double y, double z, boolean shotAtAngle) {
		this(entityType, world, x, y, z, itemStack);
		setShotAtAngle(Boolean.valueOf(shotAtAngle));
	}

	/**
	 * Handler for {@link World#setEntityState}
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void handleStatusUpdate(byte id) {
		if (id == 17 && this.world.isRemote) {
			final ItemStack itemstack = getRocketItem();
			final CompoundNBT compoundnbt = itemstack.isEmpty() ? null : itemstack.getChildTag(NBT_FIREWORKS);
			final Vector3d vec3d = this.getMotion();
			this.world.makeFireworks(this.getX(), this.getY(), this.getZ(), vec3d.x, vec3d.y, vec3d.z, compoundnbt);

		}

		super.handleStatusUpdate(id);
	}

	@OnlyIn(Dist.CLIENT)
	protected void announceRocket(int time) {
		final int days = time / 24000;
		final int hours = (time % 24000) / 1000;
		final int min = ((time % 24000) % 1000) / 17;
		Waterworks.proxy.getClientPlayerEntity()
				.sendMessage(getAnnouncementText(time, days, hours, min), Util.NIL_UUID);
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void tick() { // NOSONAR
		super.tick();
		if (this.isAttachedToEntity()) {
			if (this.boostedEntity == null) {
				getBoostedEntity().ifPresent(entityId -> {
					final Entity entity = this.world.getEntityByID(entityId);
					if (entity instanceof LivingEntity) {
						this.boostedEntity = (LivingEntity) entity;
					}

				});
			}

			if (this.boostedEntity != null) {
				if (this.boostedEntity.isElytraFlying()) {
					Vector3d vector3d = this.boostedEntity.getLookVec();
					Vector3d vector3d1 = this.boostedEntity.getMotion();
					this.boostedEntity
							.setMotion(vector3d1.add(vector3d.x * 0.1D + (vector3d.x * 1.5D - vector3d1.x) * 0.5D,
									vector3d.y * 0.1D + (vector3d.y * 1.5D - vector3d1.y) * 0.5D,
									vector3d.z * 0.1D + (vector3d.z * 1.5D - vector3d1.z) * 0.5D));
				}

				this.setPosition(this.boostedEntity.getX(), this.boostedEntity.getY(), this.boostedEntity.getZ());
				this.setMotion(this.boostedEntity.getMotion());
			}
		} else {
			if (!this.isShotAtAngle()) {
				this.setMotion(this.getMotion().mul(1.15D, 1.0D, 1.15D).add(0.0D, 0.04D, 0.0D));
			}

			Vector3d vector3d2 = this.getMotion();
			this.move(MoverType.SELF, vector3d2);
			this.setMotion(vector3d2);
		}

		RayTraceResult raytraceresult = ProjectileHelper.getCollision(this, this::func_230298_a_,
				RayTraceContext.BlockMode.COLLIDER);

		if (!this.noClip) {
			this.onImpact(raytraceresult);
			this.isAirBorne = true;
		}

		this.func_234617_x_();
		if (this.fireworkAge == 0 && !this.isSilent()) {
			this.world.playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(),
					SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.AMBIENT, 3.0F, 1.0F);
		}

		if (this.world.isRemote && this.fireworkAge == 0) {
			announceRocket(this.duration);
		}

		++this.fireworkAge;
		if (this.world.isRemote && this.fireworkAge % 2 < 2) {
			this.world.addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY() - 0.3D, this.getZ(),
					this.rand.nextGaussian() * 0.05D, -this.getMotion().y * 0.5D, this.rand.nextGaussian() * 0.05D);
		}

		if (!this.world.isRemote && this.fireworkAge > this.lifetime) {
			this.setDead();
		}

	}

	private void setDead() {
		this.world.setEntityState(this, (byte) 17);
		this.dealExplosionDamage();
		this.remove();
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result.getType() == RayTraceResult.Type.MISS
				|| !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, result)) {
			super.onImpact(result);
		}
	}

	// eclipse thinks listnbt has a potential null pointer access
	@SuppressWarnings("null")
	private void dealExplosionDamage() { // NOSONAR
		float f = 0.0F;
		final ItemStack itemstack = getRocketItem();
		final CompoundNBT compoundnbt = itemstack.isEmpty() ? null : itemstack.getChildTag(NBT_FIREWORKS);
		ListNBT listnbt = compoundnbt != null ? compoundnbt.getList("Explosions", 10) : null;
		if (listnbt != null && !listnbt.isEmpty()) {
			f = 5.0F + (float) (listnbt.size() * 2);
		}

		if (f > 0.0F) {
			if (this.boostedEntity != null) {
				this.boostedEntity.attackEntityFrom(firework(this, this.getOwner()),
						5.0F + (float) (listnbt.size() * 2));
			}

			Vector3d vector3d = this.getPositionVec();

			for (LivingEntity livingentity : this.world.getEntitiesWithinAABB(LivingEntity.class,
					this.getBoundingBox().grow(5.0D))) {
				if (livingentity != this.boostedEntity && (this.getDistanceSq(livingentity) <= 25.0D)) {
					boolean flag = false;

					for (int i = 0; i < 2; ++i) {
						Vector3d vector3d1 = new Vector3d(livingentity.getX(), livingentity.getBodyY(0.5D * (double) i),
								livingentity.getZ());
						RayTraceResult raytraceresult = this.world.rayTraceBlocks(new RayTraceContext(vector3d,
								vector3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
						if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
							flag = true;
							break;
						}
					}

					if (flag) {
						float f1 = f * (float) Math.sqrt((5.0D - (double) this.getDistance(livingentity)) / 5.0D);
						livingentity.attackEntityFrom(firework(this, this.getOwner()), f1);
					}
				}
			}
		}

	}

	private static DamageSource firework(EntityWeatherFireworkRocket enitity, @Nullable Entity owner) {
		return (new IndirectEntityDamageSource("fireworks", enitity, owner)).setExplosion();
	}

	/**
	 * Returns true if it's possible to attack this entity with an item.
	 */
	@Override
	public boolean canBeAttackedWithItem() {
		return false;
	}

	private boolean isAttachedToEntity() {
		return getBoostedEntity().isPresent();
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	/**
	 * Similar to setArrowHeading, it's point the throwable entity to a x, y, z
	 * direction.
	 */
	@Override
	public void shoot(double incomingX, double incomingY, double incomingZ, float velocity, float inaccuracy) {
		final float f = MathHelper.sqrt(incomingX * incomingX + incomingY * incomingY + incomingZ * incomingZ);
		double x = incomingX / f;
		double y = incomingY / f;
		double z = incomingZ / f;
		x = x + this.rand.nextGaussian() * 0.0075F * inaccuracy;
		y = y + this.rand.nextGaussian() * 0.0075F * inaccuracy;
		z = z + this.rand.nextGaussian() * 0.0075F * inaccuracy;
		x = x * velocity;
		y = y * velocity;
		z = z * velocity;
		this.setMotion(x, y, z);
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		buffer.writeInt(this.duration);
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		this.duration = additionalData.readInt();
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		compound.putInt("Life", this.fireworkAge);
		compound.putInt("LifeTime", this.lifetime);
		final ItemStack itemstack = getRocketItem();
		if (!itemstack.isEmpty()) {
			compound.put("FireworksItem", itemstack.write(new CompoundNBT()));
		}
		compound.putBoolean(NBT_SHOT_AT_ANGLE, isShotAtAngle());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		this.fireworkAge = compound.getInt("Life");
		this.lifetime = compound.getInt("LifeTime");
		final ItemStack itemstack = ItemStack.read(compound.getCompound("FireworksItem"));
		if (!itemstack.isEmpty()) {
			setRocketItem(itemstack);
		}

		if (compound.contains(NBT_SHOT_AT_ANGLE)) {
			setShotAtAngle(Boolean.valueOf(compound.getBoolean(NBT_SHOT_AT_ANGLE)));
		}
	}

	@Override
	public boolean equals(Object entity) {
		return super.equals(entity);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Override this methods to use your own registry fields
	 *
	 */

	@OnlyIn(Dist.CLIENT)
	protected abstract ITextComponent getAnnouncementText(int time, final int days, final int hours, final int min);

	@Override
	protected abstract void registerData();

	@OnlyIn(Dist.CLIENT)
	public abstract ItemStack getItemFromEntity();

	protected abstract boolean isShotAtAngle();

	protected abstract void setShotAtAngle(Boolean shotAtAngle);

	protected abstract ItemStack getRocketItem();

	protected abstract void setRocketItem(ItemStack givenItem);

	protected abstract OptionalInt getBoostedEntity();

	protected abstract void setBoostedEntity(LivingEntity entity);

	protected abstract int calculateDurationFromMultiplier(int rainMultiplier);

	protected abstract String getRocketTypeTag();

	protected abstract int getConfiguredDuration();
}
