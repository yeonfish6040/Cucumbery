package com.jho5245.cucumbery.customeffect.scheduler;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.customeffect.CustomEffect;
import com.jho5245.cucumbery.customeffect.CustomEffect.DisplayType;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.customeffect.scheduler.metasasis.MetasasisScheduler;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Variable;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class CustomEffectScheduler
{
  public static void schedule(@NotNull Cucumbery cucumbery)
  {
    Bukkit.getServer().getScheduler().runTaskTimer(cucumbery, () ->
    {
      tick();
      display();
      axolotlsGrace();
    }, 0L, 1L);
    MetasasisScheduler.schedule(cucumbery);
  }

  private static void tick()
  {
    for (World world : Bukkit.getWorlds())
    {
      for (Entity entity : world.getEntities())
      {
        UUID uuid = entity.getUniqueId();
        List<CustomEffect> customEffects = CustomEffectManager.getEffects(entity);
        if (customEffects.isEmpty())
        {
          continue;
        }
        for (CustomEffect customEffect : customEffects)
        {
          customEffect.tick();
          if (customEffect.getDuration() == 0)
          {
            EntityCustomEffectRemoveEvent event = new EntityCustomEffectRemoveEvent(entity, customEffect);
            Cucumbery.getPlugin().getPluginManager().callEvent(event);
          }
        }
        customEffects.removeIf(effect -> effect.getDuration() == 0);
        CustomEffectManager.effectMap.put(uuid, customEffects);
      }
    }
  }

  private static void display()
  {
    for (Player player : Bukkit.getOnlinePlayers())
    {
      UUID uuid = player.getUniqueId();
      List<CustomEffect> customEffects = CustomEffectManager.getEffects(player, DisplayType.ACTION_BAR);
      if (!customEffects.isEmpty())
      {
        MessageUtil.sendActionBar(player, CustomEffectManager.getDisplay(customEffects));
      }
      customEffects = CustomEffectManager.getEffects(player, DisplayType.BOSS_BAR);
      if (customEffects.isEmpty())
      {
        if (Variable.customEffectBossBarMap.containsKey(uuid))
        {
          player.hideBossBar(Variable.customEffectBossBarMap.get(uuid));
          Variable.customEffectBossBarMap.remove(uuid);
        }
      }
      else
      {
        if (!Variable.customEffectBossBarMap.containsKey(uuid))
        {
          BossBar bossBar = BossBar.bossBar(Component.empty(), 1f, BossBar.Color.GREEN, Overlay.PROGRESS);
          Variable.customEffectBossBarMap.put(uuid, bossBar);
        }
        BossBar bossBar = Variable.customEffectBossBarMap.get(uuid);
        bossBar.name(CustomEffectManager.getDisplay(customEffects));
        player.showBossBar(bossBar);
      }
      customEffects = CustomEffectManager.getEffects(player, DisplayType.PLAYER_LIST);
      if (!customEffects.isEmpty())
      {
        Component component = Component.empty();
        component = component.append(Component.text("\n"));
        component = component.append(
                ComponentUtil.createTranslate("&e적용 중인 효과 목록 : %s개", "&2" + customEffects.size())
        );
        component = component.append(Component.text("\n"));
        for (CustomEffect customEffect : customEffects)
        {
          int duration = customEffect.getDuration();
          int amplifier = customEffect.getAmplifier();
          component = component.append(Component.text("\n"));
          component = component.append(
                  ComponentUtil.createTranslate(amplifier == 0 ? "%1$s%2$s" : "%1$s %3$s%2$s", customEffect,
                          (duration != -1 && duration != customEffect.getInitDuration() - 1) ?
                                  " (" + Method.timeFormatMilli(duration * 50L, duration < 200, 1, true) + ")":
                                  ""
                          , amplifier + 1)
          );
        }
        player.sendPlayerListFooter(component);
      }
    }
  }

  private static void axolotlsGrace()
  {
    for (World world : Bukkit.getWorlds())
    {
      for (Entity entity : world.getEntities())
      {
        if (!(entity instanceof AnimalTamer && entity instanceof Damageable damageable))
        {
          continue;
        }
        if (damageable.getHealth() > 5d)
        {
          continue;
        }
        List<Entity> entities = entity.getNearbyEntities(15, 15, 15);
        for (Entity loop : entities)
        {
          if (loop instanceof Parrot parrot && parrot.getOwner() == entity)
          {
            CustomEffectManager.addEffect(entity, new CustomEffect(CustomEffectType.PARROTS_CHEER));
            break;
          }
        }
      }
    }
  }
}
