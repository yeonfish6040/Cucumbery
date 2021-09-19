package com.jho5245.cucumbery.listeners.entity;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

public class EntityShootBow implements Listener
{
  @EventHandler
  public void onEntityShootBow(EntityShootBowEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }

    LivingEntity livingEntity = event.getEntity();
    ItemStack item = event.getBow();
    Entity projectile = event.getProjectile();
    if (ItemStackUtil.itemExists(item))
    {
      item = item.clone();
      ItemLore.setItemLore(item, event);
      Variable.projectile.put(projectile.getUniqueId(), item);
      Variable.attackerAndWeapon.put(livingEntity.getUniqueId(), item);
      if (item.getType() == Material.CROSSBOW)
      {
        if (livingEntity instanceof Player player)
        {
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                  Method.updateInventory(player), 0L);
        }
      }
    }
  }
}
