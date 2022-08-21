package com.jho5245.cucumbery.listeners.player.no_groups;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

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
    int level = 0;
    if (CustomEffectManager.hasEffect(player, CustomEffectType.IDIOT_SHOOTER))
    {
      level = CustomEffectManager.getEffect(player, CustomEffectType.IDIOT_SHOOTER).getAmplifier() + 1;
    }
    if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasEnchants())
    {
      level = Math.max(level, itemStack.getEnchantmentLevel(CustomEnchant.IDIOT_SHOOTER));
    }
    if (level > 0)
    {
      if (projectile instanceof Firework)
      {
        double modifier = level / 100d;
        projectile.setVelocity(projectile.getVelocity().add(new Vector(Math.random() * modifier - (modifier / 2d), Math.random() * modifier - (modifier / 2d), Math.random() * modifier - (modifier / 2d))));
      }
      else
      {
        double modifier = level / 10d;
        Vector vector = player.getLocation().getDirection();
        projectile.setVelocity(vector.add(new Vector(Math.random() * modifier - (modifier / 2d), Math.random() * modifier - (modifier / 2d), Math.random() * modifier - (modifier / 2d))));
      }
    }
    EntityType entityType = projectile.getType();
    if (entityType == EntityType.SPLASH_POTION)
    {
      itemStack = itemStack.clone();
      Variable.projectile.put(projectile.getUniqueId(), itemStack);
      Variable.attackerAndWeapon.put(player.getUniqueId(), itemStack);
    }
  }
}
