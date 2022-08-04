package com.jho5245.cucumbery.listeners.entity.customeffect;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.children.group.AttributeCustomEffect;
import com.jho5245.cucumbery.custom.customeffect.children.group.AttributeCustomEffectImple;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectPostApplyEvent;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent.RemoveReason;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.Updater;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EntityCustomEffectPostApply implements Listener
{
  @EventHandler
  public void onEntityCustomEffectPostApply(EntityCustomEffectPostApplyEvent event)
  {
    Entity entity = event.getEntity();
    CustomEffect customEffect = event.getCustomEffect();
    int duration = customEffect.getDuration(), initDuration = customEffect.getInitDuration();
    int amplifier = customEffect.getAmplifier(), initAmplifier = customEffect.getInitAmplifier();
    CustomEffectType customEffectType = customEffect.getType();
    List<CustomEffectType> conflictEffects = customEffectType.getConflictEffects();

    for (CustomEffectType conflictEffect : conflictEffects)
    {
      CustomEffectManager.removeEffect(entity, conflictEffect, RemoveReason.CONFLICT);
    }

    if (customEffectType == CustomEffectType.ALARM)
    {
      MessageUtil.info(entity, "알람이 설정되었습니다! %s초 뒤에 시끄러운 소리가 남!", "&e" + Constant.Sosu2.format(duration / 20d));
    }

    if (entity instanceof Player player && (
            customEffectType == CustomEffectType.CUSTOM_MINING_SPEED_MODE ||
    customEffectType == CustomEffectType.CURSE_OF_CREATIVITY ||
    customEffectType == CustomEffectType.CURSE_OF_CREATIVITY_PLACE
    ))
    {
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Method.updateInventory(player), 2L);
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

    if (customEffectType.isInstant())
    {
      CustomEffectManager.removeEffect(entity, customEffectType);
    }

    if (customEffectType == CustomEffectType.CUCUMBERY_UPDATER)
    {
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
        attributeInstance.addModifier(new AttributeModifier(uuid, "cucumbery-" + customEffectType.translationKey(),
                (amplifier + 1) * attributeCustomEffect.getMultiplier(), attributeCustomEffect.getOperation()));
      }
    }

    if (customEffectType == CustomEffectType.VAR_DETOXICATE || customEffectType == CustomEffectType.VAR_PODAGRA || customEffectType == CustomEffectType.VAR_PNEUMONIA || customEffectType == CustomEffectType.VAR_STOMACHACHE)
    {
      CustomEffectManager.addEffect(entity, new CustomEffect(CustomEffectType.SPREAD, duration, amplifier));
    }

    if (customEffectType == CustomEffectType.VAR_DETOXICATE && entity instanceof LivingEntity livingEntity)
    {
      if (!CustomEffectManager.hasEffect(entity, CustomEffectType.VAR_DETOXICATE_ACTIVATED))
      {
        boolean doubleDecreased = Math.random() < (amplifier + 1), tripledDecreased = Math.random() < (amplifier + 1) * 0.1, allRemoved = doubleDecreased && tripledDecreased;
        if (allRemoved)
        {
          livingEntity.removePotionEffect(PotionEffectType.POISON);
          livingEntity.removePotionEffect(PotionEffectType.CONFUSION);
          livingEntity.removePotionEffect(PotionEffectType.BLINDNESS);
          livingEntity.removePotionEffect(PotionEffectType.UNLUCK);
          MessageUtil.sendMessage(livingEntity, Prefix.INFO_CUSTOM_EFFECT, "와 샌즈! %s의 효과로 인해 디버프가 제거되었습니다!", customEffect);
        }
        else if (doubleDecreased || tripledDecreased)
        {
          int decreation = doubleDecreased ? 2 : 3;
          PotionEffectType potionEffectType = switch ((int) (Math.random() * 4))
                  {
                    case 0 -> PotionEffectType.POISON;
                    case 1 -> PotionEffectType.CONFUSION;
                    case 2 -> PotionEffectType.BLINDNESS;
                    default -> PotionEffectType.UNLUCK;
                  };
          PotionEffect potionEffect = livingEntity.getPotionEffect(potionEffectType);
          if (potionEffect != null)
          {
            livingEntity.removePotionEffect(potionEffectType);
            if (potionEffect.getAmplifier() - decreation > 0)
            {
              potionEffect = potionEffect.withAmplifier(potionEffect.getAmplifier() - decreation);
              livingEntity.addPotionEffect(potionEffect);
            }
          }
        }
      }
      CustomEffectManager.addEffect(entity, CustomEffectType.VAR_DETOXICATE_ACTIVATED);
    }

    if (customEffectType == CustomEffectType.NO_ENTITY_AGGRO)
    {
      List<Entity> nearbyEntities = entity.getWorld().getEntities();
      for (Entity e : nearbyEntities)
      {
        if (e instanceof Mob mob && Objects.equals(mob.getTarget(), entity))
        {
          mob.setTarget(null);
        }
      }
    }

    if (customEffectType == CustomEffectType.COMBAT_MODE_MELEE)
    {
      CustomEffectManager.addEffect(entity, CustomEffectType.COMBAT_MODE_MELEE_COOLDOWN);
    }

    if (customEffectType == CustomEffectType.COMBAT_MODE_RANGED)
    {
      CustomEffectManager.addEffect(entity, CustomEffectType.COMBAT_MODE_RANGED_COOLDOWN);
    }

    if (customEffectType == CustomEffectType.BREAD_KIMOCHI)
    {
      CustomEffectManager.addEffect(entity, new AttributeCustomEffectImple(
              CustomEffectType.BREAD_KIMOCHI_SECONDARY_EFFECT, initDuration, initAmplifier, DisplayType.NONE, UUID.randomUUID(), Attribute.GENERIC_ARMOR, Operation.ADD_NUMBER, 2));
    }

    if (customEffectType == CustomEffectType.FREEZING)
    {
      entity.setFreezeTicks(entity.getMaxFreezeTicks());
      entity.lockFreezeTicks(true);
    }

    if (customEffectType == CustomEffectType.STAR_CATCH_PREPARE && entity instanceof Player player)
    {
      Integer penalty = Variable.starCatchPenalty.get(player.getUniqueId());
      MessageUtil.sendTitle(player, ComponentUtil.translate("&e스타캐치"), ComponentUtil.translate(
              penalty != null && penalty >= 20 ? "연속해서 강화를 시도하면 스타캐치 난이도가 증가합니다" :
                      "별이 초록색일 때 스타캐치 클릭하면 강화 성공률 증가!"
      ), 0, 100, 0);
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, "           %s               ", ComponentUtil.translate("&e[                스타캐치 클릭                ]").hoverEvent(ComponentUtil.translate("클릭하여 스타캐치를 합니다")).clickEvent(
              ClickEvent.runCommand(Constant.REINFORCE_STAR_CATCH)));
      MessageUtil.sendMessage(player, Prefix.INFO_REINFORCE, Constant.SEPARATOR);
    }
  }
}


















