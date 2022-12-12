package com.jho5245.cucumbery.listeners.addon.ultimatetimber;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.songoda.ultimatetimber.events.TreeFellEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TreeFell implements Listener
{
  @EventHandler
  public void onTreeFell(TreeFellEvent event)
  {
    Player player = event.getPlayer();
    Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Method.updateInventory(player), 1L);
  }
}
