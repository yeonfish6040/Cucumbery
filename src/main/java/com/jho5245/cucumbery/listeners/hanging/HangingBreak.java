package com.jho5245.cucumbery.listeners.hanging;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.no_groups.Method;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hanging;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;

public class HangingBreak implements Listener
{
  @EventHandler
  public void onHangingBreak(HangingBreakEvent event)
  {
    Hanging hanging = event.getEntity();
    if (CustomEffectManager.hasEffect(hanging, CustomEffectType.INVINCIBLE))
    {
      event.setCancelled(true);
      return;
    }
		if (Method.configContainsLocation(hanging.getLocation(), Cucumbery.config.getStringList("no-prevent-entity-explosion-worlds")))
		{
			return;
		}
    if (event.getCause() == RemoveCause.EXPLOSION)
    {
      EntityType type = hanging.getType();
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