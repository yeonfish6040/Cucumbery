package com.jho5245.cucumbery.listeners.entity;

import com.jho5245.cucumbery.customeffect.CustomEffect;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectApplyEvent;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class EntityCustomEffectApply implements Listener
{
  @EventHandler
  public void onEntityCustomEffectApply(EntityCustomEffectApplyEvent event)
  {
    Entity entity = event.getEntity();
    CustomEffect customEffect = event.getCustomEffect();
    CustomEffectType customEffectType = customEffect.getEffectType();
    List<CustomEffectType> conflictEffects = customEffectType.getConflictEffects();

    if (customEffectType == CustomEffectType.RESURRECTION)
    {
      if (CustomEffectManager.hasEffect(entity, CustomEffectType.RESURRECTION_COOLDOWN))
      {
        event.setCancelled(true);
        return;
      }
      CustomEffectManager.addEffect(entity, new CustomEffect(CustomEffectType.RESURRECTION_COOLDOWN));
    }
    if (customEffectType == CustomEffectType.PARROTS_CHEER && !(entity instanceof AnimalTamer && entity instanceof Damageable))
    {
      event.setCancelled(true);
    }
  }
}
