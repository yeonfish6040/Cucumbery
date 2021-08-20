package com.jho5245.cucumbery.listeners.entity;

import com.jho5245.cucumbery.deathmessages.DeathManager;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeath implements Listener
{
  @EventHandler
  public void onEntityDeath(EntityDeathEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }

    LivingEntity entity = event.getEntity();
    if (entity.getScoreboardTags().contains("invincible"))
    {
      event.setCancelled(true);
    }


    if (Variable.deathMessages.getBoolean("death-messages.enable"))
    {
      DeathManager.what(event);
    }
  }
}
