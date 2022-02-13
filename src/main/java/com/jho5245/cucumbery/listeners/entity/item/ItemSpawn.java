package com.jho5245.cucumbery.listeners.entity.item;

import com.jho5245.cucumbery.util.no_groups.Method;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

public class ItemSpawn implements Listener
{
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onItemSpawn(ItemSpawnEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Item entity = event.getEntity();
    Method.updateItem(entity);
  }
}
