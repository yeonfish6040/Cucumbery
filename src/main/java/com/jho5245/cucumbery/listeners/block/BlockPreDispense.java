package com.jho5245.cucumbery.listeners.block;

import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.data.Constant;
import io.papermc.paper.event.block.BlockPreDispenseEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class BlockPreDispense implements Listener
{
  // Not registered Event
  @EventHandler
  public void onBlockPreDispense(BlockPreDispenseEvent event)
  {
    ItemStack item = event.getItemStack();
    if (NBTAPI.isRestricted(item, Constant.RestrictionType.NO_DISPENSER_DISPENSE))
    {
      MessageUtil.broadcastDebug("BlockPreDispenseEvent cancelled.");
      event.setCancelled(true);
    }
  }
}
