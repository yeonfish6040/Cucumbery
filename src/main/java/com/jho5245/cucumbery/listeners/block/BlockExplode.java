package com.jho5245.cucumbery.listeners.block;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.blockplacedata.ExplodeEventManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class BlockExplode implements Listener
{
  @EventHandler
  public void onBlockExplode(BlockExplodeEvent event)
  {
    this.respawnBlockRegionProjection(event);
    this.blockPlaceData(event);
  }

  public void blockPlaceData(BlockExplodeEvent event)
  {
    int defaultExplosionPower = Cucumbery.config.getInt("custom-mining.default-explosion-power.default");
    BlockState blockState = event.getExplodedBlockState();
    if (blockState != null)
    {
      Material blockType = blockState.getType();
      if (Tag.BEDS.isTagged(blockType))
      {
        defaultExplosionPower = Cucumbery.config.getInt("custom-mining.default-explosion-power.BEDS");
      }
      else
      {

        defaultExplosionPower = Cucumbery.config.getInt("custom-mining.default-explosion-power." + blockType);
      }
    }
    List<Block> removeList = new ArrayList<>();
    List<Block> removeListButNobreak = new ArrayList<>();
    ExplodeEventManager.manage(defaultExplosionPower, event.getYield(), event.blockList(), removeList, removeListButNobreak);
    event.blockList().removeAll(removeList);
    removeList.removeAll(removeListButNobreak);
    for (Block block : removeList)
    {
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
              block.setBlockData(Bukkit.createBlockData(Material.AIR)), 0L);
    }
  }

  private void respawnBlockRegionProjection(BlockExplodeEvent event)
  {
    FileConfiguration config = Cucumbery.config;
    if (!config.getBoolean("enable-respawn-block-protection"))
    {
      return;
    }
    List<String> keyList = config.getStringList("respawn-block-protection-coords");
    if (keyList.isEmpty())
    {
      return;
    }
    List<Block> blocks = new ArrayList<>();
    for (String key : keyList)
    {
      try
      {
        String[] splitter = key.split(",");
        String worldString = splitter[0];
        if (splitter.length == 1 && event.getBlock().getWorld().getName().equals(worldString))
        {
          blocks.addAll(event.blockList());
        }
        else
        {
          String xString = splitter[1];
          String yString = splitter[2];
          String zString = splitter[3];
          World world = Bukkit.getServer().getWorld(worldString);
          if (world == null)
          {
            continue;
          }
          double xFrom = Double.parseDouble(xString.split("~")[0]);
          double xTo = Double.parseDouble(xString.split("~")[1]);
          double yFrom = Double.parseDouble(yString.split("~")[0]);
          double yTo = Double.parseDouble(yString.split("~")[1]);
          double zFrom = Double.parseDouble(zString.split("~")[0]);
          double zTo = Double.parseDouble(zString.split("~")[1]);
          for (int i = 0; i < event.blockList().size(); i++)
          {
            Block block = event.blockList().get(i);
            Location loc = block.getLocation();
            World locWorld = loc.getWorld();
            int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();

            if (locWorld.getName().equals(world.getName()) && x >= xFrom && x <= xTo && y >= yFrom && y <= yTo && z >= zFrom && z <= zTo)
            {
              blocks.add(block);
            }
          }
        }
      }
      catch (Exception ignored)
      {
      }
    }
    event.blockList().removeAll(blocks);
  }
}
