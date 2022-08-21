package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;

import java.util.UUID;

public class LingeringPotionSplash implements Listener
{
  @EventHandler
  public void onLingeringPotionSplash(LingeringPotionSplashEvent event)
  {
    ThrownPotion thrownPotion = event.getEntity();
    AreaEffectCloud areaEffectCloud = event.getAreaEffectCloud();
    UUID uuid = thrownPotion.getUniqueId();
    if (Variable.projectile.containsKey(uuid))
    {
      ItemStack item = Variable.projectile.get(uuid);
      Variable.projectile.remove(uuid);
      Variable.projectile.put(areaEffectCloud.getUniqueId(), item.clone());
      ProjectileSource projectileSource = thrownPotion.getShooter();
      if (projectileSource instanceof LivingEntity livingEntity)
      {
        Variable.attackerAndWeapon.put(livingEntity.getUniqueId(), item.clone());
      }
      else if (projectileSource instanceof BlockProjectileSource blockProjectileSource)
      {
        Variable.entityAndSourceLocation.put(areaEffectCloud.getUniqueId(), blockProjectileSource.getBlock().getLocation().toString());
        Variable.blockAttackerAndWeapon.put(blockProjectileSource.getBlock().getLocation().toString(), item.clone());
      }
    }
  }
}
