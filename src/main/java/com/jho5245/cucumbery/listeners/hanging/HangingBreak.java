package com.jho5245.cucumbery.listeners.hanging;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.Method;

public class HangingBreak implements Listener
{
	@EventHandler
	public void onHangingBreak(HangingBreakEvent event)
	{
		Entity entity = event.getEntity();
		if (Method.configContainsLocation(entity.getLocation(), Cucumbery.config.getStringList("no-prevent-entity-explosion-worlds")))
			return;
		if (event.getCause() == RemoveCause.EXPLOSION)
		{
			EntityType type = entity.getType();
			if (type == EntityType.ITEM_FRAME)
			{
				if (Cucumbery.config.getBoolean("prevent-entity-explosion.item-frame"))
				{
					event.setCancelled(true);
				}
			}
			if (type == EntityType.PAINTING)
			{
				if (Cucumbery.config.getBoolean("prevent-entity-explosion.painting"))
				{
					event.setCancelled(true);
				}
			}
		}
	}
}