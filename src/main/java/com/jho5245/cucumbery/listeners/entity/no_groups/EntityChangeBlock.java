package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;

public class EntityChangeBlock implements Listener
{
  @EventHandler
  public void onEntityChangeBlock(EntityChangeBlockEvent event)
  {
    Entity entity = event.getEntity();
    Block block = event.getBlock();
    Location location = block.getLocation();
    ItemStack itemStack = BlockPlaceDataConfig.getItem(location, Bukkit.getConsoleSender());
    if (NBTAPI.isRestricted(itemStack, RestrictionType.NO_BLOCK_ENTITY_CHANGE))
    {
      event.setCancelled(true);
      return;
    }

    // 화살이 TNT에게 불붙인 경우는 다른 이벤트에서 처리하므로 제거하지 않음
    if (!(entity instanceof Arrow && block.getType() == Material.TNT))
    {
      BlockPlaceDataConfig.removeData(location);
    }
  }
}
