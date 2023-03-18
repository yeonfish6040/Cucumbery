package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.children.group.LongCustomEffectImple;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTType;
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
    if (entity instanceof TNTPrimed tntPrimed)
    {
      ItemStack itemStack = BlockPlaceDataConfig.getItem(entity.getLocation());
      CustomMaterial customMaterial = CustomMaterial.itemStackOf(itemStack);
      if (customMaterial != null)
      {
        switch (customMaterial)
        {
          case TNT_I_WONT_LET_YOU_GO -> entity.getScoreboardTags().add("custom_material_tnt_i_wont_let_you_go");
          case TNT_COMBAT -> entity.getScoreboardTags().add("custom_material_tnt_combat");
          case TNT_DRAIN -> entity.getScoreboardTags().add("custom_material_tnt_drain");
          case TNT_DONUT ->
          {
            entity.getScoreboardTags().add("custom_material_tnt_donut");
            if (itemStack != null)
            {
              CustomEffectManager.addEffect(entity, new LongCustomEffectImple(CustomEffectType.CUSTOM_MATERIAL_TNT_DONUT, new NBTItem(itemStack).getShort("Size")));
            }
          }
        }
      }
      if (ItemStackUtil.itemExists(itemStack))
      {
        NBTItem nbtItem = new NBTItem(itemStack);
        Float explodePower = nbtItem.getFloat("ExplodePower");
        if (nbtItem.hasTag("ExplodePower") && nbtItem.getType("ExplodePower") == NBTType.NBTTagFloat && explodePower != null)
        {
          tntPrimed.setYield(explodePower);
        }
        Boolean fire = nbtItem.getBoolean("Fire");
        if (nbtItem.hasTag("Fire") && nbtItem.getType("Fire") == NBTType.NBTTagByte && fire != null && fire)
        {
          tntPrimed.setIsIncendiary(true);
        }
        Short fuse = nbtItem.getShort("Fuse");
        if (nbtItem.hasTag("Fuse") && nbtItem.getType("Fuse") == NBTType.NBTTagShort && fuse != null)
        {
          tntPrimed.setFuseTicks(fuse);
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
