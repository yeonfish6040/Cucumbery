package com.jho5245.cucumbery.listeners.inventory;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class InventoryMoveItem implements Listener
{
  @EventHandler
  public void onInventoryMoveItem(InventoryMoveItemEvent event)
  {
    // 다른 플러그인에 의한 캔슬 체크
    if (event.isCancelled())
      return;
    ItemStack item = event.getItem();
    this.hopperItemMoveRestriction(event, item);
    // 아이템 이동 캔슬에 의한 체크 1번 더
    if (event.isCancelled())
      return;
    try
    {
      Inventory dest = event.getDestination();
      String worldName = Objects.requireNonNull(dest.getLocation()).getWorld().getName();
      if (Cucumbery.config.getBoolean("use-helpful-lore-feature") && !Cucumbery.config.getStringList("no-use-helpful-lore-feature-worlds").contains(worldName))
      {
        if (!ItemStackUtil.hasLore(item))
          ItemLore.setItemLore(item);
      }
      // mcmmo 사용중인 서버에선 양조기에 호퍼로 아이템을 넣을 때 기본 아이템 설명 삭제
      if (dest.getType() == InventoryType.BREWING && Cucumbery.using_mcMMO)
      {
        ItemLore.removeItemLore(item);
      }
    }
    catch (Exception ignored)
    {
    }
  }

  private void hopperItemMoveRestriction(InventoryMoveItemEvent event, ItemStack item)
  {
    if (!ItemStackUtil.hasLore(item))
      return;
    InventoryType type = event.getDestination().getType();
    if (NBTAPI.isRestricted(item, RestrictionType.NO_STORE))
    {
      event.setCancelled(true);
      return;
    }
    if (NBTAPI.isRestricted(item, RestrictionType.NO_SMELT))
    {
      if (type == InventoryType.FURNACE || type == InventoryType.BLAST_FURNACE || type == InventoryType.SMOKER)
      {
        event.setCancelled(true);
        return;
      }
    }
    if (NBTAPI.isRestricted(item, RestrictionType.NO_FURNACE))
    {
      if (type == InventoryType.FURNACE)
      {
        event.setCancelled(true);
        return;
      }
    }
    if (NBTAPI.isRestricted(item, RestrictionType.NO_BLAST_FURNACE))
    {
      if (type == InventoryType.BLAST_FURNACE)
      {
        event.setCancelled(true);
        return;
      }
    }
    if (NBTAPI.isRestricted(item, RestrictionType.NO_SMOKER))
    {
      if (type == InventoryType.SMOKER)
      {
        event.setCancelled(true);
        return;
      }
    }
    if (NBTAPI.isRestricted(item, RestrictionType.NO_BREW))
    {
      if (type == InventoryType.BREWING)
      {
        event.setCancelled(true);
        return;
      }
    }
    if (NBTAPI.isRestricted(item, RestrictionType.NO_DISPENSER))
    {
      if (type == InventoryType.DISPENSER)
      {
        event.setCancelled(true);
        return;
      }
    }
    if (NBTAPI.isRestricted(item, RestrictionType.NO_DROPPER))
    {
      if (type == InventoryType.DROPPER)
      {
        event.setCancelled(true);
        return;
      }
    }
    if (NBTAPI.isRestricted(item, RestrictionType.NO_HOPPER))
    {
      if (type == InventoryType.HOPPER)
      {
        event.setCancelled(true);
        return;
      }
    }
    if (NBTAPI.isRestricted(item, RestrictionType.NO_COMPOSTER))
    {
      Location from = event.getSource().getLocation();
      Block block = Objects.requireNonNull(from).getWorld().getBlockAt(from);
      if (block.getType() == Material.HOPPER || block.getType() == Material.DROPPER)
        event.setCancelled(true);
    }
  }
}
