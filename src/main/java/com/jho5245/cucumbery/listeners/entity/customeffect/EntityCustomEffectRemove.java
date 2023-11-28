package com.jho5245.cucumbery.listeners.entity.customeffect;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.commands.reinforce.CommandReinforce;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectScheduler;
import com.jho5245.cucumbery.custom.customeffect.children.group.*;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeReinforce;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeRune;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent.RemoveReason;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.data.Prefix;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

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
    int amplifier = customEffect.getAmplifier();
    RemoveReason removeReason = event.getReason();
    if (entity instanceof Player player && customEffect.getDisplayType() == DisplayType.PLAYER_LIST && CustomEffectManager.getEffects(entity, DisplayType.PLAYER_LIST).isEmpty())
    {
      player.sendPlayerListFooter(Component.empty());
    }
    if (entity instanceof Player player && (
            customEffectType == CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE ||
                    customEffectType == CustomEffectTypeCustomMining.MINDAS_TOUCH ||
                    customEffectType == CustomEffectType.CURSE_OF_CREATIVITY ||
                    customEffectType == CustomEffectType.CURSE_OF_CREATIVITY_PLACE
    ))
    {
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> ItemStackUtil.updateInventory(player), 2L);
    }

    if (entity instanceof Player player && (customEffectType == CustomEffectType.FLY || customEffectType == CustomEffectType.FLY_REMOVE_ON_QUIT) && player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR)
    {
      MessageUtil.info(player, "비행 모드 종료!");
      player.setAllowFlight(false);
      return;
    }

    if (customEffectType == CustomEffectType.FLY_NOT_ENABLED && entity instanceof Player player && player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR &&
            (CustomEffectManager.hasEffect(entity, CustomEffectType.FLY) || CustomEffectManager.hasEffect(entity, CustomEffectType.FLY_REMOVE_ON_QUIT)))
    {
      CustomEffect effect = CustomEffectManager.hasEffect(entity, CustomEffectType.FLY) ? CustomEffectManager.getEffect(entity, CustomEffectType.FLY) : CustomEffectManager.getEffect(entity, CustomEffectType.FLY_REMOVE_ON_QUIT);
      MessageUtil.info(player, "비행 모드 사용이 불가능한 지역에서 진출하여 %s 효과가 다시 활성화됩니다!", effect);
      return;
    }

    if (entity instanceof Player player && customEffectType == CustomEffectType.SPYGLASS_TELEPORT && removeReason == RemoveReason.TIME_OUT)
    {
      RayTraceResult rayTraceResult = player.rayTraceBlocks(100d);
      if (rayTraceResult == null)
      {
        MessageUtil.sendWarn(player, "100블록 이내에 바라보고 있는 블록이 없습니다");
        CustomEffectManager.addEffect(player, CustomEffectType.SPYGLASS_TELEPORT_COOLDOWN, 100);
        return;
      }
      Block block = rayTraceResult.getHitBlock();
      if (block == null)
      {
        MessageUtil.sendWarn(player, "100블록 이내에 바라보고 있는 블록이 없습니다");
        CustomEffectManager.addEffect(player, CustomEffectType.SPYGLASS_TELEPORT_COOLDOWN, 100);
        return;
      }
      Location location = block.getLocation().add(0.5, 1, 0.5);
      if (location.getBlock().getType().isOccluding() || location.clone().add(0, 1, 0).getBlock().getType().isOccluding())
      {
        MessageUtil.sendWarn(player, "바라보고 있는 블록 위가 막혀 있어 순간 이동할 수 없습니다");
        CustomEffectManager.addEffect(player, CustomEffectType.SPYGLASS_TELEPORT_COOLDOWN, 100);
        return;
      }
      location.setYaw(player.getLocation().getYaw());
      location.setPitch(player.getLocation().getPitch());
      if (player.teleport(location))
      {
        MessageUtil.info(player, "%s 위치로 순간 이동했습니다!", location);
        CustomEffectManager.addEffect(player, CustomEffectType.SPYGLASS_TELEPORT_COOLDOWN);
      }
      else
      {
        MessageUtil.sendWarn(player, "알 수 없는 이유로 순간 이동에 실패했습니다! 혹시 다른 플레이어가 자신을 탑승하고 있나요?");
        CustomEffectManager.addEffect(player, CustomEffectType.SPYGLASS_TELEPORT_COOLDOWN, 100);
      }
    }

/*    if (entity instanceof Player player && customEffectType == CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_PROGRESS)
    {
      player.sendExperienceChange(player.getExp(), player.getLevel());
    }*/

    if (entity instanceof Player player && customEffectType == CustomEffectTypeRune.RUNE_USING && removeReason == RemoveReason.TIME_OUT)
    {
      MessageUtil.sendTitle(player, "", "g255;시간이 초과되어 룬 해방에 실패했습니다.", 0, 0, 150);
      CustomEffectManager.addEffect(entity, new CustomEffect(CustomEffectTypeRune.RUNE_COOLDOWN, 20 * 5));
    }

    if (customEffectType == CustomEffectType.ALARM)
    {
      if (removeReason == RemoveReason.TIME_OUT)
      {
        for (int i = 0; i < 10; i++)
        {
          int finalI = i;
          Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
          {
            MessageUtil.info(entity, "시간 다 됐다 일어나 이 인간아!!!!");
            SoundPlay.playSound(entity, Sound.ENTITY_ENDER_DRAGON_DEATH, 2F, 0.5F);
            SoundPlay.playSound(entity, Sound.ENTITY_WITCH_DEATH, 2F, 0.5F);
            SoundPlay.playSound(entity, Sound.ENTITY_PLAYER_LEVELUP, 2F, 0.5F);
            SoundPlay.playSound(entity, Sound.ENTITY_GENERIC_EXPLODE, 2F, 0.5F);
            SoundPlay.playSound(entity, Sound.UI_TOAST_CHALLENGE_COMPLETE, 2F, 1F);
            SoundPlay.playSound(entity, Sound.ITEM_GOAT_HORN_SOUND_0, 2F, 2F);
            SoundPlay.playSound(entity, Sound.ITEM_GOAT_HORN_SOUND_1, 2F, 2F);
            SoundPlay.playSound(entity, Sound.ITEM_GOAT_HORN_SOUND_2, 2F, 2F);
            SoundPlay.playSound(entity, Sound.ITEM_GOAT_HORN_SOUND_3, 2F, 2F);
            SoundPlay.playSound(entity, Sound.ITEM_GOAT_HORN_SOUND_4, 2F, 2F);
            SoundPlay.playSound(entity, Sound.ITEM_GOAT_HORN_SOUND_5, 2F, 2F);
            SoundPlay.playSound(entity, Sound.ITEM_GOAT_HORN_SOUND_6, 2F, 2F);
            SoundPlay.playSound(entity, Sound.ITEM_GOAT_HORN_SOUND_7, 2F, 2F);
            SoundPlay.playSound(entity, "reinforce_destroy");
            if (amplifier >= 1)
            {
              if (entity instanceof Player player)
              {
                player.spawnParticle(Particle.MOB_APPEARANCE, player.getLocation(), 1);
              }
              if (amplifier >= 2)
              {
                entity.setVelocity(new Vector(Math.random() * finalI - 5, Math.random() * finalI - 3, Math.random() * finalI - 3));
              }
            }
          }, i);
        }
      }
      else if (removeReason == RemoveReason.GUI)
      {
        MessageUtil.info(entity, "알람 취소됨!");
      }
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

    if (customEffectType == CustomEffectTypeReinforce.STAR_CATCH_PREPARE && entity instanceof Player player)
    {
      Integer i = Variable.starCatchPenalty.get(uuid);
      if (i == null)
      {
        i = 0;
      }
      player.playSound(player.getLocation(), "star_catch_" + (Math.min(4, i / 20) + 1), SoundCategory.PLAYERS, 1F, 1F);
      CustomEffectManager.addEffect(entity, new CustomEffect(CustomEffectTypeReinforce.STAR_CATCH_PROCESS, Math.max(20, 120 - i)));
    }

    if (customEffectType == CustomEffectTypeReinforce.STAR_CATCH_PROCESS && entity instanceof Player player)
    {
      CustomEffectManager.addEffect(player, CustomEffectTypeReinforce.STAR_CATCH_FINISHED);
      CommandReinforce.REINFORCE_OPERATING.remove(uuid);
      player.performCommand("강화 realstart");
    }

    if (customEffectType == CustomEffectType.STRANGE_CREATIVE_MODE && entity instanceof Player player && customEffect instanceof StringCustomEffect stringCustomEffect)
    {
      String data = stringCustomEffect.getString();
      try
      {
        String[] split = data.split("\\|");
        player.setGameMode(GameMode.valueOf(split[0]));
        NBTEntity nbtEntity = new NBTEntity(player);
        nbtEntity.mergeCompound(new NBTContainer("{abilities:{instabuild:" + split[1] + ",invulnerable:" + split[2] + "}}"));
        player.setAllowFlight(Boolean.parseBoolean(split[3]));
      }
      catch (Exception ignored)
      {

      }
    }

    if (customEffectType.getNamespacedKey().getNamespace().equals("minecraft") && entity instanceof LivingEntity livingEntity)
    {
      PotionEffectType potionEffectType = PotionEffectType.getByName(customEffectType.getNamespacedKey().getKey());
      if (potionEffectType == null)
      {
        throw new NullPointerException("Invalid Potion Effect Type: " + customEffectType.getIdString());
      }
      livingEntity.removePotionEffect(potionEffectType);
    }
/*
    if (entity instanceof LivingEntity livingEntity)
    {
      if (customEffectType == CustomEffectTypeMinecraft.SPEED)
      {
        livingEntity.removePotionEffect(PotionEffectType.SPEED);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
      if (customEffectType == CustomEffectTypeMinecraft.HASTE)
      {
        livingEntity.removePotionEffect(PotionEffectType.FAST_DIGGING);
      }
      if (customEffectType == CustomEffectTypeMinecraft.MINING_FATIGUE)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW_DIGGING);
      }
      if (customEffectType == CustomEffectTypeMinecraft.STRENGTH)
      {
        livingEntity.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
      }
      if (customEffectType == CustomEffectTypeMinecraft.INSTANT_HEALTH)
      {
        livingEntity.removePotionEffect(PotionEffectType.HEAL);
      }
      if (customEffectType == CustomEffectTypeMinecraft.INSTANT_DAMAGE)
      {
        livingEntity.removePotionEffect(PotionEffectType.HARM);
      }
      if (customEffectType == CustomEffectTypeMinecraft.JUMP_BOOST)
      {
        livingEntity.removePotionEffect(PotionEffectType.JUMP);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
      if (customEffectType == CustomEffectTypeMinecraft.SLOWNESS)
      {
        livingEntity.removePotionEffect(PotionEffectType.SLOW);
      }
    }*/
  }
}
