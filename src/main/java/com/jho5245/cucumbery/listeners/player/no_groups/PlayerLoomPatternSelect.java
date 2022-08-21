package com.jho5245.cucumbery.listeners.player.no_groups;

import io.papermc.paper.event.player.PlayerLoomPatternSelectEvent;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.LoomInventory;

public class PlayerLoomPatternSelect implements Listener
{
  @EventHandler
  public void onPlayerLoomPatternSelect(PlayerLoomPatternSelectEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    LoomInventory loomInventory = event.getLoomInventory();
    Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> {

      ItemStack result = loomInventory.getItem(3);
      if (result != null)
      {
        ItemLore.setItemLore(result);
      }
    }, 0L);
  }
}
