package com.jho5245.cucumbery.listeners.entity.damage;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.combat.CombatManager;
import com.jho5245.cucumbery.customeffect.CustomEffect;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.data.Constant;
import de.tr7zw.changeme.nbtapi.NBTList;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.Nullable;

public class EntityDamage implements Listener
{
	@EventHandler public void onEntityDamage(EntityDamageEvent event)
	{
		if (event.isCancelled())
			return;
		Entity entity = event.getEntity();
		if (entity instanceof Player player)
		{
			if (UserData.SPECTATOR_MODE.getBoolean(player))
			{
				event.setCancelled(true);
				return;
			}
		}
		this.cancelEntityDamage(event);
		this.cancelLavaBurnItem(event);
		this.customEffect(event);
	}

	private void customEffect(EntityDamageEvent event)
	{
		double damage = event.getDamage();
		double damageMultiplier = 1d, finalDamageMultiplier = 1d;
		Entity entity = event.getEntity();
		@Nullable Object damager = CombatManager.getDamager(event);
		if (CustomEffectManager.hasEffect(entity, CustomEffectType.INVINCIBLE) || CustomEffectManager.hasEffect(entity, CustomEffectType.RESURRECTION_INVINCIBLE))
		{
			event.setCancelled(true);
			entity.setFireTicks(-20);
			return;
		}
		if (CustomEffectManager.hasEffect(entity, CustomEffectType.PARROTS_CHEER))
		{
			damageMultiplier -= 0.45d;
		}
		DamageCause damageCause = event.getCause();
		switch (damageCause)
		{
			case HOT_FLOOR ->{
				if (CustomEffectManager.hasEffect(entity, CustomEffectType.FROST_WALKER))
				{
					event.setCancelled(true);
					return;
				}
			}
			case ENTITY_ATTACK, ENTITY_SWEEP_ATTACK, CUSTOM -> {
				if (damager instanceof Entity damagerEntity)
				{
					if (CustomEffectManager.hasEffect(damagerEntity, CustomEffectType.SHARPNESS))
					{
						CustomEffect customEffectSharpness = CustomEffectManager.getEffect(damagerEntity, CustomEffectType.SHARPNESS);
						int amplifier = customEffectSharpness.getAmplifier();
						damage += amplifier + 1.5;
					}
					if (CustomEffectManager.hasEffect(damagerEntity, CustomEffectType.SMITE) && (entity instanceof Zombie || entity instanceof ZombieHorse || entity instanceof AbstractSkeleton || entity instanceof Wither))
					{
						CustomEffect customEffectSmite = CustomEffectManager.getEffect(damagerEntity, CustomEffectType.SMITE);
						int amplifier = customEffectSmite.getAmplifier();
						damage += (amplifier + 1) * 2.5;
					}
					if (CustomEffectManager.hasEffect(damagerEntity, CustomEffectType.BANE_OF_ARTHROPODS) && (entity instanceof Spider || entity instanceof Silverfish || entity instanceof Endermite))
					{
						CustomEffect customEffectBaneOfArthropods = CustomEffectManager.getEffect(damagerEntity, CustomEffectType.BANE_OF_ARTHROPODS);
						int amplifier = customEffectBaneOfArthropods.getAmplifier();
						damage += (amplifier + 1) * 2.5;
						((Monster) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (20 + Math.random() * (amplifier + 1) * 5), 3));
					}
					if (CustomEffectManager.hasEffect(damagerEntity, CustomEffectType.BLESS_OF_SANS))
					{
						CustomEffect customEffectBlessOfSans = CustomEffectManager.getEffect(damagerEntity, CustomEffectType.BLESS_OF_SANS);
						int amplifier = customEffectBlessOfSans.getAmplifier();
						damageMultiplier += (amplifier + 1) * 0.1;
					}
					if (CustomEffectManager.hasEffect(damagerEntity, CustomEffectType.PARROTS_CHEER))
					{
						damageMultiplier += 0.1d;
					}
				}
			}
			case FALL -> {
				if (!(event instanceof EntityDamageByEntityEvent damageByEntityEvent) || !(damageByEntityEvent.getDamager() instanceof EnderPearl))
				{
					if (CustomEffectManager.hasEffect(entity, CustomEffectType.FEATHER_FALLING))
					{
						CustomEffect customEffectFeatherFalling = CustomEffectManager.getEffect(entity, CustomEffectType.FEATHER_FALLING);
						double fallDistance = entity.getFallDistance();
						int amplifier = customEffectFeatherFalling.getAmplifier();
						if (fallDistance < (amplifier + 1) * 5 + 3.5d)
						{
							event.setCancelled(true);
							return;
						}
						damageMultiplier -= (amplifier + 1) * 0.08;
					}
				}
			}
			case PROJECTILE -> {
				if (event instanceof EntityDamageByEntityEvent damageByEntityEvent)
				{
					Entity damagerEntity = damageByEntityEvent.getDamager();
					if (damagerEntity instanceof Projectile projectile)
					{
						ProjectileSource projectileSource = projectile.getShooter();
						if (projectileSource instanceof Entity sourceEntity)
						{
							if (CustomEffectManager.hasEffect(sourceEntity, CustomEffectType.PARROTS_CHEER))
							{
								damageMultiplier += 0.1d;
							}
						}
					}
				}
			}
		}
		finalDamageMultiplier *= damageMultiplier;
		if (finalDamageMultiplier != 1d || damage != event.getDamage())
		{
			event.setDamage(damage * finalDamageMultiplier);
		}
	}

	private void cancelEntityDamage(EntityDamageEvent event)
	{
		Entity entity = event.getEntity();
		if (Method.configContainsLocation(entity.getLocation(), Cucumbery.config.getStringList("no-prevent-entity-explosion-worlds")))
			return;
		if (event.getCause() == DamageCause.ENTITY_EXPLOSION)
		{
			EntityType type = entity.getType();
			if (type == EntityType.ARMOR_STAND)
			{
				if (Cucumbery.config.getBoolean("prevent-entity-explosion.armor-stand"))
				{
					event.setCancelled(true);
				}
			}
		}
	}

	private void cancelLavaBurnItem(EntityDamageEvent event)
	{
		EntityType type = event.getEntityType();
		if (type != EntityType.DROPPED_ITEM)
			return;
		Item itemEntity = (Item) event.getEntity();
		ItemStack item = itemEntity.getItemStack();
		boolean affectedByPlugin = false;
		NBTList<String> extraTags = NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.EXTRA_TAGS_KEY);
		if (extraTags != null && !extraTags.isEmpty())
		{
			boolean noFire = false;
			if (NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.INVULNERABLE.toString()))
			{
				event.setCancelled(true);
				affectedByPlugin = true;
				itemEntity.setInvulnerable(true);
			}
			DamageCause damageCause = event.getCause();
			if ((damageCause == DamageCause.ENTITY_EXPLOSION || damageCause == DamageCause.BLOCK_EXPLOSION) && NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.INVULNERABLE_EXPLOSION.toString()))
			{
				affectedByPlugin = true;
			}

			if (damageCause == DamageCause.ENTITY_EXPLOSION && NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.INVULNERABLE_EXPLOSION_ENTITY.toString()))
			{
				affectedByPlugin = true;
			}

			if (damageCause == DamageCause.BLOCK_EXPLOSION && NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.INVULNERABLE_EXPLOSION_BLOCK.toString()))
			{
				affectedByPlugin = true;
			}

			if ((damageCause == DamageCause.LAVA || damageCause == DamageCause.FIRE || damageCause == DamageCause.FIRE_TICK) && NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.INVULNERABLE_FIRES.toString()))
			{
				noFire = true;
				affectedByPlugin = true;
			}

			if (damageCause == DamageCause.LAVA && NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.INVULNERABLE_FIRES_LAVA.toString()))
			{
				affectedByPlugin = true;
			}
			if (damageCause == DamageCause.FIRE && NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.INVULNERABLE_FIRES_FIRE.toString()))
			{
				noFire = true;
				affectedByPlugin = true;
			}
			if (damageCause == DamageCause.FIRE_TICK && NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.INVULNERABLE_FIRES_FIRE_TICK.toString()))
			{
				noFire = true;
				affectedByPlugin = true;
			}
			if (damageCause == DamageCause.CONTACT && NBTAPI.arrayContainsValue(extraTags, Constant.ExtraTag.INVULNERABLE_CONTACT.toString()))
			{
				affectedByPlugin = true;
			}

//			MessageUtil.broadcastDebug(damageCause.toString() + ", " + event.getDamage() + ", " + itemEntity.getThrower());

			if (affectedByPlugin)
			{
				event.setCancelled(true);
				if (noFire)
				itemEntity.setFireTicks(-20);
				itemEntity.setInvulnerable(true);
				Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> itemEntity.setInvulnerable(false), 0L);
			}
		}
	}
}
