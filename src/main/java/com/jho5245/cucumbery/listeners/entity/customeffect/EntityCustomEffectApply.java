package com.jho5245.cucumbery.listeners.entity.customeffect;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.customeffect.CustomEffect;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.customeffect.children.group.AttributeCustomEffect;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectApplyEvent;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.Updater;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityCustomEffectApply implements Listener
{
  @EventHandler
  public void onEntityCustomEffectApply(EntityCustomEffectApplyEvent event)
  {
    Entity entity = event.getEntity();
    CustomEffect customEffect = event.getCustomEffect();
    int duration = customEffect.getDuration();
    int amplifier = customEffect.getAmplifier();
    CustomEffectType customEffectType = customEffect.getType();
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
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
              Updater.defaultUpdater.updateLatest(amplifier == 1), 2L);
    }

    if (customEffectType == CustomEffectType.HEROS_ECHO)
    {
      if ((entity instanceof Player p && p.getGameMode() != GameMode.SPECTATOR) || (entity instanceof Tameable tameable && tameable.getOwner() instanceof Player))
      {
        List<Entity> appliedEntities = new ArrayList<>();
        List<Entity> nearbyEntities = entity.getNearbyEntities(100, 100, 100);
        for (Entity e : nearbyEntities)
        {
          if ((e instanceof Player player && player.getGameMode() != GameMode.SPECTATOR) ||
                  (e instanceof Tameable tameable && tameable.getOwner() instanceof Player))
          {
            CustomEffectManager.addEffect(e, CustomEffectType.HEROS_ECHO_OTHERS);
            appliedEntities.add(e);
          }
        }
        if (!appliedEntities.isEmpty())
        {
          MessageUtil.sendMessage(entity, Prefix.INFO_CUSTOM_EFFECT, "%s 능력 발동(100 블록 이내의 모든 플레이어와 반려 동물) - %s에게 %s 효과를 적용했습니다", customEffectType, appliedEntities, customEffect);
          MessageUtil.sendMessage(appliedEntities, Prefix.INFO_CUSTOM_EFFECT, "%s 능력 발동 - %s(으)로부터 %s 효과를 적용받았습니다", customEffectType, entity, customEffect);
        }
      }
    }

    if (entity instanceof Attributable attributable && customEffect instanceof AttributeCustomEffect attributeCustomEffect)
    {
      UUID uuid = attributeCustomEffect.getUniqueId();
      Attribute attribute = attributeCustomEffect.getAttribute();
      AttributeInstance attributeInstance = attributable.getAttribute(attribute);
      if (attributeInstance != null)
      {
        attributeInstance.addModifier(new AttributeModifier(uuid, "cucumbery-" + customEffectType.translationKey(), (amplifier + 1) * attributeCustomEffect.getMultiplier(), attributeCustomEffect.getOperation()));
      }
    }
  }
}


















