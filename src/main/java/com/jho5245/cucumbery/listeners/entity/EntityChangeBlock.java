package com.jho5245.cucumbery.listeners.entity;

import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EntityChangeBlock implements Listener
{
  @EventHandler
  public void onEntityChangeBlock(EntityChangeBlockEvent event)
  {
    Block block = event.getBlock();
    Location location = block.getLocation();
    YamlConfiguration yamlConfiguration = Variable.blockPlaceData.get(location.getWorld().getName());
    if (yamlConfiguration != null)
    {
      yamlConfiguration.set(location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ(), null);
      Variable.blockPlaceData.put(location.getWorld().getName(), yamlConfiguration);
    }
  }
}
