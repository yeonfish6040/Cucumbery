package com.jho5245.cucumbery.listeners.entity.customeffect;

import com.jho5245.cucumbery.customeffect.CustomEffect;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectApplyEvent;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.Updater;
import com.jho5245.cucumbery.util.storage.data.Prefix;
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
    int duration = customEffect.getDuration();
    int amplifier = customEffect.getAmplifier();
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

    if (customEffectType == CustomEffectType.MUTE)
    {
      MessageUtil.sendMessage(entity, Prefix.INFO, duration != -1 ? "%s 동안 채팅 금지 당하셨습니다" : "채팅 금지 당하셨습니다", Method.timeFormatMilli(duration * 50L, false, 0));
    }

    if (customEffectType == CustomEffectType.CUCUMBERY_UPDATER)
    {
      CustomEffectManager.removeEffect(entity, CustomEffectType.CUCUMBERY_UPDATER);
      Updater.defaultUpdater.updateLatest(amplifier == 1);
    }
  }
}
