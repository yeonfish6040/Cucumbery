package com.jho5245.cucumbery.listeners.entity;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityRemoveFromWorld implements Listener
{
  @EventHandler
  public void onEntityRemoveFromWorld(EntityRemoveFromWorldEvent event)
  {
    Entity entity = event.getEntity();
    if (!(entity instanceof Player))
    {
      CustomEffectManager.clearEffects(entity);
    }
  }
}
