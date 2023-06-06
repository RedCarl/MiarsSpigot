package net.minecraft.server;

import java.util.List;

import gg.kazerspigot.knockback.KnockBackProfile;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.entity.Arrow;
// TacoSpigot end
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import ga.windpvp.windspigot.cache.Constants;
// CraftBukkit end
// TacoSpigot start
import net.techcable.tacospigot.event.entity.ArrowCollideEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

public class EntityArrow extends Entity implements IProjectile {

	private int d = -1;
	private int e = -1;
	private int f = -1;
	private Block g;
	private int h;
	public boolean inGround = false; // Spigot - private -> public
	public int fromPlayer;
	public int shake;
	public Entity shooter;
	private int ar;
	private int as;
	private double damage = 2.0D;
	public int knockbackStrength;

	// Spigot Start
	@Override
	public void inactiveTick() {
		if (this.inGround) {
			this.ar += 1; // Despawn counter. First int after shooter
		}
		super.inactiveTick();
	}
	// Spigot End

	public EntityArrow(World world) {
		super(world);
		this.j = 10.0D;
		this.setSize(0.5F, 0.5F);
	}

	public EntityArrow(World world, double d0, double d1, double d2) {
		super(world);
		this.j = 10.0D;
		this.setSize(0.5F, 0.5F);
		this.setPosition(d0, d1, d2);
	}

	public EntityArrow(World world, EntityLiving entityliving, EntityLiving entityliving1, float f, float f1) {
		super(world);
		this.j = 10.0D;
		this.shooter = entityliving;
		this.projectileSource = (LivingEntity) entityliving.getBukkitEntity(); // CraftBukkit
		if (entityliving instanceof EntityHuman) {
			this.fromPlayer = 1;
		}

		this.locY = entityliving.locY + entityliving.getHeadHeight() - 0.10000000149011612D;
		double d0 = entityliving1.locX - entityliving.locX;
		double d1 = entityliving1.getBoundingBox().b + entityliving1.length / 3.0F - this.locY;
		double d2 = entityliving1.locZ - entityliving.locZ;
		double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);

		if (d3 >= 1.0E-7D) {
			float f2 = (float) (MathHelper.b(d2, d0) * 180.0D / 3.1415927410125732D) - 90.0F;
			float f3 = (float) (-(MathHelper.b(d1, d3) * 180.0D / 3.1415927410125732D));
			double d4 = d0 / d3;
			double d5 = d2 / d3;

			this.setPositionRotation(entityliving.locX + d4, this.locY, entityliving.locZ + d5, f2, f3);
			float f4 = (float) (d3 * 0.20000000298023224D);

			this.shoot(d0, d1 + f4, d2, f, f1);
		}
	}

	public EntityArrow(World world, EntityLiving entityliving, float f) {
		super(world);
		this.j = 10.0D;
		this.shooter = entityliving;
		this.projectileSource = (LivingEntity) entityliving.getBukkitEntity(); // CraftBukkit
		if (entityliving instanceof EntityHuman) {
			this.fromPlayer = 1;
		}

		this.setSize(0.5F, 0.5F);
		this.setPositionRotation(entityliving.locX, entityliving.locY + entityliving.getHeadHeight(), entityliving.locZ,
				entityliving.yaw, entityliving.pitch);
		this.locX -= MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * 0.16F;
		this.locY -= 0.10000000149011612D;
		this.locZ -= MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * 0.16F;
		this.setPosition(this.locX, this.locY, this.locZ);
		this.motX = -MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F);
		this.motZ = MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F);
		this.motY = (-MathHelper.sin(this.pitch / 180.0F * 3.1415927F));
		this.shoot(this.motX, this.motY, this.motZ, f * 1.5F, 1.0F);
	}

	@Override
	protected void h() {
		this.datawatcher.a(16, Byte.valueOf((byte) 0));
	}

	@Override
	public void shoot(double d0, double d1, double d2, float f, float f1) {
		float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

		d0 /= f2;
		d1 /= f2;
		d2 /= f2;
		d0 += this.random.nextGaussian() * (this.random.nextBoolean() ? -1 : 1) * 0.007499999832361937D * f1;
		d1 += this.random.nextGaussian() * (this.random.nextBoolean() ? -1 : 1) * 0.007499999832361937D * f1;
		d2 += this.random.nextGaussian() * (this.random.nextBoolean() ? -1 : 1) * 0.007499999832361937D * f1;
		d0 *= f;
		d1 *= f;
		d2 *= f;
		this.motX = d0;
		this.motY = d1;
		this.motZ = d2;
		float f3 = MathHelper.sqrt(d0 * d0 + d2 * d2);

		this.lastYaw = this.yaw = (float) (MathHelper.b(d0, d2) * 180.0D / 3.1415927410125732D);
		this.lastPitch = this.pitch = (float) (MathHelper.b(d1, f3) * 180.0D / 3.1415927410125732D);
		this.ar = 0;
	}

	@Override
	public void t_() {
		BlockPosition blockposition;
		IBlockData iblockdata;
		Block block;
		super.t_();
		if (this.lastPitch == 0.0f && this.lastYaw == 0.0f) {
			float f = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
			this.lastYaw = this.yaw = (float)(MathHelper.b(this.motX, this.motZ) * 180.0 / 3.1415927410125732);
			this.lastPitch = this.pitch = (float)(MathHelper.b(this.motY, f) * 180.0 / 3.1415927410125732);
		}
		if ((block = (iblockdata = this.world.getType(blockposition = new BlockPosition(this.d, this.e, this.f))).getBlock()).getMaterial() != Material.AIR) {
			block.updateShape(this.world, blockposition);
			AxisAlignedBB axisalignedbb = block.a(this.world, blockposition, iblockdata);
			if (axisalignedbb != null && axisalignedbb.a(new Vec3D(this.locX, this.locY, this.locZ))) {
				this.inGround = true;
			}
		}
		if (this.shake > 0) {
			--this.shake;
		}
		if (this.inGround) {
			int i = block.toLegacyData(iblockdata);
			if (block == this.g && i == this.h) {
				++this.ar;
				if (this.ar >= this.world.spigotConfig.arrowDespawnRate) {
					this.die();
				}
			} else {
				this.inGround = false;
				this.motX *= this.random.nextFloat() * 0.2f;
				this.motY *= this.random.nextFloat() * 0.2f;
				this.motZ *= this.random.nextFloat() * 0.2f;
				this.ar = 0;
				this.as = 0;
			}
		} else {
			float f1;
			int j;
			++this.as;
			Vec3D vec3d = new Vec3D(this.locX, this.locY, this.locZ);
			Vec3D vec3d1 = new Vec3D(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
			MovingObjectPosition movingobjectposition = this.world.rayTrace(vec3d, vec3d1, false, true, false);
			vec3d = new Vec3D(this.locX, this.locY, this.locZ);
			vec3d1 = new Vec3D(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
			if (movingobjectposition != null) {
				vec3d1 = new Vec3D(movingobjectposition.pos.a, movingobjectposition.pos.b, movingobjectposition.pos.c);
			}
			Entity entity = null;
			List<Entity> list = this.world.getEntities(this, this.getBoundingBox().a(this.motX, this.motY, this.motZ).grow(1.0, 1.0, 1.0));
			double d0 = 0.0;
			for (j = 0; j < list.size(); ++j) {
				double d1;
				Entity entity1 = list.get(j);
				if (!entity1.ad() || entity1 == this.shooter && this.as < 5) {
					continue;
				}
				f1 = 0.3f;
				AxisAlignedBB axisalignedbb1 = entity1.getBoundingBox().grow(f1, f1, f1);
				MovingObjectPosition movingobjectposition1 = axisalignedbb1.a(vec3d, vec3d1);
				if (movingobjectposition1 == null || !((d1 = vec3d.distanceSquared(movingobjectposition1.pos)) < d0) && d0 != 0.0) {
					continue;
				}
				entity = entity1;
				d0 = d1;
			}
			if (entity != null) {
				movingobjectposition = new MovingObjectPosition(entity);
			}
			if (movingobjectposition != null && movingobjectposition.entity != null && movingobjectposition.entity instanceof EntityHuman) {
				EntityHuman entityhuman = (EntityHuman)movingobjectposition.entity;
				if (entityhuman.abilities.isInvulnerable || this.shooter instanceof EntityHuman && !((EntityHuman)this.shooter).a(entityhuman)) {
					movingobjectposition = null;
				}
			}
			if (movingobjectposition != null && movingobjectposition.entity instanceof EntityPlayer && this.shooter != null && this.shooter instanceof EntityPlayer && !((EntityPlayer)this.shooter).getBukkitEntity().canSee(((EntityPlayer)movingobjectposition.entity).getBukkitEntity())) {
				movingobjectposition = null;
			}
			if (movingobjectposition != null) {
				CraftEventFactory.callProjectileHitEvent(this);
				if (movingobjectposition.entity != null) {
					DamageSource damagesource;
					float f2 = MathHelper.sqrt(this.motX * this.motX + this.motY * this.motY + this.motZ * this.motZ);
					int k = MathHelper.f((double)f2 * this.damage);
					if (this.isCritical()) {
						k += this.random.nextInt(k / 2 + 2);
					}
					if (movingobjectposition.entity.damageEntity(damagesource = this.shooter == null ? DamageSource.arrow(this, this) : DamageSource.arrow(this, this.shooter), k)) {
						if (!(!this.isBurning() || movingobjectposition.entity instanceof EntityEnderman || movingobjectposition.entity instanceof EntityPlayer && this.shooter instanceof EntityPlayer && !this.world.pvpMode)) {
							EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), 5);
							Bukkit.getPluginManager().callEvent(combustEvent);
							if (!combustEvent.isCancelled()) {
								movingobjectposition.entity.setOnFire(combustEvent.getDuration());
							}
						}
						if (movingobjectposition.entity instanceof EntityLiving) {
							float f3;
							EntityLiving entityliving = (EntityLiving)movingobjectposition.entity;
							if (!this.world.isClientSide) {
								entityliving.o(entityliving.bv() + 1);
							}
							if (movingobjectposition.entity.getBukkitEntity().getType() == EntityType.PLAYER) {
								EntityPlayer hit = ((CraftPlayer)movingobjectposition.entity.getBukkitEntity()).getHandle();
								if (this.shooter != null && this.shooter != hit && this.shooter instanceof EntityPlayer) {
									((EntityPlayer)this.shooter).playerConnection.sendPacket(new PacketPlayOutGameStateChange(6, 0.0f));
								}
								KnockBackProfile kb2 = hit.getKnockBack();
								Vector v = new Vector(this.motX, this.motY, this.motZ).normalize();
								double velX = v.getX() / 1.6 * kb2.bowH.value;
								double velY = 0.36 * kb2.bowV.value;
								double velZ = v.getZ() / 1.6 * kb2.bowH.value;
								if (this.knockbackStrength > 0) {
									velX *= (double)this.knockbackStrength * (double)0.6f + 1.0;
									velY *= 1.0;
									velZ *= (double)this.knockbackStrength * (double)0.6f + 1.0;
								}
								PlayerVelocityEvent playerVelocityEvent = new PlayerVelocityEvent(hit.getBukkitEntity(), new Vector(velX, velY, velZ));
								Bukkit.getPluginManager().callEvent(playerVelocityEvent);
								if (!playerVelocityEvent.isCancelled()) {
									hit.playerConnection.sendPacket(new PacketPlayOutEntityVelocity(hit.getId(), velX, velY, velZ));
									hit.velocityChanged = false;
									hit.motX = velX;
									hit.motY = velY;
									hit.motZ = velZ;
								}
							} else if (this.knockbackStrength > 0 && (f3 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ)) > 0.0f) {
								movingobjectposition.entity.g(this.motX * (double)this.knockbackStrength * (double)0.6f / (double)f3, 0.1, this.motZ * (double)this.knockbackStrength * (double)0.6f / (double)f3);
							}
							if (this.shooter instanceof EntityLiving) {
								EnchantmentManager.a(entityliving, this.shooter);
								EnchantmentManager.b((EntityLiving)this.shooter, entityliving);
							}
							if (this.shooter != null && movingobjectposition.entity != this.shooter && movingobjectposition.entity instanceof EntityHuman && this.shooter instanceof EntityPlayer) {
								((EntityPlayer)this.shooter).playerConnection.sendPacket(new PacketPlayOutGameStateChange(6, 0.0f));
							}
						}
						this.makeSound("random.bowhit", 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));
						if (!(movingobjectposition.entity instanceof EntityEnderman)) {
							this.die();
						}
					} else {
						this.motX *= -0.1f;
						this.motY *= -0.1f;
						this.motZ *= -0.1f;
						this.yaw += 180.0f;
						this.lastYaw += 180.0f;
						this.as = 0;
					}
				} else {
					BlockPosition blockposition1 = movingobjectposition.a();
					this.d = blockposition1.getX();
					this.e = blockposition1.getY();
					this.f = blockposition1.getZ();
					IBlockData iblockdata1 = this.world.getType(blockposition1);
					this.g = iblockdata1.getBlock();
					this.h = this.g.toLegacyData(iblockdata1);
					this.motX = (float)(movingobjectposition.pos.a - this.locX);
					this.motY = (float)(movingobjectposition.pos.b - this.locY);
					this.motZ = (float)(movingobjectposition.pos.c - this.locZ);
					f1 = MathHelper.sqrt(this.motX * this.motX + this.motY * this.motY + this.motZ * this.motZ);
					this.locX -= this.motX / (double)f1 * (double)0.05f;
					this.locY -= this.motY / (double)f1 * (double)0.05f;
					this.locZ -= this.motZ / (double)f1 * (double)0.05f;
					this.makeSound("random.bowhit", 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));
					this.inGround = true;
					this.shake = 7;
					this.setCritical(false);
					if (this.g.getMaterial() != Material.AIR) {
						this.g.a(this.world, blockposition1, iblockdata1, this);
					}
				}
			}
			if (this.isCritical()) {
				for (j = 0; j < 4; ++j) {
					this.world.addParticle(EnumParticle.CRIT, this.locX + this.motX * (double)j / 4.0, this.locY + this.motY * (double)j / 4.0, this.locZ + this.motZ * (double)j / 4.0, -this.motX, -this.motY + 0.2, -this.motZ, new int[0]);
				}
			}
			this.locX += this.motX;
			this.locY += this.motY;
			this.locZ += this.motZ;
			float f2 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
			this.yaw = (float)(MathHelper.b(this.motX, this.motZ) * 180.0 / 3.1415927410125732);
			this.pitch = (float)(MathHelper.b(this.motY, f2) * 180.0 / 3.1415927410125732);
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
			float f4 = 0.99f;
			f1 = 0.05f;
			if (this.V()) {
				for (int l = 0; l < 4; ++l) {
					float f3 = 0.25f;
					this.world.addParticle(EnumParticle.WATER_BUBBLE, this.locX - this.motX * (double)f3, this.locY - this.motY * (double)f3, this.locZ - this.motZ * (double)f3, this.motX, this.motY, this.motZ, new int[0]);
				}
				f4 = 0.6f;
			}
			if (this.U()) {
				this.extinguish();
			}
			this.motX *= f4;
			this.motY *= f4;
			this.motZ *= f4;
			this.motY -= f1;
			this.setPosition(this.locX, this.locY, this.locZ);
			this.checkBlockCollisions();
		}
	}

	@Override
	public void b(NBTTagCompound nbttagcompound) {
		nbttagcompound.setShort("xTile", (short) this.d);
		nbttagcompound.setShort("yTile", (short) this.e);
		nbttagcompound.setShort("zTile", (short) this.f);
		nbttagcompound.setShort("life", (short) this.ar);
		MinecraftKey minecraftkey = Block.REGISTRY.c(this.g);

		nbttagcompound.setString("inTile", minecraftkey == null ? "" : minecraftkey.toString());
		nbttagcompound.setByte("inData", (byte) this.h);
		nbttagcompound.setByte("shake", (byte) this.shake);
		nbttagcompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
		nbttagcompound.setByte("pickup", (byte) this.fromPlayer);
		nbttagcompound.setDouble("damage", this.damage);
	}

	@Override
	public void a(NBTTagCompound nbttagcompound) {
		this.d = nbttagcompound.getShort("xTile");
		this.e = nbttagcompound.getShort("yTile");
		this.f = nbttagcompound.getShort("zTile");
		this.ar = nbttagcompound.getShort("life");
		if (nbttagcompound.hasKeyOfType("inTile", 8)) {
			this.g = Block.getByName(nbttagcompound.getString("inTile"));
		} else {
			this.g = Block.getById(nbttagcompound.getByte("inTile") & 255);
		}

		this.h = nbttagcompound.getByte("inData") & 255;
		this.shake = nbttagcompound.getByte("shake") & 255;
		this.inGround = nbttagcompound.getByte("inGround") == 1;
		if (nbttagcompound.hasKeyOfType("damage", 99)) {
			this.damage = nbttagcompound.getDouble("damage");
		}

		if (nbttagcompound.hasKeyOfType("pickup", 99)) {
			this.fromPlayer = nbttagcompound.getByte("pickup");
		} else if (nbttagcompound.hasKeyOfType("player", 99)) {
			this.fromPlayer = nbttagcompound.getBoolean("player") ? 1 : 0;
		}

	}

	@Override
	public void d(EntityHuman entityhuman) {
		if (!this.world.isClientSide && this.inGround && this.shake <= 0) {
			// CraftBukkit start
			ItemStack itemstack = new ItemStack(Items.ARROW);
			if (this.fromPlayer == 1 && entityhuman.inventory.canHold(itemstack) > 0) {
				EntityItem item = new EntityItem(this.world, this.locX, this.locY, this.locZ, itemstack);

				PlayerPickupItemEvent event = new PlayerPickupItemEvent(
						(org.bukkit.entity.Player) entityhuman.getBukkitEntity(),
						new org.bukkit.craftbukkit.entity.CraftItem(this.world.getServer(), this, item), 0);
				// event.setCancelled(!entityhuman.canPickUpLoot); TODO
				this.world.getServer().getPluginManager().callEvent(event);

				if (event.isCancelled()
						|| !((EntityPlayer) entityhuman).getBukkitEntity().canSee(this.getBukkitEntity())) {
					return;
				}
			}
			// CraftBukkit end
			boolean flag = this.fromPlayer == 1 || this.fromPlayer == 2 && entityhuman.abilities.canInstantlyBuild;

			if (this.fromPlayer == 1 && !entityhuman.inventory.pickup(new ItemStack(Items.ARROW, 1))) {
				flag = false;
			}

			if (flag) {
				this.makeSound("random.pop", 0.2F,
						((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				entityhuman.receive(this, 1);
				this.die();
			}

		}
	}

	@Override
	protected boolean s_() {
		return false;
	}

	public void b(double d0) {
		this.damage = d0;
	}

	public double j() {
		return this.damage;
	}

	public void setKnockbackStrength(int i) {
		this.knockbackStrength = i;
	}

	@Override
	public boolean aD() {
		return false;
	}

	@Override
	public float getHeadHeight() {
		return 0.0F;
	}

	public void setCritical(boolean flag) {
		byte b0 = this.datawatcher.getByte(16);

		if (flag) {
			this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 1)));
		} else {
			this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -2)));
		}

	}

	public boolean isCritical() {
		byte b0 = this.datawatcher.getByte(16);

		return (b0 & 1) != 0;
	}

	// CraftBukkit start
	public boolean isInGround() {
		return inGround;
	}
	// CraftBukkit end
}
