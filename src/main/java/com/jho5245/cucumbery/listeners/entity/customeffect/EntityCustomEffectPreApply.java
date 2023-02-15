package com.jho5245.cucumbery.listeners.entity.customeffect;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.children.group.AttributeCustomEffect;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectPreApplyEvent;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
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
    CustomEffectType customEffectType = customEffect.getType();
    if (customEffectType == CustomEffectType.RESURRECTION && CustomEffectManager.hasEffect(entity, CustomEffectType.RESURRECTION_COOLDOWN))
    {
      event.setCancelled(true);
      MessageUtil.sendWarn(entity, "%s 효과가 적용중이여서 %s 효과를 적용받지 못했습니다",
              CustomEffectManager.getDisplay(entity, Collections.singletonList(
                      CustomEffectManager.getEffect(entity, CustomEffectType.RESURRECTION_COOLDOWN)), true),
              customEffect);
      return;
    }

    if (customEffectType == CustomEffectType.FLY_NOT_ENABLED && !(CustomEffectManager.hasEffect(entity, CustomEffectType.FLY) || CustomEffectManager.hasEffect(entity, CustomEffectType.FLY_REMOVE_ON_QUIT)))
    {
      event.setCancelled(true);
      return;
    }

    if (customEffectType == CustomEffectType.FLY_NOT_ENABLED && !CustomEffectManager.hasEffect(entity, CustomEffectType.FLY_NOT_ENABLED) &&
            entity instanceof Player player && player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR &&
            (CustomEffectManager.hasEffect(entity, CustomEffectType.FLY) || CustomEffectManager.hasEffect(entity, CustomEffectType.FLY_REMOVE_ON_QUIT)))
    {
      CustomEffect effect = CustomEffectManager.hasEffect(entity, CustomEffectType.FLY) ? CustomEffectManager.getEffect(entity, CustomEffectType.FLY) : CustomEffectManager.getEffect(entity, CustomEffectType.FLY_REMOVE_ON_QUIT);
      MessageUtil.sendWarn(player,"비행 모드 사용이 불가능한 지역에 진입하여 %s 효과가 비활성화됩니다!", effect);
      return;
    }

    if (customEffectType == CustomEffectType.PARROTS_CHEER && !(entity instanceof AnimalTamer && entity instanceof Damageable))
    {
      event.setCancelled(true);
      return;
    }
    if (customEffectType == CustomEffectType.HEROS_ECHO || customEffectType == CustomEffectType.HEROS_ECHO_OTHERS)
    {
      if (!((entity instanceof Player player && player.getGameMode() != GameMode.SPECTATOR) || (entity instanceof Tameable tameable && tameable.getOwner() instanceof Player)))
      {
        event.setCancelled(true);
        return;
      }
    }

    if (CustomEffectManager.hasEffect(entity, CustomEffectType.COMBAT_MODE_MELEE_COOLDOWN) && customEffectType == CustomEffectType.COMBAT_MODE_RANGED)
    {
      event.setCancelled(true);
      return;
    }

    if (CustomEffectManager.hasEffect(entity, CustomEffectType.COMBAT_MODE_RANGED_COOLDOWN) && customEffectType == CustomEffectType.COMBAT_MODE_MELEE)
    {
      event.setCancelled(true);
      return;
    }

    if (customEffect instanceof AttributeCustomEffect)
    {
      if (!(entity instanceof Attributable attributable))
      {
        event.setCancelled(true);
        return;
      }
      AttributeInstance attributeInstance = attributable.getAttribute(Attribute.GENERIC_MAX_HEALTH);
      if (attributeInstance == null)
      {
        event.setCancelled(true);
      }
    }

    if (customEffectType == CustomEffectType.PVP_MODE_ENABLED && CustomEffectManager.hasEffect(entity, CustomEffectType.PVP_MODE_COOLDOWN))
    {
      event.setCancelled(true);
    }

    if (customEffectType == CustomEffectType.COMBO_STACK && CustomEffectManager.hasEffect(entity, CustomEffectType.COMBO_DELAY))
    {
      event.setCancelled(true);
    }

    if (customEffectType == CustomEffectType.COMBO_EXPERIENCE && !(entity instanceof ExperienceOrb))
    {
      event.setCancelled(true);
    }
  }
}



















