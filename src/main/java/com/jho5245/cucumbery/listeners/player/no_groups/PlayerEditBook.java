package com.jho5245.cucumbery.listeners.player.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;

public class PlayerEditBook implements Listener
{
  @EventHandler
  public void onPlayerEditBook(PlayerEditBookEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getPlayer();
    Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
    {
      ItemLore.setItemLore(player.getInventory().getItemInMainHand());
      ItemLore.setItemLore(player.getInventory().getItemInOffHand());
    }, 0L);
  }
}
