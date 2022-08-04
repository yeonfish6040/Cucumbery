package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
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
                  Method.updateInventory(player), 0L);
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
      level = Math.max(level, bow.getEnchantmentLevel(CustomEnchant.IDIOT_SHOOTER));
    }
    if (level > 0)
    {
      double modifier = level / 10d;
      Vector vector = livingEntity.getLocation().getDirection();
      projectile.setVelocity(vector.add(new Vector(Math.random() * modifier - (modifier / 2d), Math.random() * modifier - (modifier / 2d), Math.random() * modifier - (modifier / 2d))));
    }
  }
}
