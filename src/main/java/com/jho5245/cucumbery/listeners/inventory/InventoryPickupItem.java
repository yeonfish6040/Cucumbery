package com.jho5245.cucumbery.listeners.inventory;

import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryPickupItem implements Listener
{
  @EventHandler
  public void onInventoryPickupItem(InventoryPickupItemEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Item item = event.getItem();
    ItemStack itemStack = item.getItemStack();
    if (NBTAPI.isRestricted(itemStack, RestrictionType.NO_STORE))
    {
      event.setCancelled(true);
      return;
    }
    Inventory inventory = event.getInventory();
    InventoryType inventoryType = inventory.getType();
    if (inventoryType == InventoryType.HOPPER && NBTAPI.isRestricted(itemStack, RestrictionType.NO_HOPPER))
    {
      event.setCancelled(true);
      return;
    }
    if (inventoryType == InventoryType.BREWING && NBTAPI.isRestricted(itemStack, RestrictionType.NO_BREW))
    {
      event.setCancelled(true);
    }
  }
}
