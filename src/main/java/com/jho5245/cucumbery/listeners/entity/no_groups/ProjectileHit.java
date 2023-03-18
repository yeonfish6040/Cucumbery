package com.jho5245.cucumbery.listeners.entity.no_groups;

import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Set;

public class ProjectileHit implements Listener
{
  @EventHandler
  public void onProjectileHit(ProjectileHitEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Projectile projectile = event.getEntity();
    if (projectile instanceof AbstractArrow abstractArrow)
    {
      Set<String> tags = abstractArrow.getScoreboardTags();
      if (tags.contains("custom_material_arrow_explosive"))
      {
        abstractArrow.getWorld().createExplosion(abstractArrow.getLocation(), 2f, false, false, abstractArrow);
        abstractArrow.remove();
      }
      if (tags.contains("custom_material_arrow_explosive_destruction"))
      {
        abstractArrow.getWorld().createExplosion(abstractArrow.getLocation(), 2f, false, true, abstractArrow);
        abstractArrow.remove();
      }
      if (tags.contains("custom_material_arrow_mount"))
      {
        if (Math.random() > 0.5)
        {
          abstractArrow.remove();
        }
      }
      if (tags.contains("custom_material_arrow_mount_disposal"))
      {
        abstractArrow.remove();
      }
    }
  }
}
