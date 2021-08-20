package com.jho5245.cucumbery.listeners.player.item;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerItemMend implements Listener
{
  @EventHandler
  public void onPlayerItemMend(PlayerItemMendEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    ItemStack item = event.getItem();
    Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> ItemLore.setItemLore(item), 0L);
  }
}
