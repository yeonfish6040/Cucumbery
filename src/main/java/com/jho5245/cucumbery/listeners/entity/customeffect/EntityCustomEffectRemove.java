package com.jho5245.cucumbery.listeners.entity.customeffect;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.children.group.AttributeCustomEffect;
import com.jho5245.cucumbery.custom.customeffect.scheduler.CustomEffectScheduler;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class EntityCustomEffectRemove implements Listener
{
  @EventHandler
  public void onEntityCustomEffectRemove(EntityCustomEffectRemoveEvent event)
  {
    Entity entity = event.getEntity();
    UUID uuid = entity.getUniqueId();
    CustomEffect customEffect = event.getCustomEffect();
    CustomEffectType customEffectType = customEffect.getType();
    if (entity instanceof Player player && customEffect.getDisplayType() == DisplayType.PLAYER_LIST && CustomEffectManager.getEffects(entity, DisplayType.PLAYER_LIST).isEmpty())
    {
      player.sendPlayerListFooter(Component.empty());
    }
    if (customEffectType == CustomEffectType.MUTE)
    {
      MessageUtil.sendMessage(entity, Prefix.INFO, "채팅 금지가 해제되었습니다");
    }

    if (customEffectType == CustomEffectType.SPREAD)
    {
      CustomEffectManager.removeEffect(entity, CustomEffectType.VAR_DETOXICATE);
      CustomEffectManager.removeEffect(entity, CustomEffectType.VAR_PODAGRA_ACTIVATED);
      CustomEffectManager.removeEffect(entity, CustomEffectType.VAR_PODAGRA);
      CustomEffectManager.removeEffect(entity, CustomEffectType.VAR_PNEUMONIA);
      CustomEffectManager.removeEffect(entity, CustomEffectType.VAR_STOMACHACHE);
      if (CustomEffectScheduler.spreadTimerTask.containsKey(uuid))
      {
        BukkitTask bukkitTask = CustomEffectScheduler.spreadTimerTask.get(uuid);
        bukkitTask.cancel();
        CustomEffectScheduler.spreadTimerTask.remove(uuid);
        CustomEffectScheduler.spreadTimerSet.remove(uuid);
      }
    }

    if (customEffectType == CustomEffectType.VAR_PODAGRA)
    {
      CustomEffectManager.removeEffect(entity, CustomEffectType.VAR_PODAGRA_ACTIVATED);
    }
    if (customEffectType == CustomEffectType.VAR_DETOXICATE)
    {
      CustomEffectManager.removeEffect(entity, CustomEffectType.VAR_DETOXICATE_ACTIVATED);
    }
    if (customEffectType == CustomEffectType.FANCY_SPOTLIGHT)
    {
      CustomEffectManager.removeEffect(entity, CustomEffectType.FANCY_SPOTLIGHT_ACTIVATED);
    }

    if (customEffectType == CustomEffectType.BREAD_KIMOCHI)
    {
      CustomEffectManager.removeEffect(entity, CustomEffectType.BREAD_KIMOCHI_SECONDARY_EFFECT);
    }

    if (entity instanceof Attributable attributable && customEffect instanceof AttributeCustomEffect attributeCustomEffect)
    {
      UUID effectUniqueId = attributeCustomEffect.getUniqueId();
      Attribute attribute = attributeCustomEffect.getAttribute();
      AttributeInstance attributeInstance = attributable.getAttribute(attribute);
      if (attributeInstance != null)
      {
        AttributeModifier attributeModifier = null;
        for (AttributeModifier modifier : attributeInstance.getModifiers())
        {
          if (modifier.getUniqueId().equals(effectUniqueId))
          {
            attributeModifier = modifier;
            break;
          }
        }
        if (attributeModifier != null)
        {
          attributeInstance.removeModifier(attributeModifier);
        }
        // update max health
        if (attribute == Attribute.GENERIC_MAX_HEALTH && entity instanceof Damageable damageable)
        {
          double maxHealth = attributeInstance.getValue();
          if (damageable.getHealth() > maxHealth)
          {
            damageable.setHealth(maxHealth);
          }
        }
      }
    }

    if (!(entity instanceof Player) && (customEffectType == CustomEffectType.DISAPPEAR || customEffectType == CustomEffectType.DAMAGE_INDICATOR))
    {
      entity.remove();
    }

    if (customEffectType == CustomEffectType.FREEZING)
    {
      entity.lockFreezeTicks(false);
    }
  }
}
