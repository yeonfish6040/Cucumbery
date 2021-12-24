package com.jho5245.cucumbery.listeners.entity;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
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
import org.bukkit.util.Vector;

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
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.IDIOT_SHOOTER))
    {
      double modifier = (CustomEffectManager.getEffect(livingEntity, CustomEffectType.IDIOT_SHOOTER).getAmplifier() + 1) / 10d;
      Vector vector = livingEntity.getLocation().getDirection();
      projectile.setVelocity(vector.add(new Vector(Math.random() * modifier - (modifier / 2d), Math.random() * modifier - (modifier / 2d), Math.random() * modifier - (modifier / 2d))));
    }
    if (ItemStackUtil.itemExists(item))
    {
      item = item.clone();
      ItemLore.setItemLore(item, event);
      ItemStack consumable = event.getConsumable();
      if (ItemStackUtil.itemExists(consumable))
      {
        Variable.projectile.put(projectile.getUniqueId(), consumable);
      }
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
