package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

public class EntityRegainHealth implements Listener
{
  @EventHandler
  public void onEntityRegainHealth(EntityRegainHealthEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Entity entity = event.getEntity();
    RegainReason reason = event.getRegainReason();
    if (reason == RegainReason.REGEN || reason == RegainReason.SATIATED)
    {
      if (CustomEffectManager.hasEffect(entity, CustomEffectType.NO_REGENERATION))
      {
        event.setCancelled(true);
      }
    }
  }
}
