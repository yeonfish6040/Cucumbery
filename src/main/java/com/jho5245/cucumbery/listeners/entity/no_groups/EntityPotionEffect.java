package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent.Action;
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause;
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
    PotionEffectType effectType = event.getModifiedType();

    if (CustomEffectManager.hasEffect(entity, CustomEffectType.LEVITATION_RESISTANCE) && effectType.equals(PotionEffectType.LEVITATION) && cause == Cause.ATTACK)
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(entity, CustomEffectType.LEVITATION_RESISTANCE);
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

    if (action == Action.ADDED &&
            (effectType.equals(PotionEffectType.POISON) || effectType.equals(PotionEffectType.CONFUSION) || effectType.equals(PotionEffectType.BLINDNESS) || effectType.equals(PotionEffectType.UNLUCK)) &&
            CustomEffectManager.hasEffect(entity, CustomEffectType.VAR_DETOXICATE) && entity instanceof LivingEntity livingEntity)
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(livingEntity, CustomEffectType.VAR_DETOXICATE);
      int amplifier = customEffect.getAmplifier();
      boolean doubleDecreased = Math.random() < (amplifier + 1), tripledDecreased = Math.random() < (amplifier + 1) * 0.1, allRemoved = doubleDecreased && tripledDecreased;
      if (allRemoved)
      {
        livingEntity.removePotionEffect(PotionEffectType.POISON);
        livingEntity.removePotionEffect(PotionEffectType.CONFUSION);
        livingEntity.removePotionEffect(PotionEffectType.BLINDNESS);
        livingEntity.removePotionEffect(PotionEffectType.UNLUCK);
        MessageUtil.sendMessage(livingEntity, Prefix.INFO_CUSTOM_EFFECT, "와 샌즈! %s의 효과로 인해 디버프가 제거되었습니다!", customEffect);
      }
      event.setCancelled(true);
    }
  }
}
