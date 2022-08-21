package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;

import java.util.UUID;

public class EntitySpawn implements Listener
{
  @EventHandler
  public void onEntitySpawn(EntitySpawnEvent event)
  {
    Entity entity = event.getEntity();
    if (entity instanceof Projectile projectile && Method.usingLoreFeature(entity.getLocation()))
    {
      if (projectile instanceof ThrowableProjectile throwableProjectile)
      {
        throwableProjectile.setItem(ItemLore.setItemLore(throwableProjectile.getItem()));
      }
      else if (projectile instanceof ThrownPotion thrownPotion)
      {
        thrownPotion.setItem(ItemLore.setItemLore(thrownPotion.getItem()));
      }
    }

    if (entity instanceof Projectile projectile)
    {
      UUID projectileUUID = projectile.getUniqueId();
      ProjectileSource projectileSource = projectile.getShooter();
      if (projectileSource instanceof BlockProjectileSource blockProjectileSource)
      {
        Variable.entityAndSourceLocation.put(projectileUUID, blockProjectileSource.getBlock().getLocation().toString());
      }
      if (projectile instanceof ThrowableProjectile throwableProjectile)
      {
        ItemStack item = throwableProjectile.getItem();
        if (projectileSource instanceof LivingEntity livingEntity)
        {
          Variable.attackerAndWeapon.put(livingEntity.getUniqueId(), item.clone());
        }
        else if (projectileSource instanceof BlockProjectileSource blockProjectileSource)
        {
          Variable.projectile.put(projectileUUID, Variable.blockAttackerAndWeapon.get(blockProjectileSource.getBlock().getLocation().toString()));
        }
        else
        {
          Variable.projectile.put(projectileUUID, item.clone());
        }
      }
      else if (projectile instanceof ThrownPotion thrownPotion)
      {
        ItemStack item = thrownPotion.getItem();
        if (projectileSource instanceof LivingEntity livingEntity)
        {
          Variable.attackerAndWeapon.put(livingEntity.getUniqueId(), item.clone());
        }
        else if (projectileSource instanceof BlockProjectileSource blockProjectileSource)
        {
          Variable.projectile.put(projectileUUID, Variable.blockAttackerAndWeapon.get(blockProjectileSource.getBlock().getLocation().toString()));
        }
        else
        {

          Variable.projectile.put(thrownPotion.getUniqueId(), item.clone());
        }
      }
      else if (projectile instanceof Firework firework)
      {
        ItemStack item = new ItemStack(Material.FIREWORK_ROCKET);
        item.setItemMeta(firework.getFireworkMeta());
        if (Method.usingLoreFeature(projectile.getLocation()))
        {
          ItemLore.setItemLore(item);
        }
        if (projectileSource instanceof LivingEntity livingEntity)
        {
          ItemStack crossBow = ItemStackUtil.getPlayerUsingItem(livingEntity, Material.CROSSBOW);
          if (ItemStackUtil.itemExists(crossBow) && firework.isShotAtAngle())
          {
            Variable.projectile.put(firework.getUniqueId(), crossBow.clone());
            Variable.attackerAndWeapon.put(livingEntity.getUniqueId(), crossBow.clone());
          }
          else
          {
            Variable.projectile.put(firework.getUniqueId(), item.clone());
            Variable.attackerAndWeapon.put(livingEntity.getUniqueId(), item.clone());
          }
        }
        else if (projectileSource instanceof BlockProjectileSource blockProjectileSource)
        {

          Variable.projectile.put(projectileUUID, Variable.blockAttackerAndWeapon.get(blockProjectileSource.getBlock().getLocation().toString()));
        }
        else
        {

          Variable.projectile.put(firework.getUniqueId(), item.clone());
          Location loc = projectile.getLocation();
          String key = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()).toString();
          Variable.entityAndSourceLocation.put(projectileUUID, key);
        }
      }
    }
    /*
    if (entity instanceof TNTPrimed tntPrimed)
    {
      if (tntPrimed.getSource() == null)
      {
        Collection<LivingEntity> yeet = tntPrimed.getLocation().getNearbyLivingEntities(30);
        if (yeet.size() > 0)
        {
          double distance = Double.MAX_VALUE;
          LivingEntity target = null;
          for (LivingEntity iterate : yeet)
          {
            if (tntPrimed.equals(iterate))
            {
              continue;
            }
              double temp = iterate.getLocation().distance(tntPrimed.getLocation());
              if (distance == Double.MAX_VALUE || temp < distance)
              {
                distance = temp;
                target = iterate;
              }
          }
          tntPrimed.setSource(target);
        }
      }
    }*/

    if (entity instanceof LivingEntity livingEntity)
    {
      EntityEquipment equipment = livingEntity.getEquipment();
      if (equipment != null)
      {
        ItemStack mainHand = equipment.getItemInMainHand(), offHand = equipment.getItemInOffHand(), helmet = equipment.getHelmet(), chestplate = equipment.getChestplate(), leggings = equipment
                .getLeggings(), boots = equipment.getBoots();
        ItemLore.setItemLore(mainHand);
        ItemLore.setItemLore(offHand);
        // when kiled Pillager's helmet Banner has TMI, it is not considered as Bad Omen effect-given entity.
        boolean isPillager = livingEntity instanceof Pillager && helmet != null && helmet.getType() == Material.WHITE_BANNER;
        if (!isPillager && helmet != null)
        {
          ItemLore.setItemLore(helmet);
        }
        if (chestplate != null)
        {
          ItemLore.setItemLore(chestplate);
        }
        if (leggings != null)
        {
          ItemLore.setItemLore(leggings);
        }
        if (boots != null)
        {
          ItemLore.setItemLore(boots);
        }
        equipment.setItemInMainHand(mainHand);
        equipment.setItemInOffHand(offHand);
        if (!isPillager)
        {
          equipment.setHelmet(helmet);
        }
        equipment.setChestplate(chestplate);
        equipment.setLeggings(leggings);
        equipment.setBoots(boots);
      }
    }
  }
}
