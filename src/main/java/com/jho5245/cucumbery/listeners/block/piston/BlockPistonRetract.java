package com.jho5245.cucumbery.listeners.block.piston;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.ItemSerializer;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
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
    Location pistonLocation = event.getBlock().getLocation();
    World world = pistonLocation.getWorld();
    YamlConfiguration blockPlaceData = Variable.blockPlaceData.get(world.getName());
    BlockPistonExtend.isPistonWorking = true;
    Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
            BlockPistonExtend.isPistonWorking = false, 2L);
    if (blockPlaceData == null)
    {
      return;
    }
    for (Block beforePushedBlock : event.getBlocks())
    {
      Location beforeLocation = beforePushedBlock.getLocation();
      int beforeX = beforeLocation.getBlockX(), beforeY = beforeLocation.getBlockY(), beforeZ = beforeLocation.getBlockZ();
      String beforeItemString = blockPlaceData.getString(beforeX + "_" + beforeY + "_" + beforeZ);
      if (beforeItemString == null)
      {
        continue;
      }
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
        int afterX = afterLocation.getBlockX(), afterY = afterLocation.getBlockY(), afterZ = afterLocation.getBlockZ();
        blockPlaceData.set(afterX + "_" + afterY + "_" + afterZ, beforeItemString);
      }, 0L);

      blockPlaceData.set(beforeX + "_" + beforeY + "_" + beforeZ, null);
    }
    Variable.blockPlaceData.put(world.getName(), blockPlaceData);
  }
}
