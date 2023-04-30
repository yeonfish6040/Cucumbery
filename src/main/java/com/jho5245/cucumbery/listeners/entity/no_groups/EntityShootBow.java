package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.children.group.DoubleCustomEffect;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.UUID;

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
    UUID uuid = livingEntity.getUniqueId();
    ItemStack bow = event.getBow();
    ItemStack consumable = event.getConsumable();
    if (livingEntity instanceof Player player && NBTAPI.isRestricted(player, bow, RestrictionType.NO_SHOT) && bow != null)
    {
      event.setCancelled(true);
      if (!Variable.playerShootBowAlertCooldown.contains(uuid))
      {
        Variable.playerActionbarCooldownIsShowing.add(uuid);
        MessageUtil.sendTitle(player, "&c발사 불가!", ComponentUtil.translate("%s은(는) 발사 무기로 사용할 수 없는 아이템입니다", bow), 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerShootBowAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (livingEntity instanceof Player player && NBTAPI.isRestricted(player, consumable, RestrictionType.NO_SHOT_AMMO) && consumable != null)
    {
      event.setCancelled(true);
      if (!Variable.playerShootBowAlertCooldown.contains(uuid))
      {
        Variable.playerActionbarCooldownIsShowing.add(uuid);
        MessageUtil.sendTitle(player, "&c발사 불가!", ComponentUtil.translate("%s은(는) 발사체로 사용할 수 없는 아이템입니다", consumable), 5, 80, 15);
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerShootBowAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (!(livingEntity instanceof Player) && NBTAPI.isRestricted(bow, RestrictionType.NO_SHOT) && bow != null)
    {
      event.setCancelled(true);
      if (!Variable.playerShootBowAlertCooldown.contains(uuid))
      {
        Variable.playerActionbarCooldownIsShowing.add(uuid);
        MessageUtil.sendTitle(livingEntity, "&c발사 불가!", ComponentUtil.translate("%s은(는) 발사 무기로 사용할 수 없는 아이템입니다", bow), 5, 80, 15);
        SoundPlay.playSound(livingEntity, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerShootBowAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (!(livingEntity instanceof Player) && NBTAPI.isRestricted(consumable, RestrictionType.NO_SHOT_AMMO) && consumable != null)
    {
      event.setCancelled(true);
      if (!Variable.playerShootBowAlertCooldown.contains(uuid))
      {
        Variable.playerActionbarCooldownIsShowing.add(uuid);
        MessageUtil.sendTitle(livingEntity, "&c발사 불가!", ComponentUtil.translate("%s은(는) 발사체로 사용할 수 없는 아이템입니다", consumable), 5, 80, 15);
        SoundPlay.playSound(livingEntity, Constant.ERROR_SOUND);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerShootBowAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    Entity projectile = event.getProjectile();
    // 피해 발산 효과
    if (livingEntity instanceof Player player && CustomEffectManager.hasEffect(player, CustomEffectType.DAMAGE_SPREAD))
    {
      CustomEffect customEffect = CustomEffectManager.getEffect(player, CustomEffectType.DAMAGE_SPREAD);
      if (customEffect instanceof DoubleCustomEffect doubleCustomEffect)
      {
        double bonusDamage = doubleCustomEffect.getDouble();
        Variable.DAMAGE_SPREAD_MAP.put(projectile.getUniqueId(), bonusDamage);
      }
      CustomEffectManager.removeEffect(player, CustomEffectType.DAMAGE_SPREAD);
    }
    if (ItemStackUtil.itemExists(bow))
    {
      bow = bow.clone();
      ItemLore.setItemLore(bow, event);
      if (ItemStackUtil.itemExists(consumable))
      {
        consumable = consumable.clone();
        ItemLore.setItemLore(consumable);
        Variable.projectile.put(projectile.getUniqueId(), consumable);
      }
      Variable.attackerAndWeapon.put(livingEntity.getUniqueId(), bow);
      if (bow.getType() == Material.CROSSBOW)
      {
        if (livingEntity instanceof Player player)
        {
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                  ItemStackUtil.updateInventory(player), 0L);
        }
      }
    }
    CustomMaterial bowCustomMaterial = CustomMaterial.itemStackOf(bow);
    if (bowCustomMaterial != null)
    {
      switch (bowCustomMaterial)
      {
        case BOW_CRIT ->
        {
          if (projectile instanceof AbstractArrow abstractArrow)
          {
            ItemStack consumableClone = consumable != null ? consumable.clone() : CustomMaterial.ARROW_CRIT.create();
            new NBTItem(consumableClone, true).setString("id", "arrow_crit");
            consumableClone.setAmount(1);
            Variable.entityShootBowConsumableMap.put(abstractArrow.getUniqueId(), ItemSerializer.serialize(consumableClone));
            if (consumable != null)
            {
              consumable.setType(consumableClone.getType());
              consumable.setItemMeta(consumableClone.getItemMeta());
            }
            if (Math.random() > 0.66)
            {
              abstractArrow.setDamage(abstractArrow.getDamage() * 2d);
            }
          }
          event.setConsumeItem(true);
        }
        case BOW_ENDER_PEARL ->
        {
          CustomMaterial consumableCustomMaterial = CustomMaterial.itemStackOf(consumable);
          if ((consumableCustomMaterial == null || consumableCustomMaterial == CustomMaterial.ARROW_INFINITE) && consumable != null && consumable.getType() == Material.ARROW)
          {
            Entity finalProjectile = projectile;
            event.setProjectile(livingEntity.getWorld().spawnEntity(projectile.getLocation(), EntityType.ENDER_PEARL, SpawnReason.ENDER_PEARL, entity ->
            {
              ((EnderPearl) entity).setShooter(livingEntity);
              entity.setVelocity(finalProjectile.getVelocity());
            }));
            projectile = event.getProjectile();
          }
        }
        case BOW_EXPLOSIVE ->
        {
          if (projectile instanceof AbstractArrow abstractArrow)
          {
            abstractArrow.addScoreboardTag("custom_material_arrow_explosive");
          }
          event.setConsumeItem(true);
        }
        case BOW_EXPLOSIVE_DESTRUCTION ->
        {
          if (projectile instanceof AbstractArrow abstractArrow)
          {
            abstractArrow.addScoreboardTag("custom_material_arrow_explosive_destruction");
          }
          event.setConsumeItem(true);
        }
        case BOW_FLAME ->
        {
          if (projectile instanceof AbstractArrow abstractArrow)
          {
            ItemStack consumableClone = consumable != null ? consumable.clone() : CustomMaterial.ARROW_FLAME.create();
            new NBTItem(consumableClone, true).setString("id", "arrow_flame");
            consumableClone.setAmount(1);
            Variable.entityShootBowConsumableMap.put(abstractArrow.getUniqueId(), ItemSerializer.serialize(consumableClone));
            if (consumable != null)
            {
              consumable.setType(consumableClone.getType());
              consumable.setItemMeta(consumableClone.getItemMeta());
            }
            abstractArrow.setFireTicks(2000);
          }
          event.setConsumeItem(true);
        }
        case BOW_INFINITE ->
        {
          event.setConsumeItem(false);
          if (livingEntity instanceof Player player)
          {
            player.updateInventory();
          }
        }
        case BOW_MOUNT ->
        {
          if (projectile instanceof AbstractArrow abstractArrow)
          {
            ItemStack consumableClone = consumable != null ? consumable.clone() : CustomMaterial.ARROW_MOUNT.create();
            new NBTItem(consumableClone, true).setString("id", "arrow_mount");
            consumableClone.setAmount(1);
            Variable.entityShootBowConsumableMap.put(abstractArrow.getUniqueId(), ItemSerializer.serialize(consumableClone));
            if (consumable != null)
            {
              consumable.setType(consumableClone.getType());
              consumable.setItemMeta(consumableClone.getItemMeta());
            }
            abstractArrow.addPassenger(livingEntity);
            abstractArrow.addScoreboardTag("custom_material_arrow_mount");
          }
          event.setConsumeItem(true);
        }
      }
    }
    CustomMaterial consumableCustomMaterial = CustomMaterial.itemStackOf(consumable);
    if (consumable != null && consumableCustomMaterial != null)
    {
      switch (consumableCustomMaterial)
      {
        case ARROW_CRIT ->
        {
          if (projectile instanceof AbstractArrow abstractArrow)
          {
            ItemStack consumableClone = consumable.clone();
            consumableClone.setAmount(1);
            Variable.entityShootBowConsumableMap.put(abstractArrow.getUniqueId(), ItemSerializer.serialize(consumableClone));
            if (Math.random() > 0.66)
            {
              abstractArrow.setDamage(abstractArrow.getDamage() * 2d);
            }
            event.setConsumeItem(true);
          }
        }
        case ARROW_EXPLOSIVE ->
        {
          if (projectile instanceof AbstractArrow abstractArrow)
          {
            abstractArrow.addScoreboardTag("custom_material_arrow_explosive");
          }
          event.setConsumeItem(true);
        }
        case ARROW_EXPLOSIVE_DESTRUCTION ->
        {
          if (projectile instanceof AbstractArrow abstractArrow)
          {
            abstractArrow.addScoreboardTag("custom_material_arrow_explosive_destruction");
          }
          event.setConsumeItem(true);
        }
        case ARROW_FLAME ->
        {
          if (projectile instanceof AbstractArrow abstractArrow)
          {
            ItemStack consumableClone = consumable.clone();
            consumableClone.setAmount(1);
            Variable.entityShootBowConsumableMap.put(abstractArrow.getUniqueId(), ItemSerializer.serialize(consumableClone));
            abstractArrow.setFireTicks(2000);
          }
          event.setConsumeItem(true);
        }
        case ARROW_INFINITE ->
        {
          if (projectile instanceof AbstractArrow abstractArrow)
          {
            abstractArrow.setPickupStatus(PickupStatus.CREATIVE_ONLY);
          }
          event.setConsumeItem(false);
          if (livingEntity instanceof Player player)
          {
            player.updateInventory();
          }
        }
        case ARROW_MOUNT ->
        {
          if (projectile instanceof AbstractArrow abstractArrow)
          {
            ItemStack consumableClone = consumable.clone();
            consumableClone.setAmount(1);
            Variable.entityShootBowConsumableMap.put(abstractArrow.getUniqueId(), ItemSerializer.serialize(consumableClone));
            abstractArrow.addPassenger(livingEntity);
            abstractArrow.addScoreboardTag("custom_material_arrow_mount");
          }
          event.setConsumeItem(true);
        }
        case ARROW_MOUNT_INFINITE ->
        {
          if (projectile instanceof AbstractArrow abstractArrow)
          {
            ItemStack consumableClone = consumable.clone();
            consumableClone.setAmount(1);
            Variable.entityShootBowConsumableMap.put(abstractArrow.getUniqueId(), ItemSerializer.serialize(consumableClone));
            abstractArrow.addPassenger(livingEntity);
          }
          event.setConsumeItem(true);
        }
        case ARROW_MOUNT_DISPOSAL ->
        {
          if (projectile instanceof AbstractArrow abstractArrow)
          {
            abstractArrow.addPassenger(livingEntity);
            abstractArrow.addScoreboardTag("custom_material_arrow_mount_disposal");
          }
          event.setConsumeItem(true);
        }
      }
    }
    int level = 0;
    if (CustomEffectManager.hasEffect(livingEntity, CustomEffectType.IDIOT_SHOOTER))
    {
      level = (CustomEffectManager.getEffect(livingEntity, CustomEffectType.IDIOT_SHOOTER).getAmplifier() + 1);
    }
    if (bow != null && bow.hasItemMeta() && bow.getItemMeta().hasEnchants())
    {
      level = Math.max(level, CustomEnchant.isEnabled() ? bow.getItemMeta().getEnchantLevel(CustomEnchant.IDIOT_SHOOTER) : 0);
    }
    if (level > 0)
    {
      double modifier = level / 10d;
      Vector vector = livingEntity.getLocation().getDirection();
      projectile.setVelocity(vector.add(new Vector(Math.random() * modifier - (modifier / 2d), Math.random() * modifier - (modifier / 2d), Math.random() * modifier - (modifier / 2d))));
    }
  }
}
