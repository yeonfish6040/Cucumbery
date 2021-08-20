package com.jho5245.cucumbery.listeners.inventory;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

public class Brew implements Listener
{
  @EventHandler
  public void onBrew(BrewEvent event)
  {
    BrewerInventory brewerInventory = event.getContents();
    Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
    {
      ItemStack item1 = brewerInventory.getItem(0);
      ItemStack item2 = brewerInventory.getItem(1);
      ItemStack item3 = brewerInventory.getItem(2);
      if (item1 != null)
      {
        ItemLore.setItemLore(item1);
      }
      if (item2 != null)
      {
        ItemLore.setItemLore(item2);
      }
      if (item3 != null)
      {
        ItemLore.setItemLore(item3);
      }
    }, 0L);
  }
}
