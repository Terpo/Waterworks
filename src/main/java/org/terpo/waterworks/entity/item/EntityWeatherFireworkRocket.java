package org.terpo.waterworks.entity.item;

import java.util.OptionalInt;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class EntityWeatherFireworkRocket extends Entity implements IRendersAsItem, IProjectile {

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

	public EntityWeatherFireworkRocket(EntityType<?> entityType, World worldIn, double x, double y, double z,
			ItemStack givenItem) {
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
				if (!worldIn.isRemote) {
					announceRocket(this.duration);
				}
			}
		}
	}

	public EntityWeatherFireworkRocket(EntityType<?> entityType, World world, ItemStack itemStack,
			LivingEntity entity) {
		this(entityType, world, entity.posX, entity.posY, entity.posZ, itemStack);
		setBoostedEntity(entity);
		this.boostedEntity = entity;
	}

	public EntityWeatherFireworkRocket(EntityType<?> entityType, World world, ItemStack itemStack, double x, double y,
			double z, boolean shotAtAngle) {
		this(entityType, world, x, y, z, itemStack);
		setShotAtAngle(Boolean.valueOf(shotAtAngle));
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void tick() {
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
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
					final Vec3d vec3d = this.boostedEntity.getLookVec();
					final Vec3d vec3d1 = this.boostedEntity.getMotion();
					this.boostedEntity.setMotion(vec3d1.add(vec3d.x * 0.1D + (vec3d.x * 1.5D - vec3d1.x) * 0.5D,
							vec3d.y * 0.1D + (vec3d.y * 1.5D - vec3d1.y) * 0.5D,
							vec3d.z * 0.1D + (vec3d.z * 1.5D - vec3d1.z) * 0.5D));
				}

				this.setPosition(this.boostedEntity.posX, this.boostedEntity.posY, this.boostedEntity.posZ);
				this.setMotion(this.boostedEntity.getMotion());
			}
		} else {
			if (!this.isShotAtAngle()) {
				this.setMotion(this.getMotion().mul(1.15D, 1.0D, 1.15D).add(0.0D, 0.04D, 0.0D));
			}

			this.move(MoverType.SELF, this.getMotion());
		}

		final Vec3d vec3d2 = this.getMotion();
		final RayTraceResult raytraceresult = ProjectileHelper.func_221267_a(this,
				this.getBoundingBox().expand(vec3d2).grow(1.0D),
				entitiy -> !entitiy.isSpectator() && entitiy.isAlive() && entitiy.canBeCollidedWith(),
				RayTraceContext.BlockMode.COLLIDER, true);
		if (!this.noClip) {
			this.computeRayTraceResult(raytraceresult);
			this.isAirBorne = true;
		}

		final float f = MathHelper.sqrt(func_213296_b(vec3d2));
		this.rotationYaw = (float) (MathHelper.atan2(vec3d2.x, vec3d2.z) * (180F / (float) Math.PI));

		for (this.rotationPitch = (float) (MathHelper.atan2(vec3d2.y, f) * (180F / (float) Math.PI)); this.rotationPitch
				- this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
			//
		}

		while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = MathHelper.lerp(0.2F, this.prevRotationPitch, this.rotationPitch);
		this.rotationYaw = MathHelper.lerp(0.2F, this.prevRotationYaw, this.rotationYaw);
		if (this.fireworkAge == 0 && !this.isSilent()) {
			this.world.playSound((PlayerEntity) null, this.posX, this.posY, this.posZ,
					SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.AMBIENT, 3.0F, 1.0F);
		}

		++this.fireworkAge;
		if (this.world.isRemote && this.fireworkAge % 2 < 2) {
			this.world.addParticle(ParticleTypes.FIREWORK, this.posX, this.posY - 0.3D, this.posZ,
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

	protected void computeRayTraceResult(RayTraceResult rayTraceResult) {
		if (rayTraceResult.getType() == RayTraceResult.Type.ENTITY && !this.world.isRemote) {
			this.setDead();
		} else if (this.collided) {
			BlockPos blockpos;
			if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
				blockpos = new BlockPos(((BlockRayTraceResult) rayTraceResult).getPos());
			} else {
				blockpos = new BlockPos(this);
			}

			this.world.getBlockState(blockpos).onEntityCollision(this.world, blockpos, this);
			if (this.hasExplosions()) {
				this.setDead();
			}
		}

	}

	private boolean hasExplosions() {
		final ItemStack itemstack = getRocketItem();
		final CompoundNBT compoundnbt = itemstack.isEmpty() ? null : itemstack.getChildTag(NBT_FIREWORKS);
		final ListNBT listnbt = compoundnbt != null ? compoundnbt.getList("Explosions", 10) : null;
		return listnbt != null && !listnbt.isEmpty();
	}

	// eclipse thinks listnbt has a potential null pointer access
	@SuppressWarnings("null")
	private void dealExplosionDamage() {
		float f = 0.0F;
		final ItemStack itemstack = getRocketItem();
		final CompoundNBT compoundnbt = itemstack.isEmpty() ? null : itemstack.getChildTag(NBT_FIREWORKS);

		final ListNBT listnbt = compoundnbt != null ? compoundnbt.getList("Explosions", 10) : null;
		if (listnbt != null && !listnbt.isEmpty()) {
			f = 5.0F + listnbt.size() * 2;
		}

		if (f > 0.0F) {
			if (this.boostedEntity != null) {

				this.boostedEntity.attackEntityFrom(DamageSource.FIREWORKS, 5.0F + listnbt.size() * 2);
			}

			final Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);

			for (final LivingEntity livingentity : this.world.getEntitiesWithinAABB(LivingEntity.class,
					this.getBoundingBox().grow(5.0D))) {
				if (livingentity != this.boostedEntity && (this.getDistanceSq(livingentity) <= 25.0D)) {
					boolean flag = false;

					for (int i = 0; i < 2; ++i) {
						final Vec3d vec3d1 = new Vec3d(livingentity.posX,
								livingentity.posY + livingentity.getHeight() * 0.5D * i, livingentity.posZ);
						final RayTraceResult raytraceresult = this.world.rayTraceBlocks(new RayTraceContext(vec3d,
								vec3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
						if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
							flag = true;
							break;
						}
					}

					if (flag) {
						final float f1 = f * (float) Math.sqrt((5.0D - this.getDistance(livingentity)) / 5.0D);
						livingentity.attackEntityFrom(DamageSource.FIREWORKS, f1);
					}
				}
			}
		}

	}

	private boolean isAttachedToEntity() {
		return getBoostedEntity().isPresent();
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
			final Vec3d vec3d = this.getMotion();
			this.world.makeFireworks(this.posX, this.posY, this.posZ, vec3d.x, vec3d.y, vec3d.z, compoundnbt);

		}

		super.handleStatusUpdate(id);
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

	/**
	 * Returns true if it's possible to attack this entity with an item.
	 */
	@Override
	public boolean canBeAttackedWithItem() {
		return false;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return new SSpawnObjectPacket(this);
	}

	protected void announceRocket(int time) {
		final int days = time / 24000;
		final int hours = (time % 24000) / 1000;
		final int min = ((time % 24000) % 1000) / 17;
		final String announcement = getAnnouncementText(time, days, hours, min);
		ServerLifecycleHooks.getCurrentServer().getPlayerList().sendMessage(new StringTextComponent(announcement));
	}

	/**
	 * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
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

	/**
	 * Override this methods to use your own registry fields
	 *
	 */

	@Override
	protected void registerData() {
		//
	}

	@SuppressWarnings("static-method")
	public boolean isShotAtAngle() {
		return false;
	}

	@SuppressWarnings("unused")
	public void setShotAtAngle(Boolean shotAtAngle) {
		//
	}

	@SuppressWarnings("static-method")
	public ItemStack getRocketItem() {
		return ItemStack.EMPTY;
	}

	@SuppressWarnings("unused")
	public void setRocketItem(ItemStack givenItem) {
		//
	}

	@SuppressWarnings("static-method")
	public OptionalInt getBoostedEntity() {
		return OptionalInt.of(0);
	}

	@SuppressWarnings("unused")
	public void setBoostedEntity(LivingEntity entity) {
		//
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public ItemStack getItem() {
		return ItemStack.EMPTY;
	}

	@SuppressWarnings({"static-method", "unused"})
	protected String getAnnouncementText(int time, final int days, final int hours, final int min) {
		final String announcement = "";
		return announcement;
	}

	@SuppressWarnings({"static-method", "unused"})
	protected int calculateDurationFromMultiplier(int rainMultiplier) {
		return 0;
	}

	@SuppressWarnings("static-method")
	protected String getRocketTypeTag() {
		return "";
	}

	protected int getConfiguredDuration() {
		return 0;
	}

}
