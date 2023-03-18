package com.jho5245.cucumbery.listeners.block;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class BlockDestroy implements Listener
{
  @EventHandler
  public void onBlockDestroy(BlockDestroyEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Block block = event.getBlock();
    if (block.getType() == Material.SEA_PICKLE)
    {
      try
      {
        String nbt = block.getBlockData().getAsString();
        int amount = Integer.parseInt(nbt.split("pickles=")[1].split(",waterlogged=")[0]);
        if (amount > 1)
        {
          return;
        }
      }
      catch (Exception e)
      {
        return;
      }
    }
    Location location = block.getLocation();
    World world = location.getWorld();
    boolean drops = false;
    if (world != null)
    {
      Boolean gameRuleValue = world.getGameRuleValue(GameRule.DO_TILE_DROPS);
      if (gameRuleValue != null && gameRuleValue)
      {
        drops = true;
      }
      BlockPlaceDataConfig blockPlaceDataConfig = BlockPlaceDataConfig.getInstance(location.getChunk());
      if (event.willDrop() && drops)
      {
        if (blockPlaceDataConfig != null)
        {
          String dataString = blockPlaceDataConfig.getRawData(location);
          if (dataString != null)
          {
            ItemStack dataItem = ItemSerializer.deserialize(dataString);
            event.setCancelled(true);
            world.setGameRule(GameRule.DO_TILE_DROPS, false);
            block.breakNaturally(new ItemStack(Material.AIR), true);
            world.setGameRule(GameRule.DO_TILE_DROPS, true);
            Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                    block.setBlockData(Bukkit.createBlockData(Material.AIR)), 0L);
            world.dropItemNaturally(location, dataItem);
          }
          blockPlaceDataConfig.set(location, null);
        }
      }
    }
  }
}
