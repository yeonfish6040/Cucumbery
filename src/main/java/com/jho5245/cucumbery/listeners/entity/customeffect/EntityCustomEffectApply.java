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
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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

    if (customEffectType.isInstant())
    {
      CustomEffectManager.removeEffect(entity, customEffectType);
    }

    if (customEffectType == CustomEffectType.CUCUMBERY_UPDATER)
    {
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
              Updater.defaultUpdater.updateLatest(amplifier == 1), 2L);
    }

    if (entity instanceof LivingEntity livingEntity)
    {
      int minecraftDuration = duration == -1 ? Integer.MAX_VALUE : duration;
      switch (customEffectType)
      {
        case MINECRAFT_SPEED -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, minecraftDuration, amplifier));
        case MINECRAFT_SLOWNESS -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, minecraftDuration, amplifier));
        case MINECRAFT_HASTE -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, minecraftDuration, amplifier));
        case MINECRAFT_MINING_FATIGUE -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, minecraftDuration, amplifier));
        case MINECRAFT_STRENGTH -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, minecraftDuration, amplifier));
        case MINECRAFT_WEAKNESS -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, minecraftDuration, amplifier));
        case MINECRAFT_INSTANT_DAMAGE -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.HARM, minecraftDuration, amplifier));
        case MINECRAFT_INSTANT_HEAL -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, minecraftDuration, amplifier));
        case MINECRAFT_JUMP_BOOST -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, minecraftDuration, amplifier));
        case MINECRAFT_NAUSEA -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, minecraftDuration, amplifier));
        case MINECRAFT_REGENERATION -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, minecraftDuration, amplifier));
        case MINECRAFT_RESISTANCE -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, minecraftDuration, amplifier));
        case MINECRAFT_FIRE_RESISTANCE -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, minecraftDuration, amplifier));
        case MINECRAFT_WATER_BREATHING -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, minecraftDuration, amplifier));
        case MINECRAFT_BLINDNESS -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, minecraftDuration, amplifier));
        case MINECRAFT_INVISIBILITY -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, minecraftDuration, amplifier));
        case MINECRAFT_NIGHT_VISION -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, minecraftDuration, amplifier));
        case MINECRAFT_HUNGER -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, minecraftDuration, amplifier));
        case MINECRAFT_POISON -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, minecraftDuration, amplifier));
        case MINECRAFT_WITHER -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, minecraftDuration, amplifier));
        case MINECRAFT_HEALTH_BOOST -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, minecraftDuration, amplifier));
        case MINECRAFT_ABSORPTION -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, minecraftDuration, amplifier));
        case MINECRAFT_SATURATION -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, minecraftDuration, amplifier));
        case MINECRAFT_LEVITATION -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, minecraftDuration, amplifier));
        case MINECRAFT_SLOW_FALLING -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, minecraftDuration, amplifier));
        case MINECRAFT_GLOWING -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, minecraftDuration, amplifier));
        case MINECRAFT_LUCK -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, minecraftDuration, amplifier));
        case MINECRAFT_UNLUCK -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, minecraftDuration, amplifier));
        case MINECRAFT_CONDUIT_POWER -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, minecraftDuration, amplifier));
        case MINECRAFT_DOLPHINS_GRACE -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, minecraftDuration, amplifier));
        case MINECRAFT_BAD_OMEN -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.BAD_OMEN, minecraftDuration, amplifier));
        case MINECRAFT_HERO_OF_THE_VILLAGE -> livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, minecraftDuration, amplifier));
      }
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


















