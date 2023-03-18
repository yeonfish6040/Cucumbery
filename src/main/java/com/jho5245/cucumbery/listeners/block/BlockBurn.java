package com.jho5245.cucumbery.listeners.block;

import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBurn implements Listener
{
  @EventHandler
  public void onBlockBurn(BlockBurnEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Block block = event.getBlock();
    Location location = block.getLocation();
    ItemStack itemStack = BlockPlaceDataConfig.getItem(location, Bukkit.getConsoleSender());
    if (NBTAPI.isRestricted(itemStack, RestrictionType.NO_BLOCK_BURN))
    {
      event.setCancelled(true);
      return;
    }
    BlockPlaceDataConfig.removeData(location);
  }
}
