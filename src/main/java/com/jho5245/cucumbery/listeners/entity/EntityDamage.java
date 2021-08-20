package com.jho5245.cucumbery.listeners.entity;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.data.Constant;
import de.tr7zw.changeme.nbtapi.NBTList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

public class EntityDamage implements Listener
{
	@EventHandler public void onEntityDamage(EntityDamageEvent event)
	{
		if (event.isCancelled())
			return;
		this.cancelEntityDamage(event);
		this.cancelLavaBurnItem(event);
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
		if (extraTags != null && extraTags.size() > 0)
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
