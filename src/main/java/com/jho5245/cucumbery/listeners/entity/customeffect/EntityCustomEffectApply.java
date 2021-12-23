package com.jho5245.cucumbery.listeners.entity.customeffect;

import com.jho5245.cucumbery.customeffect.CustomEffect;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectApplyEvent;
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
    for (CustomEffectType conflictEffect : conflictEffects)
    {
      CustomEffectManager.removeEffect(entity, conflictEffect);
    }

    if (customEffectType == CustomEffectType.RESURRECTION)
    {
      CustomEffectManager.addEffect(entity, new CustomEffect(CustomEffectType.RESURRECTION_COOLDOWN));
    }

    if (customEffectType == CustomEffectType.TROLL_INVENTORY_PROPERTY && !CustomEffectManager.hasEffect(entity, CustomEffectType.TROLL_INVENTORY_PROPERTY_MIN))
    {
      CustomEffectManager.addEffect(entity, new CustomEffect(CustomEffectType.TROLL_INVENTORY_PROPERTY_MIN, customEffect.getDuration(), 0));
    }
  }
}
