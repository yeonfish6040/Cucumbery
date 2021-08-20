package com.jho5245.cucumbery.listeners.player;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class PlayerLaunchProjectile implements Listener
{
  @EventHandler
  public void onPlayerLaunchProjectile(PlayerLaunchProjectileEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }

    Player player = event.getPlayer();
    ItemStack itemStack = event.getItemStack();
    Projectile projectile = event.getProjectile();
    EntityType entityType = projectile.getType();
    if (entityType == EntityType.SPLASH_POTION)
    {
      Variable.projectile.put(projectile.getUniqueId(), itemStack.clone());
      Variable.attackerAndWeapon.put(player.getUniqueId(), itemStack.clone());
    }
  }
}
