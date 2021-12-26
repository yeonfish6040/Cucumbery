package com.jho5245.cucumbery.listeners.entity.customeffect;

import com.jho5245.cucumbery.customeffect.CustomEffect;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectPreApplyEvent;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityCustomEffectPreApply implements Listener
{
  @EventHandler
  public void onEntityCustomEffectPreApply(EntityCustomEffectPreApplyEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Entity entity = event.getEntity();
    CustomEffect customEffect = event.getCustomEffect();
    CustomEffectType customEffectType = customEffect.getEffectType();
    if (customEffectType == CustomEffectType.RESURRECTION && CustomEffectManager.hasEffect(entity, CustomEffectType.RESURRECTION_COOLDOWN))
    {
      event.setCancelled(true);
      return;
    }
    if (customEffectType == CustomEffectType.PARROTS_CHEER && !(entity instanceof AnimalTamer && entity instanceof Damageable))
    {
      event.setCancelled(true);
    }

    if (customEffectType == CustomEffectType.CUCUMBERY_UPDATER && !(entity instanceof Player))
    {
      event.setCancelled(true);
    }
  }
}
