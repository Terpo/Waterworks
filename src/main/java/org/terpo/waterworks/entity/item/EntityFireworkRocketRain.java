package org.terpo.waterworks.entity.item;

import org.terpo.waterworks.init.WaterworksConfig;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityFireworkRocketRain extends Entity {
	protected static DataParameter<ItemStack> RAINROCKET_ITEM = EntityDataManager
			.<ItemStack>createKey(EntityFireworkRocketRain.class, DataSerializers.ITEM_STACK);
	protected static DataParameter<Integer> RAINROCKET_ITEM_INT = EntityDataManager
			.<Integer>createKey(EntityFireworkRocketRain.class, DataSerializers.VARINT);
	/** The age of the firework in ticks. */
	private int fireworkAge;
	/**
	 * The lifetime of the firework in ticks. When the age reaches the lifetime the
	 * firework explodes.
	 */
	private int lifetime;
	private EntityLivingBase entityPlacer;

	private int rainDuration = WaterworksConfig.rockets.rainDuration;

	public EntityFireworkRocketRain(World worldIn) {
		super(worldIn);
		this.setSize(0.25F, 0.25F);
	}
	public EntityFireworkRocketRain(World worldIn, double x, double y, double z, ItemStack givenItem) {
		super(worldIn);
		this.fireworkAge = 0;
		this.setSize(0.25F, 0.25F);
		this.setPosition(x, y, z);
		int i = 1;

		if (!givenItem.isEmpty() && givenItem.hasTagCompound()) {
			this.dataManager.set(RAINROCKET_ITEM, givenItem.copy());
			final NBTTagCompound nbttagcompound = givenItem.getTagCompound();
			final NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Fireworks");
			i += nbttagcompound1.getByte("Flight");
		}

		this.motionX = this.rand.nextGaussian() * 0.001D;
		this.motionZ = this.rand.nextGaussian() * 0.001D;
		this.motionY = 0.05D;
		this.lifetime = 10 * i + this.rand.nextInt(6) + this.rand.nextInt(7);

		if (givenItem.hasTagCompound()) {
			final NBTTagCompound tag = givenItem.getTagCompound();
			int rainMultiplier = -1;
			if (tag.hasKey("RAIN")) {
				rainMultiplier = tag.getInteger("RAIN");
			}
			if (rainMultiplier != -1) {
				this.rainDuration = WaterworksConfig.rockets.rainDuration * rainMultiplier;
				if (!worldIn.isRemote) {
					announceRocket(this.rainDuration);
				}
			}
		}

	}

	public EntityFireworkRocketRain(World worldIn, ItemStack itemStack, EntityLivingBase entityLivingbase) {
		this(worldIn, entityLivingbase.posX, entityLivingbase.posY, entityLivingbase.posZ, itemStack);
		this.dataManager.set(RAINROCKET_ITEM_INT, Integer.valueOf(entityLivingbase.getEntityId()));
		this.entityPlacer = entityLivingbase;
	}

	private static void announceRocket(int time) {
		final int days = time / 24000;
		final int hours = (time % 24000) / 1000;
		final int min = ((time % 24000) % 1000) / 17;
		final String announcement = "Rain Rocket was launched. Bad weather for the next " + time + " Ticks (" + days
				+ " Days " + hours + " Hours " + min + " Minutes)";
		FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
				.sendMessage(new TextComponentString(announcement));
	}

	@Override
	protected void entityInit() {
		this.dataManager.register(RAINROCKET_ITEM, ItemStack.EMPTY);
		this.dataManager.register(RAINROCKET_ITEM_INT, Integer.valueOf(0));
	}

	/**
	 * Checks if the entity is in range to render.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		return distance < 4096.0D && !this.getRocketIntValue();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRender3d(double x, double y, double z) {
		return super.isInRangeToRender3d(x, y, z) && !this.getRocketIntValue();
	}

	/**
	 * Updates the velocity of the entity to a new value.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z) {
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			final float f = MathHelper.sqrt(x * x + z * z);
			this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
			this.rotationPitch = (float) (MathHelper.atan2(y, f) * (180D / Math.PI));
			this.prevRotationYaw = this.rotationYaw;
			this.prevRotationPitch = this.rotationPitch;
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		super.onUpdate();

		if (this.getRocketIntValue()) {
			if (this.entityPlacer == null) {
				final Entity entity = this.world.getEntityByID(this.dataManager.get(RAINROCKET_ITEM_INT).intValue());

				if (entity instanceof EntityLivingBase) {
					this.entityPlacer = (EntityLivingBase) entity;
				}
			}

			if (this.entityPlacer != null) {
				if (this.entityPlacer.isElytraFlying()) {
					final Vec3d vec3d = this.entityPlacer.getLookVec();
					this.entityPlacer.motionX += vec3d.x * 0.1D + (vec3d.x * 1.5D - this.entityPlacer.motionX) * 0.5D;
					this.entityPlacer.motionY += vec3d.y * 0.1D + (vec3d.y * 1.5D - this.entityPlacer.motionY) * 0.5D;
					this.entityPlacer.motionZ += vec3d.z * 0.1D + (vec3d.z * 1.5D - this.entityPlacer.motionZ) * 0.5D;
				}

				this.setPosition(this.entityPlacer.posX, this.entityPlacer.posY, this.entityPlacer.posZ);
				this.motionX = this.entityPlacer.motionX;
				this.motionY = this.entityPlacer.motionY;
				this.motionZ = this.entityPlacer.motionZ;
			}
		} else {
			this.motionX *= 1.15D;
			this.motionZ *= 1.15D;
			this.motionY += 0.04D;
			this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
		}

		final float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

		for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, f) * (180D / Math.PI)); this.rotationPitch
				- this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
			// doNothing but roll
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

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

		if (this.fireworkAge == 0 && !this.isSilent()) {
			this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ,
					SoundEvents.ENTITY_FIREWORK_LAUNCH, SoundCategory.AMBIENT, 3.0F, 1.0F);
		}

		++this.fireworkAge;

		if (this.world.isRemote && this.fireworkAge % 2 < 2) {
			this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX, this.posY - 0.3D, this.posZ,
					this.rand.nextGaussian() * 0.05D, -this.motionY * 0.5D, this.rand.nextGaussian() * 0.05D,
					new int[0]);
		}

		if (!this.world.isRemote && this.fireworkAge > this.lifetime) {
			this.world.setEntityState(this, (byte) 17);
			this.damageEntities();
			this.setDead();
		}
	}

	private void damageEntities() {
		float f = 0.0F;
		final ItemStack itemstack = this.dataManager.get(RAINROCKET_ITEM);
		final NBTTagCompound nbttagcompound = itemstack.isEmpty() ? null : itemstack.getSubCompound("Fireworks");
		final NBTTagList nbttaglist = nbttagcompound != null ? nbttagcompound.getTagList("Explosions", 10) : null;

		if (nbttaglist != null && !nbttaglist.hasNoTags()) {
			f = 5 + nbttaglist.tagCount() * 2;
		}

		if (f > 0.0F) {
			if (this.entityPlacer != null) {
				this.entityPlacer.attackEntityFrom(DamageSource.FIREWORKS,
						5 + ((nbttaglist != null) ? nbttaglist.tagCount() : 1) * 2);
			}

			final Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);

			for (final EntityLivingBase entitylivingbase : this.world.getEntitiesWithinAABB(EntityLivingBase.class,
					this.getEntityBoundingBox().grow(5.0D))) {
				if (entitylivingbase != this.entityPlacer && this.getDistanceSq(entitylivingbase) <= 25.0D) {
					boolean flag = false;

					for (int i = 0; i < 2; ++i) {
						final RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d,
								new Vec3d(entitylivingbase.posX,
										entitylivingbase.posY + entitylivingbase.height * 0.5D * i,
										entitylivingbase.posZ),
								false, true, false);

						if (raytraceresult == null || raytraceresult.typeOfHit == RayTraceResult.Type.MISS) {
							flag = true;
							break;
						}
					}

					if (flag) {
						final float f1 = f * (float) Math.sqrt((5.0D - this.getDistance(entitylivingbase)) / 5.0D);
						entitylivingbase.attackEntityFrom(DamageSource.FIREWORKS, f1);
					}
				}
			}
		}
	}

	public boolean getRocketIntValue() {
		return this.dataManager.get(RAINROCKET_ITEM_INT).intValue() > 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id) {
		if (id == 17 && this.world.isRemote) {
			final ItemStack itemstack = this.dataManager.get(RAINROCKET_ITEM);
			final NBTTagCompound nbttagcompound = itemstack.isEmpty() ? null : itemstack.getSubCompound("Fireworks");
			this.world.makeFireworks(this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ,
					nbttagcompound);
		}

		super.handleStatusUpdate(id);
	}

	public static void registerFixesFireworkRocket(DataFixer fixer) {
		fixer.registerWalker(FixTypes.ENTITY,
				new ItemStackData(EntityFireworkRocketRain.class, new String[]{"FireworksItem"}));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("Life", this.fireworkAge);
		compound.setInteger("LifeTime", this.lifetime);
		final ItemStack itemstack = this.dataManager.get(RAINROCKET_ITEM);

		if (!itemstack.isEmpty()) {
			compound.setTag("FireworksItem", itemstack.writeToNBT(new NBTTagCompound()));
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		this.fireworkAge = compound.getInteger("Life");
		this.lifetime = compound.getInteger("LifeTime");
		final NBTTagCompound nbttagcompound = compound.getCompoundTag("FireworksItem");

		if (nbttagcompound != null) {
			final ItemStack itemstack = new ItemStack(nbttagcompound);

			if (!itemstack.isEmpty()) {
				this.dataManager.set(RAINROCKET_ITEM, itemstack);
			}
		}
	}

	@Override
	public boolean canBeAttackedWithItem() {
		return false;
	}

	@Override
	public void setDead() {
		final WorldInfo worldInfo = this.getEntityWorld().getWorldInfo();
		worldInfo.setCleanWeatherTime(0);
		worldInfo.setRainTime(this.rainDuration);
		worldInfo.setThunderTime(this.rainDuration);
		worldInfo.setRaining(true);
		if (this.rand.nextInt(10) > 6) {
			worldInfo.setThundering(true);
		} else {
			worldInfo.setThundering(false);
		}
		this.isDead = true;
	}

}
