package com.jho5245.cucumbery.listeners.block;

import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;

public class BlockBurn implements Listener
{
  @EventHandler
  public void onBlockBurn(BlockBurnEvent event)
  {
    Block block = event.getBlock();
    Location location = block.getLocation();
    YamlConfiguration blockPlaceData = Variable.blockPlaceData.get(location.getWorld().getName());
    if (blockPlaceData != null)
    {
      String dataString = blockPlaceData.getString(location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ());
      if (dataString != null)
      {
        blockPlaceData.set(location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ(), null);
        Variable.blockPlaceData.put(location.getWorld().getName(), blockPlaceData);
      }
    }
  }
}
