package com.jho5245.cucumbery.listeners.player.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import io.papermc.paper.event.player.PlayerStonecutterRecipeSelectEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.StonecutterInventory;

public class PlayerStoneCutterRecipeSelect implements Listener
{
  @EventHandler
  public void onPlayerStoneCutterRecipeSelect(PlayerStonecutterRecipeSelectEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }

    StonecutterInventory stonecutterInventory = event.getStonecutterInventory();
    Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
    {
      ItemStack item = stonecutterInventory.getResult();
      if (item != null)
      {
        ItemLore.setItemLore(item);
      }
    }, 0L);
  }
}
