package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.children.group.EntityCustomEffect;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

public class EntityDismount implements Listener
{
  @EventHandler
  public void onEntityDismount(EntityDismountEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Entity entity = event.getEntity(), dismounted = event.getDismounted();
    if (!CustomEffectManager.hasEffect(entity, CustomEffectType.CONTINUAL_RIDING_EXEMPT) && CustomEffectManager.getEffectNullable(entity, CustomEffectType.CONTINUAL_RIDING) instanceof EntityCustomEffect entityCustomEffect)
    {
      Entity effectEntity = entityCustomEffect.getEntity();
      if (effectEntity == dismounted && dismounted.isValid() && !dismounted.isDead())
      {
        event.setCancelled(true);
      }
    }
  }
}
