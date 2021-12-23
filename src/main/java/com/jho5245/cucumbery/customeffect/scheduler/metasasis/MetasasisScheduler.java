package com.jho5245.cucumbery.customeffect.scheduler.metasasis;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.customeffect.CustomEffect;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MetasasisScheduler
{
  public static void schedule(@NotNull Cucumbery cucumbery)
  {
    Bukkit.getServer().getScheduler().runTaskTimer(cucumbery, () ->
    {
      tick();
    }, 0L, 1L);
  }

  private static void tick()
  {
    for (World world : Bukkit.getWorlds())
    {
      for (Entity entity : world.getEntities())
      {
        if (!(entity instanceof Player || (entity instanceof Tameable tameable && tameable.isTamed())))
        {
          continue;
        }
        if (CustomEffectManager.hasEffect(entity, CustomEffectType.METASASIS))
        {
          CustomEffect customEffectMetasasis = CustomEffectManager.getEffect(entity, CustomEffectType.METASASIS);
          int amplifier = customEffectMetasasis.getInitAmplifier();
          double radius = 2d + Math.pow(amplifier + 1d, 2d) * 0.0008;
          List<Entity> nearbyEntities = entity.getNearbyEntities(radius, radius, radius);
          for (Entity nearbyEntity : nearbyEntities)
          {
            if (entity == nearbyEntity || CustomEffectManager.hasEffect(nearbyEntity, CustomEffectType.METASASIS) || !(nearbyEntity instanceof Player || (nearbyEntity instanceof Tameable tameable && tameable.isTamed())))
            {
              continue;
            }
            int duration = customEffectMetasasis.getInitDuration();
            CustomEffectManager.addEffect(nearbyEntity, new CustomEffect(CustomEffectType.METASASIS, duration, amplifier));
          }
        }
      }
    }
  }
}
