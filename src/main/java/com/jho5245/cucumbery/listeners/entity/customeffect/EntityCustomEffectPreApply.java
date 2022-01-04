package com.jho5245.cucumbery.listeners.entity.customeffect;

import com.jho5245.cucumbery.customeffect.CustomEffect;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectPreApplyEvent;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collections;

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
      MessageUtil.sendWarn(entity, ComponentUtil.translate("%s 효과가 적용중이여서 %s 효과를 적용받지 못했습니다.",
              CustomEffectManager.getDisplay(Collections.singletonList(
                      CustomEffectManager.getEffect(entity, CustomEffectType.RESURRECTION_COOLDOWN)), true),
                      customEffect));
      return;
    }
    if (customEffectType == CustomEffectType.PARROTS_CHEER && !(entity instanceof AnimalTamer && entity instanceof Damageable))
    {
      event.setCancelled(true);
    }
    if (!(entity instanceof Player))
    {
      switch (customEffectType)
      {
        case CUCUMBERY_UPDATER, SERVER_RADIO_LISTENING, COOLDOWN_CHAT, COOLDOWN_ITEM_MEGAPHONE, DARKNESS_TERROR, DARKNESS_TERROR_RESISTANCE, MUNDANE,
                UNCRAFTABLE, BUFF_FREEZE, DEBUG_WATCHER, CHEESE_EXPERIMENT, CURSE_OF_BEANS, CURSE_OF_CONSUMPTION, CURSE_OF_INVENTORY, CURSE_OF_MUSHROOM, WHAT_TO_DO
                , ELYTRA_BOOSTER, MUTE, THICK-> event.setCancelled(true);
      }
    }
  }
}
