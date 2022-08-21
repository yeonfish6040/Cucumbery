package com.jho5245.cucumbery.util.combat;

import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CombatManager
{
  @Nullable
  public static Object getDamager(@NotNull EntityDamageEvent event)
  {
    if (event.isCancelled())
    {
      return null;
    }
    Entity entity = event.getEntity();
    UUID uuid = entity.getUniqueId();
    DamageCause damageCause = event.getCause();
    switch (damageCause)
    {
      case CUSTOM, ENTITY_ATTACK, ENTITY_SWEEP_ATTACK, ENTITY_EXPLOSION, DRAGON_BREATH, MAGIC, POISON, WITHER -> {
        if (event instanceof EntityDamageByEntityEvent damageByEntityEvent)
        {
          Entity damager = damageByEntityEvent.getDamager();
          if (damager instanceof AreaEffectCloud areaEffectCloud)
          {
            ProjectileSource projectileSource = areaEffectCloud.getSource();
            if (projectileSource instanceof ThrownPotion thrownPotion)
            {
              ProjectileSource potionSource = thrownPotion.getShooter();
              if (potionSource instanceof Entity shooter)
              {
                return shooter;
              }
              return thrownPotion;
            }
            if (projectileSource == null && Variable.victimAndBlockDamager.containsKey(uuid))
            {
              return Variable.victimAndBlockDamager.get(uuid); // ItemStack - dispenser
            }
            return areaEffectCloud;
          }
          if (damager instanceof TNTPrimed tntPrimed)
          {
            Entity tntPrimer = tntPrimed.getSource();
            if (tntPrimer != null)
            {
              return tntPrimer;
            }
          }
        }
        if (Variable.victimAndDamager.containsKey(uuid))
        {
          return Variable.victimAndDamager.get(uuid);
        }
      }
      case PROJECTILE -> {
        if (event instanceof EntityDamageByEntityEvent damageByEntityEvent)
        {
          Entity damager = damageByEntityEvent.getDamager();
          if (damager instanceof Projectile projectile)
          {
            ProjectileSource projectileSource = projectile.getShooter();
            if (projectileSource == null)
            {
              if (Variable.victimAndBlockDamager.containsKey(uuid))
              {
                return Variable.victimAndBlockDamager.get(uuid); // ItemStack - dispenser
              }
            }
            else
            {
              return projectileSource;
            }
            return projectile;
          }
        }
      }
      default -> {
        if (Variable.victimAndDamager.containsKey(uuid))
        {
          return Variable.victimAndDamager.get(uuid);
        }
        if (Variable.victimAndBlockDamager.containsKey(uuid))
        {
          return Variable.victimAndBlockDamager.get(uuid); // ItemStack - dispenser
        }
      }
    }
    return null;
  }

  @Nullable
  public static ItemStack getWeapon(@NotNull EntityDamageEvent event)
  {
    Entity entity = event.getEntity();
    UUID uuid = entity.getUniqueId();
    Object damagerObject = getDamager(event);
    DamageCause damageCause = event.getCause();
    if (damagerObject instanceof Entity damager)
    {
      UUID damagerUuid = damager.getUniqueId();
      // parrot cookie weapon
      if (entity instanceof Parrot && event.getCause() == DamageCause.ENTITY_ATTACK)
      {
        ItemStack cookie = ItemSerializer.deserialize(Variable.attackerAndWeaponString.get(damagerUuid));
        if (ItemStackUtil.itemExists(cookie) && cookie.getType() == Material.COOKIE)
        {
          return cookie;
        }
      }
      switch (damageCause)
      {
        case CUSTOM, ENTITY_ATTACK, ENTITY_SWEEP_ATTACK, ENTITY_EXPLOSION, DRAGON_BREATH, MAGIC, POISON, WITHER -> {
          if (damager instanceof LivingEntity livingEntityDamager)
          {
            EntityEquipment entityEquipment = livingEntityDamager.getEquipment();
            if (entityEquipment != null)
            {
              return entityEquipment.getItemInMainHand();
            }
          }
          if (damager instanceof AreaEffectCloud areaEffectCloud)
          {
            ProjectileSource projectileSource = areaEffectCloud.getSource();
            if (projectileSource instanceof ThrownPotion thrownPotion)
            {
              ProjectileSource potionSource = thrownPotion.getShooter();
              if (potionSource instanceof Entity shooter)
              {
                UUID shooterUuid = shooter.getUniqueId();
                if (Variable.attackerAndWeapon.containsKey(shooterUuid))
                {
                  return Variable.attackerAndWeapon.get(shooterUuid);
                }
              }
              if (potionSource instanceof BlockProjectileSource blockProjectileSource)
              {
                Block block = blockProjectileSource.getBlock();
                if (Variable.blockAttackerAndWeapon.containsKey(block.getLocation().toString()))
                {
                  return Variable.blockAttackerAndWeapon.get(block.getLocation().toString());
                }
              }
            }
            if (projectileSource instanceof BlockProjectileSource blockProjectileSource)
            {
              Block block = blockProjectileSource.getBlock();
              if (Variable.blockAttackerAndWeapon.containsKey(block.getLocation().toString()))
              {
                return Variable.blockAttackerAndWeapon.get(block.getLocation().toString());
              }
            }
          }
        }
        case PROJECTILE -> {
          if (damager instanceof Projectile projectile)
          {
            ProjectileSource projectileSource = projectile.getShooter();
            if (projectileSource instanceof Entity source)
            {
              UUID sourceUuid = source.getUniqueId();
              if (Variable.attackerAndWeapon.containsKey(sourceUuid))
              {
                return Variable.attackerAndWeapon.get(sourceUuid);
              }
            }
            if (projectileSource instanceof BlockProjectileSource blockProjectileSource)
            {
              Block block = blockProjectileSource.getBlock();
              if (Variable.blockAttackerAndWeapon.containsKey(block.getLocation().toString()))
              {
                return Variable.blockAttackerAndWeapon.get(block.getLocation().toString());
              }
            }
            if (projectileSource == null)
            {
              UUID projectileUuid = projectile.getUniqueId();
              if (Variable.projectile.containsKey(projectileUuid))
              {
                return Variable.projectile.get(projectileUuid);
              }

            }
          }
        }
        default -> {
          if (Variable.attackerAndWeapon.containsKey(damagerUuid))
          {
            return Variable.attackerAndWeapon.get(damagerUuid);
          }
        }
      }
    }
    else
    {
      // TODO: yeet
    }
    return null;
  }

  @Nullable
  public static ItemStack getSubWeapon(@NotNull EntityDamageEvent event)
  {
    return null;
  }
}
