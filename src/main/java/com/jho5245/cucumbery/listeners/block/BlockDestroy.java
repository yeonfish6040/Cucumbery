package com.jho5245.cucumbery.listeners.block;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Variable;
import de.tr7zw.changeme.nbtapi.NBTList;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
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
    switch (block.getType())
    {
      case SEA_PICKLE:
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
        break;
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
      YamlConfiguration blockPlaceData = Variable.blockPlaceData.get(location.getWorld().getName());
      if (event.willDrop() && drops)
      {
        if (blockPlaceData != null)
        {
          String dataString = blockPlaceData.getString(location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ());
          if (dataString != null)
          {
            ItemStack dataItem = ItemSerializer.deserialize(dataString);
            NBTList<String> extraTag = NBTAPI.getStringList(NBTAPI.getMainCompound(dataItem), CucumberyTag.EXTRA_TAGS_KEY);
            boolean noExpDropDueToForcePreverse = NBTAPI.arrayContainsValue(extraTag, Constant.ExtraTag.FORCE_PRESERVE_BLOCK_NBT);
            for (ItemStack drop : block.getDrops())
            {
              if (drop.getType() == dataItem.getType() || noExpDropDueToForcePreverse)
              {
                event.setCancelled(true);
                world.setGameRule(GameRule.DO_TILE_DROPS, false);
                block.breakNaturally(new ItemStack(Material.AIR), true);
                world.setGameRule(GameRule.DO_TILE_DROPS, true);
                Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                        block.setBlockData(Bukkit.createBlockData(Material.AIR)), 0L);
                world.dropItemNaturally(location, dataItem);
                break;
              }
            }
          }
        }
      }
      if (blockPlaceData != null)
      {
        blockPlaceData.set(location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ(), null);
        Variable.blockPlaceData.put(location.getWorld().getName(), blockPlaceData);
      }
    }
  }
}
