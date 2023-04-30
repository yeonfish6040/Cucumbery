package com.jho5245.cucumbery.listeners.player.interact;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeCustomMining;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.data.Permission;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.no_groups.SoundPlay;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerInteractEntity implements Listener
{
  @EventHandler
  public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
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
    CustomMaterial customMaterial = CustomMaterial.itemStackOf(item);
    Entity entity = event.getRightClicked();
    if (customMaterial != null)
    {
      switch (customMaterial)
      {
        case UNBINDING_SHEARS -> {
          event.setCancelled(true);
          return;
        }
        case TRACKER -> {
          if (entity instanceof ItemFrame)
          {
            return;
          }
          event.setCancelled(true);
          if (player.isSneaking() && !(entity instanceof Player))
          {
            MessageUtil.info(player, "%s을(를) 트래킹합니다", entity);
            NBTItem nbtItem = new NBTItem(item, true);
            nbtItem.setString("Tracking", entity.getUniqueId().toString());
            ItemStackUtil.updateInventory(player, item);
          }
        }
      }
    }
    Material material = item.getType();
    EntityType entityType = entity.getType();
    if (entity instanceof Parrot)
    {
      ItemStack cookie = ItemStackUtil.getPlayerUsingItem(player, Material.COOKIE);
      if (ItemStackUtil.itemExists(cookie))
      {
        cookie = cookie.clone();
        Variable.attackerAndWeaponString.put(player.getUniqueId(), ItemSerializer.serialize(cookie));
      }
    }
    if (Variable.scrollReinforcing.contains(uuid))
    {
      event.setCancelled(true);
      return;
    }
    if (!Cucumbery.config.getBoolean("grant-default-permission-to-players") && !Permission.EVENT_INTERACT_ENTITY.has(player))
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
    if (NBTAPI.isRestricted(player, item, RestrictionType.NO_USE) || NBTAPI.isRestricted(player, item, RestrictionType.NO_USE_ENTITY))
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
    if (NBTAPI.isRestricted(player, item, RestrictionType.NO_TRADE) || NBTAPI.isRestricted(player, item, RestrictionType.NO_STORE) || NBTAPI.isRestricted(player, item, RestrictionType.NO_ITEM_FRAME))
    {
      if (entityType == EntityType.ITEM_FRAME)
      {
        ItemFrame itemFrame = (ItemFrame) entity;
        if (!ItemStackUtil.itemExists(itemFrame.getItem()))
        {
          event.setCancelled(true);
          if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerInteractAtEntityRestrictedItemAlertCooldown.contains(uuid))
          {
            SoundPlay.playSound(player, Constant.ERROR_SOUND);
            if (NBTAPI.isRestricted(player, item, RestrictionType.NO_STORE))
            {
              MessageUtil.sendTitle(event.getPlayer(), "&c사용 불가!", "&r보관 불가인 아이템을 액자에 걸 수 없습니다", 5, 80, 15);
            }
            else if (NBTAPI.isRestricted(player, item, RestrictionType.NO_TRADE))
            {
              MessageUtil.sendTitle(event.getPlayer(), "&c사용 불가!", "&r캐릭터 귀속인 아이템을 액자에 걸 수 없습니다", 5, 80, 15);
            }
            else if (NBTAPI.isRestricted(player, item, RestrictionType.NO_ITEM_FRAME))
            {
              MessageUtil.sendTitle(event.getPlayer(), "&c사용 불가!", "&r액자에 사용할 수 없는 아이템입니다", 5, 80, 15);
            }
            Variable.playerInteractAtEntityRestrictedItemAlertCooldown.add(uuid);
            Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerInteractAtEntityRestrictedItemAlertCooldown.remove(uuid), 100L);
          }
        }
      }
    }
    if (NBTAPI.isRestricted(player, item, RestrictionType.NO_SMELT))
    {
      if (entityType == EntityType.MINECART_FURNACE)
      {
        event.setCancelled(true);
        if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerInteractAtEntityRestrictedItemAlertCooldown.contains(uuid))
        {
          SoundPlay.playSound(player, Constant.ERROR_SOUND);
          MessageUtil.sendTitle(event.getPlayer(), "&c사용 불가!", "&r화로가 실린 광산 수레에 사용할 수 없는 아이템입니다", 5, 80, 15);
          Variable.playerInteractAtEntityRestrictedItemAlertCooldown.add(uuid);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerInteractAtEntityRestrictedItemAlertCooldown.remove(uuid), 100L);
        }
      }
    }
    if (ItemStackUtil.itemExists(item) && CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE) && player.getGameMode() != GameMode.CREATIVE
            && !CustomEffectManager.hasEffect(player, CustomEffectTypeCustomMining.CUSTOM_MINING_SPEED_MODE_2_NO_RESTORE))
    {
      event.setCancelled(true);
      return;
    }
    if ((entityType == EntityType.MUSHROOM_COW) && material == Material.BOWL)
    {
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> ItemStackUtil.updateInventory(player), 0L);
    }

    if (entityType == EntityType.COW || entityType == EntityType.MUSHROOM_COW && material == Material.BUCKET)
    {
      if (player.getGameMode() != GameMode.CREATIVE && NBTAPI.arrayContainsValue(NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.EXTRA_TAGS_KEY),
              Constant.ExtraTag.INFINITE.toString()))
      {
        event.setCancelled(true);
        if (!Permission.EVENT_ERROR_HIDE.has(player) && !Variable.playerInteractAtEntityRestrictedItemAlertCooldown.contains(uuid))
        {
          SoundPlay.playSound(player, Constant.ERROR_SOUND);
          MessageUtil.sendTitle(player, "&c사용 불가!", ComponentUtil.translate("%s은(는) %s에게 사용할 수 없습니다", item, entity), 5, 80, 15);
          Variable.playerInteractAtEntityRestrictedItemAlertCooldown.add(uuid);
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Variable.playerInteractAtEntityRestrictedItemAlertCooldown.remove(uuid), 100L);
        }
        return;
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> ItemStackUtil.updateInventory(player), 0L);
    }
  }
}
