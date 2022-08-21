package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.destroystokyo.paper.event.entity.EntityJumpEvent;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Sittable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityJump implements Listener
{
  @EventHandler
  public void onEntityJump(EntityJumpEvent event)
  {
    Entity entity = event.getEntity();
    if (CustomEffectManager.hasEffect(entity, CustomEffectType.CONFUSION))
    {
      event.setCancelled(true);
      if (entity instanceof Sittable sittable)
      {
        sittable.setSitting(!sittable.isSitting());
      }
      return;
    }
    if (CustomEffectManager.hasEffect(entity, CustomEffectType.CURSE_OF_JUMPING))
    {
      event.setCancelled(true);
      return;
    }
  }
}
