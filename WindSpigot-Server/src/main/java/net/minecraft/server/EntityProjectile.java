package net.minecraft.server;

import java.util.List;

import com.eatthepath.uuid.FastUUID;

import ga.windpvp.windspigot.cache.Constants;
import ga.windpvp.windspigot.config.WindSpigotConfig;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.entity.LivingEntity;

public abstract class EntityProjectile extends Entity implements IProjectile {

	private int blockX = -1;
	private int blockY = -1;
	private int blockZ = -1;
	private Block inBlockId;
	protected boolean inGround;
	public int shake;
	public EntityLiving shooter;
	public String shooterName;
	private int i;
	private int ar;

	public EntityProjectile(World world) {
		super(world);
		this.setSize(0.25F, 0.25F);
	}

	@Override
	protected void h() {
	}

	public EntityProjectile(World world, EntityLiving entityliving) {
		super(world);
		this.shooter = entityliving;
		this.projectileSource = (LivingEntity) entityliving.getBukkitEntity();
		this.setSize(0.25f, 0.25f);
		this.setPositionRotation(entityliving.locX, entityliving.locY + (double)entityliving.getHeadHeight(), entityliving.locZ, entityliving.yaw, entityliving.pitch);
		this.locX -= (double)MathHelper.cos(this.yaw / 180.0f * (float)Math.PI) * (this instanceof EntityPotion && this.shooter instanceof EntityHuman ? (Double)((EntityHuman)this.shooter).getKnockBack().potionDistanceRadius.value : (double)0.16f);
		this.locY -= 0.1f;
		this.locZ -= (double)MathHelper.sin(this.yaw / 180.0f * (float)Math.PI) * (this instanceof EntityPotion && this.shooter instanceof EntityHuman ? (Double)((EntityHuman)this.shooter).getKnockBack().potionDistanceRadius.value : (double)0.16f);
		this.setPosition(this.locX, this.locY, this.locZ);
		float f = 0.4f;
		this.motX = -MathHelper.sin(this.yaw / 180.0f * (float)Math.PI) * MathHelper.cos(this.pitch / 180.0f * (float)Math.PI) * f;
		this.motZ = MathHelper.cos(this.yaw / 180.0f * (float)Math.PI) * MathHelper.cos(this.pitch / 180.0f * (float)Math.PI) * f;
		this.motY = -MathHelper.sin((this.pitch + this.l()) / 180.0f * (float)Math.PI) * f;
		this.shoot(this.motX, this.motY, this.motZ, this.j(), 1.0f);
	}
	public EntityProjectile(World world, double d0, double d1, double d2) {
		super(world);
		this.i = 0;
		this.setSize(0.25F, 0.25F);
		this.setPosition(d0, d1, d2);
	}

	protected float j() {
		return 1.5F;
	}

	protected float l() {
		return 0.0F;
	}

	@Override
	public void shoot(double d0, double d1, double d2, float f, float f1) {
		float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

		d0 /= f2;
		d1 /= f2;
		d2 /= f2;
		d0 += this.random.nextGaussian() * 0.007499999832361937D * f1;
		d1 += this.random.nextGaussian() * 0.007499999832361937D * f1;
		d2 += this.random.nextGaussian() * 0.007499999832361937D * f1;
		d0 *= f;
		d1 *= f;
		d2 *= f;
		this.motX = d0;
		this.motY = d1;
		this.motZ = d2;
		float f3 = MathHelper.sqrt(d0 * d0 + d2 * d2);

		this.lastYaw = this.yaw = (float) (MathHelper.b(d0, d2) * 180.0D / 3.1415927410125732D);
		this.lastPitch = this.pitch = (float) (MathHelper.b(d1, f3) * 180.0D / 3.1415927410125732D);
		this.i = 0;
	}

	@Override
	public void t_() {
		this.P = this.locX;
		this.Q = this.locY;
		this.R = this.locZ;
		super.t_();
		if (this.shake > 0) {
			--this.shake;
		}
		if (this.inGround) {
			if (this.world.getType(new BlockPosition(this.blockX, this.blockY, this.blockZ)).getBlock() == this.inBlockId) {
				++this.i;
				if (this.i == 1200) {
					this.die();
				}
				return;
			}
			this.inGround = false;
			this.motX *= 0.1f;
			this.motY *= 0.1f;
			this.motZ *= 0.1f;
			this.i = 0;
			this.ar = 0;
		} else {
			++this.ar;
		}
		Vec3D vec3d = new Vec3D(this.locX, this.locY, this.locZ);
		Vec3D vec3d1 = new Vec3D(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
		MovingObjectPosition movingobjectposition = this.world.rayTrace(vec3d, vec3d1);
		vec3d = new Vec3D(this.locX, this.locY, this.locZ);
		vec3d1 = new Vec3D(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
		if (movingobjectposition != null) {
			vec3d1 = new Vec3D(movingobjectposition.pos.a, movingobjectposition.pos.b, movingobjectposition.pos.c);
		}
		if (!this.world.isClientSide) {
			Entity entity = null;
			EntityLiving entityliving = this.getShooter();
			double j = 1.0;
			List<Entity> list = this.world.getEntities(this, this.getBoundingBox().a(this.motX, this.motY, this.motZ).grow(j, j, j));
			double d0 = 0.0;
			for (int i = 0; i < list.size(); ++i) {
				double d1;
				Entity entity1 = list.get(i);
				if (!entity1.ad()) {
					continue;
				}
				if (entity1 == entityliving) {
					if (this.ar < (this instanceof EntityPotion ? (entityliving instanceof EntityHuman ? ((EntityHuman)entityliving).getKnockBack().potionPlayerSpeed.value : 5) : 5)) {
						continue;
					}
				}
				double f = 0.3f;
				AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow(f, f, f);
				MovingObjectPosition movingobjectposition1 = axisalignedbb.a(vec3d, vec3d1);
				if (this instanceof EntityPotion && entityliving instanceof EntityHuman && ((EntityHuman) entityliving).getKnockBack().smoothPotting.value && movingobjectposition1 == null && this.getBoundingBox().b(entity1.getBoundingBox())) {
					movingobjectposition1 = new MovingObjectPosition(entity1);
				}
				if (movingobjectposition1 == null || !((d1 = vec3d.distanceSquared(movingobjectposition1.pos)) < d0) && d0 != 0.0) {
					continue;
				}
				entity = entity1;
				d0 = d1;
			}
			if (entity != null) {
				movingobjectposition = new MovingObjectPosition(entity);
			}
		}
		if (movingobjectposition != null && movingobjectposition.entity instanceof EntityPlayer && this.shooter != null && this.shooter instanceof EntityPlayer && !((EntityPlayer)this.shooter).getBukkitEntity().canSee(((EntityPlayer)movingobjectposition.entity).getBukkitEntity())) {
			movingobjectposition = null;
		}
		if (movingobjectposition != null) {
			if (movingobjectposition.type == MovingObjectPosition.EnumMovingObjectType.BLOCK && this.world.getType(movingobjectposition.a()).getBlock() == Blocks.PORTAL) {
				this.d(movingobjectposition.a());
			} else {
				this.a(movingobjectposition);
				if (this.dead) {
					CraftEventFactory.callProjectileHitEvent(this);
				}
			}
		}
		this.locX += this.motX;
		this.locY += this.motY;
		this.locZ += this.motZ;
		float f1 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
		this.yaw = (float)(MathHelper.b(this.motX, this.motZ) * 180.0 / 3.1415927410125732);
		this.pitch = (float)(MathHelper.b(this.motY, f1) * 180.0 / 3.1415927410125732);
		while (this.pitch - this.lastPitch < -180.0f) {
			this.lastPitch -= 360.0f;
		}
		while (this.pitch - this.lastPitch >= 180.0f) {
			this.lastPitch += 360.0f;
		}
		while (this.yaw - this.lastYaw < -180.0f) {
			this.lastYaw -= 360.0f;
		}
		while (this.yaw - this.lastYaw >= 180.0f) {
			this.lastYaw += 360.0f;
		}
		this.pitch = this.lastPitch + (this.pitch - this.lastPitch) * 0.2f;
		this.yaw = this.lastYaw + (this.yaw - this.lastYaw) * 0.2f;
		float f2 = 0.99f;
		float f3 = this.m();
		if (this.V()) {
			for (int j = 0; j < 4; ++j) {
				float f4 = 0.25f;
				this.world.addParticle(EnumParticle.WATER_BUBBLE, this.locX - this.motX * (double)f4, this.locY - this.motY * (double)f4, this.locZ - this.motZ * (double)f4, this.motX, this.motY, this.motZ);
			}
			f2 = 0.8f;
		}
		this.motX *= f2;
		this.motY *= f2;
		this.motZ *= f2;
		this.motY -= f3;
		this.setPosition(this.locX, this.locY, this.locZ);
	}

	protected float m() {
		return 0.03F;
	}

	protected abstract void a(MovingObjectPosition movingobjectposition);

	@Override
	public void b(NBTTagCompound nbttagcompound) {
		nbttagcompound.setShort("xTile", (short) this.blockX);
		nbttagcompound.setShort("yTile", (short) this.blockY);
		nbttagcompound.setShort("zTile", (short) this.blockZ);
		MinecraftKey minecraftkey = Block.REGISTRY.c(this.inBlockId);

		nbttagcompound.setString("inTile", minecraftkey == null ? "" : minecraftkey.toString());
		nbttagcompound.setByte("shake", (byte) this.shake);
		nbttagcompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
		if ((this.shooterName == null || this.shooterName.length() == 0) && this.shooter instanceof EntityHuman) {
			this.shooterName = this.shooter.getName();
		}

		nbttagcompound.setString("ownerName", this.shooterName == null ? "" : this.shooterName);
	}

	@Override
	public void a(NBTTagCompound nbttagcompound) {
		this.blockX = nbttagcompound.getShort("xTile");
		this.blockY = nbttagcompound.getShort("yTile");
		this.blockZ = nbttagcompound.getShort("zTile");
		if (nbttagcompound.hasKeyOfType("inTile", 8)) {
			this.inBlockId = Block.getByName(nbttagcompound.getString("inTile"));
		} else {
			this.inBlockId = Block.getById(nbttagcompound.getByte("inTile") & 255);
		}

		this.shake = nbttagcompound.getByte("shake") & 255;
		this.inGround = nbttagcompound.getByte("inGround") == 1;
		this.shooter = null;
		this.shooterName = nbttagcompound.getString("ownerName");
		if (this.shooterName != null && this.shooterName.length() == 0) {
			this.shooterName = null;
		}

		this.shooter = this.getShooter();
	}

	public EntityLiving getShooter() {
		if (this.shooter == null && this.shooterName != null && this.shooterName.length() > 0) {
			this.shooter = this.world.a(this.shooterName);
			if (this.shooter == null && this.world instanceof WorldServer) {
				try {
					Entity entity = ((WorldServer) this.world).getEntity(FastUUID.parseUUID(this.shooterName));

					if (entity instanceof EntityLiving) {
						this.shooter = (EntityLiving) entity;
					}
				} catch (Throwable throwable) {
					this.shooter = null;
				}
			}
		}

		return this.shooter;
	}
}
