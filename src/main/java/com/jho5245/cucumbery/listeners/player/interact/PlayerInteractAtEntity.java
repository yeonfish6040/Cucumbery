package com.jho5245.cucumbery.listeners.player.interact;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerInteractAtEntity implements Listener
{
  @EventHandler
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }

    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();
    // 아이템 섭취 사용에서 사라지지 않을 경우 아이템 소실 방지를 위한 쿨타임
    if (Variable.playerItemConsumeCauseSwapCooldown.contains(uuid))
    {
      event.setCancelled(true);
      return;
    }
    EquipmentSlot hand = event.getHand();
    ItemStack item = player.getInventory().getItem(hand);
    Entity entity = event.getRightClicked();
    EntityType entityType = entity.getType();
    if (Variable.scrollReinforcing.contains(uuid))
    {
      event.setCancelled(true);
      return;
    }
    if (ItemStackUtil.itemExists(item) && CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE) && player.getGameMode() != GameMode.CREATIVE
            && !CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_2_NO_RESTORE))
    {
      event.setCancelled(true);
      return;
    }
    if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Permission.EVENT_INTERACT_AT_ENTITY.has(player))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerInteractAtEntityAlertCooldown.contains(uuid))
      {
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        MessageUtil.sendTitle(event.getPlayer(), "&c행동 불가!", "&r개체에게 상호작용할 권한이 없습니다", 5, 80, 15);
        Variable.playerInteractAtEntityAlertCooldown.add(uuid);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerInteractAtEntityAlertCooldown.remove(uuid), 100L);
      }
      return;
    }
    if (NBTAPI.isRestricted(player, item, RestrictionType.NO_USE))
    {
      event.setCancelled(true);
      if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerInteractAtEntityRestrictedItemAlertCooldown.contains(uuid))
      {
        SoundPlay.playSound(player, Constant.ERROR_SOUND);
        MessageUtil.sendTitle(event.getPlayer(), "&c사용 불가!", "&r사용할 수 없는 아이템입니다", 5, 80, 15);
        Variable.playerInteractAtEntityRestrictedItemAlertCooldown.add(uuid);
        Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerInteractAtEntityRestrictedItemAlertCooldown.remove(uuid), 100L);
      }
    }
    if (NBTAPI.isRestricted(player, item, RestrictionType.NO_TRADE) || NBTAPI.isRestricted(player, item, RestrictionType.NO_STORE)
            || NBTAPI.isRestricted(player, item, RestrictionType.NO_ARMOR_STAND))
    {
      if (entityType == EntityType.ARMOR_STAND)
      {
        event.setCancelled(true);
        if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerInteractAtEntityRestrictedItemAlertCooldown.contains(uuid))
        {
          SoundPlay.playSound(player, Constant.ERROR_SOUND);
          if (NBTAPI.isRestricted(player, item, RestrictionType.NO_STORE))
            MessageUtil.sendTitle(event.getPlayer(), "&c사용 불가!", "&r보관 불가인 아이템을 갑옷 거치대에 걸 수 없습니다", 5, 80, 15);
          else if (NBTAPI.isRestricted(player, item, RestrictionType.NO_TRADE))
            MessageUtil.sendTitle(event.getPlayer(), "&c사용 불가!", "&r캐릭터 귀속인 아이템을 갑옷 거치대에 걸 수 없습니다", 5, 80, 15);
          else if (NBTAPI.isRestricted(player, item, RestrictionType.NO_ARMOR_STAND))
            MessageUtil.sendTitle(event.getPlayer(), "&c사용 불가!", "&r갑옷 거치대에 사용할 수 없는 아이템입니다", 5, 80, 15);
          Variable.playerInteractAtEntityRestrictedItemAlertCooldown.add(uuid);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerInteractAtEntityRestrictedItemAlertCooldown.remove(uuid), 100L);
        }
      }
    }
    if (entity.getType() == EntityType.ARMOR_STAND)
    {
      ItemStackUtil.updateInventory(player);
    }
  }
}
