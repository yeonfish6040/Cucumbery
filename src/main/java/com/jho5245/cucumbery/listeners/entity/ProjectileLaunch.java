package com.jho5245.cucumbery.listeners.entity;

import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class ProjectileLaunch implements Listener
{
  @EventHandler
  public void onProjectileLaunch(ProjectileLaunchEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Projectile projectile = event.getEntity();
    ProjectileSource projectileSource = projectile.getShooter();
    if (projectileSource instanceof Entity entity)
    {
      if (projectile instanceof Trident && CustomEffectManager.hasEffect(entity, CustomEffectType.IDIOT_SHOOTER))
      {
        double modifier = (CustomEffectManager.getEffect(entity, CustomEffectType.IDIOT_SHOOTER).getAmplifier() + 1) / 10d;
        Vector vector = entity.getLocation().getDirection();
        projectile.setVelocity(vector.add(new Vector(Math.random() * modifier - (modifier / 2d), Math.random() * modifier - (modifier / 2d), Math.random() * modifier - (modifier / 2d))));
      }
    }
  }
}
