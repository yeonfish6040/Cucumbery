package com.jho5245.cucumbery.listeners.entity;

import com.jho5245.cucumbery.customeffect.CustomEffect;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent.Action;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EntityPotionEffect implements Listener
{
  @EventHandler
  public void onEntityPotionEffect(EntityPotionEffectEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Entity entity = event.getEntity();
    Action action = event.getAction();
    Cause cause = event.getCause();
    PotionEffect oldEffect = event.getOldEffect(), newEffect = event.getNewEffect();
    PotionEffectType potionEffectType = event.getModifiedType();
    boolean override = event.isOverride();

//
//    MessageUtil.broadcastDebug("entity-", entity, ", action:" + action + ", cause:" + cause, ", type:", potionEffectType,
//            ComponentUtil.translate(", old:%s, new:%s, override:%s", oldEffect != null ? oldEffect : "null", newEffect != null ? newEffect : "null", override + ""));

    if (CustomEffectManager.hasEffect(entity, CustomEffectType.LEVITATION_RESISTACNE) && potionEffectType.equals(PotionEffectType.LEVITATION) && cause == Cause.ATTACK)
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(entity, CustomEffectType.LEVITATION_RESISTACNE);
      int amplifier = customEffect.getAmplifier() + 1;
      if (Math.random() * 10d < amplifier)
      {
        event.setCancelled(true);
      }
    }
    if (entity instanceof Player player && cause == Cause.MILK && CustomEffectManager.hasEffect(player, CustomEffectType.CHEESE_EXPERIMENT))
    {
      CustomEffectManager.removeEffect(player, CustomEffectType.CHEESE_EXPERIMENT);
      event.setCancelled(true);
    }
  }
}
