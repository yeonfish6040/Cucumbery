package com.jho5245.cucumbery.listeners.block;

import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.inventory.ItemStack;

public class BlockFromTo implements Listener
{
  @EventHandler
  public void onBlockFromTo(BlockFromToEvent event)
  {
    Block fromBlock = event.getBlock();
    Block toBlock = event.getToBlock();
    if ((fromBlock.getType() == Material.WATER || fromBlock.getType() == Material.LAVA) && !toBlock.getType().isAir())
    {
      Location location = toBlock.getLocation();
      BlockPlaceDataConfig blockPlaceDataConfig = BlockPlaceDataConfig.getInstance(location.getChunk());
      if (blockPlaceDataConfig != null)
      {
        String itemData = blockPlaceDataConfig.getRawData(location);
        if (itemData != null)
        {
          ItemStack item = ItemSerializer.deserialize(itemData);
          for (ItemStack drop : toBlock.getDrops())
          {
            if (drop.getType() == item.getType())
            {
              event.setCancelled(true);
              toBlock.setBlockData(fromBlock.getBlockData());
              toBlock.getWorld().dropItemNaturally(toBlock.getLocation(), item);
              break;
            }
          }
        }
        blockPlaceDataConfig.set(location, null);
      }
    }
  }
}
