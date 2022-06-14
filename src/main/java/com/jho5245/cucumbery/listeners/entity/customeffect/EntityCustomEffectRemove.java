package com.jho5245.cucumbery.listeners.entity.customeffect;

import com.jho5245.cucumbery.commands.reinforce.CommandReinforce;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.children.group.AttributeCustomEffect;
import com.jho5245.cucumbery.custom.customeffect.children.group.LocationCustomEffect;
import com.jho5245.cucumbery.custom.customeffect.children.group.LocationVelocityCustomEffect;
import com.jho5245.cucumbery.custom.customeffect.children.group.PlayerCustomEffect;
import com.jho5245.cucumbery.custom.customeffect.scheduler.CustomEffectScheduler;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent.RemoveReason;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import net.kyori.adventure.text.Component;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
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
    RemoveReason removeReason = event.getReason();
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
      if (entity.isValid())
      {
        entity.remove();
      }
    }

    if (customEffectType == CustomEffectType.FREEZING)
    {
      entity.lockFreezeTicks(false);
    }

    if (customEffectType == CustomEffectType.NOTIFY_NO_TRADE_ITEM_DROP)
    {
      switch (removeReason)
      {
        case DEATH, TIME_OUT -> MessageUtil.info(entity, "아이템 제거가 취소되었습니다");
      }
    }

    if (customEffectType == CustomEffectType.POSITION_MEMORIZE && removeReason == RemoveReason.PLUGIN)
    {
      if (customEffect instanceof LocationCustomEffect locationCustomEffect)
      {
        entity.teleport(locationCustomEffect.getLocation());
        MessageUtil.info(entity, "%s 효과의 능력을 사용하여 원래 있던 위치로 되돌아갔습니다", customEffect);
      }
    }

    if (customEffectType == CustomEffectType.DORMAMMU && removeReason == RemoveReason.TIME_OUT)
    {
      if (customEffect instanceof LocationVelocityCustomEffect locationVelocityCustomEffect)
      {
        entity.teleport(locationVelocityCustomEffect.getLocation());
        entity.setVelocity(locationVelocityCustomEffect.getVelocity());
      }
    }

    if (customEffectType == CustomEffectType.COMBO_EXPERIENCE && entity instanceof ExperienceOrb)
    {
      if (entity.isValid())
      {
        entity.remove();
      }
    }

    if (customEffectType == CustomEffectType.COMBO)
    {
      CustomEffectManager.removeEffect(entity, CustomEffectType.COMBO_STACK);
      entity.getWorld().getEntities().forEach(e ->
      {
        if (e instanceof ExperienceOrb && CustomEffectManager.hasEffect(e, CustomEffectType.COMBO_EXPERIENCE))
        {
          CustomEffect effect = CustomEffectManager.getEffect(e, CustomEffectType.COMBO_EXPERIENCE);
          if (effect instanceof PlayerCustomEffect playerCustomEffect)
          {
            if (entity.equals(playerCustomEffect.getPlayer()))
            {
              e.remove();
            }
          }
        }
      });
    }

    if (customEffectType == CustomEffectType.STAR_CATCH_PREPARE && entity instanceof Player player)
    {
      Integer i = Variable.starCatchPenalty.get(uuid);
      if (i == null)
      {
        i = 0;
      }
      player.playSound(player.getLocation(), "star_catch_" + (Math.min(4, i / 20) + 1), SoundCategory.PLAYERS, 1F, 1F);
      CustomEffectManager.addEffect(entity, new CustomEffect(CustomEffectType.STAR_CATCH_PROCESS, Math.max(20, 120 - i)));
    }

    if (customEffectType == CustomEffectType.STAR_CATCH_PROCESS && entity instanceof Player player)
    {
      CustomEffectManager.addEffect(player, CustomEffectType.STAR_CATCH_FINISHED);
      Integer i = Variable.starCatchPenalty.get(uuid);
      if (i == null)
      {
        i = 0;
      }
      Variable.starCatchPenalty.put(uuid, i + 1);
      CommandReinforce.REINFORCE_OPERATING.remove(uuid);
      player.performCommand("강화 realstart");
    }
  }
}
