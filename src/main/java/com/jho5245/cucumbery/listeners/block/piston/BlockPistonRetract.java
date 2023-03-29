package com.jho5245.cucumbery.listeners.block.piston;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import com.jho5245.cucumbery.util.storage.data.Constant;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPistonRetract implements Listener
{
  @EventHandler
  public void onBlockPistonRetract(BlockPistonRetractEvent event)
  {
    BlockFace blockFace = event.getDirection();
    BlockPistonExtend.isPistonWorking = true;
    Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
            BlockPistonExtend.isPistonWorking = false, 2L);
    for (Block beforePushedBlock : event.getBlocks())
    {
      Location beforeLocation = beforePushedBlock.getLocation();
      BlockPlaceDataConfig blockPlaceDataConfig = BlockPlaceDataConfig.getInstance(beforeLocation.getChunk());
      if (blockPlaceDataConfig == null)
      {
        continue;
      }
      String beforeItemString = blockPlaceDataConfig.getRawData(beforeLocation);
      ItemStack dataItem = ItemSerializer.deserialize(beforeItemString);
      if (NBTAPI.isRestricted(dataItem, Constant.RestrictionType.NO_BLOCK_PISTON_MOVE) || NBTAPI.isRestricted(dataItem, Constant.RestrictionType.NO_BLOCK_PISTON_PULL))
      {
        event.setCancelled(true);
        return;
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        Block afterPushedBlock = beforePushedBlock.getRelative(blockFace);
        Location afterLocation = afterPushedBlock.getLocation();
        BlockPlaceDataConfig blockPlaceDataConfig1 = BlockPlaceDataConfig.getInstance(afterLocation.getChunk());
        blockPlaceDataConfig1.set(afterLocation, beforeItemString);
        BlockPlaceDataConfig.spawnItemDisplay(afterLocation);
      }, 0L);
      BlockPlaceDataConfig.removeData(beforeLocation);
    }
  }
}
