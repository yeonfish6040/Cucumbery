package com.jho5245.cucumbery.listeners.block;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.listeners.block.piston.BlockPistonExtend;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPhysics implements Listener
{
  @EventHandler
  public void onBlockPhysics(BlockPhysicsEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Block block = event.getBlock();//, sourceBlock = event.getSourceBlock();
    Boolean gameRuleValue = block.getWorld().getGameRuleValue(GameRule.DO_TILE_DROPS);
    boolean drop = gameRuleValue != null && gameRuleValue;
    BlockPlaceDataConfig blockPlaceDataConfig = BlockPlaceDataConfig.getInstance(block.getChunk());
    if (blockPlaceDataConfig == null)
    {
      return;
    }
    Material blockType = block.getType();
    if (blockType.isAir())
    {
      return;
    }
    switch (blockType)
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
      case WHEAT:
      case CARROTS:
      case POTATOES:
      case NETHER_WART:
        if (!event.getSourceBlock().getType().isAir())
        {
          return;
        }
        break;
    }
    Location bLoc = block.getLocation();
    String dataString = blockPlaceDataConfig.getRawData(bLoc);
    if (dataString == null)
    {
      return;
    }
    ItemStack dataItem = ItemSerializer.deserialize(dataString);
    Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
    {
      Material afterBlockType = block.getType();
      if (blockType != afterBlockType)
      {
        if (drop)
        {
          Material sourceBlockType = event.getSourceBlock().getType();
//          MessageUtil.broadcastDebug("changetype:" + event.getChangedType());
//          MessageUtil.broadcastDebug("srctype:" + sourceBlockType);
//          MessageUtil.broadcastDebug("ispiston:" + BlockPistonExtend.isPistonWorking);
//          if ((!BlockPistonExtend.isPistonWorking && sourceBlockType.isAir()) || (event.getChangedType() == dataItem.getType() && BlockPistonExtend.isPistonWorking))
//            if ((!BlockPistonExtend.isPistonWorking && sourceBlockType.isAir()) || (event.getChangedType() == dataItem.getType() && !sourceBlockType.isAir() && (BlockPistonExtend.isPistonWorking
//                    || sourceBlockType == Material.PISTON_HEAD || sourceBlockType == Material.MOVING_PISTON || sourceBlockType == Material.PISTON || sourceBlockType == Material.STICKY_PISTON)))
          if (!BlockPistonExtend.isPistonWorking || !sourceBlockType.isAir() || (blockType != Material.RAIL && blockType != Material.POWERED_RAIL && blockType != Material.DETECTOR_RAIL && blockType != Material.ACTIVATOR_RAIL))
          {
            for (Item item : bLoc.add(0.5d, 0.5d, 0.5d).getNearbyEntitiesByType(Item.class, 0.5d, 0.5d, 0.5d))
            {
              ItemStack itemStack = item.getItemStack();
              if (itemStack.getType() == dataItem.getType())
              {
                itemStack = itemStack.clone();
                ItemLore.removeItemLore(itemStack);
                if (!itemStack.hasItemMeta() && item.getPickupDelay() != 0)
                {
                  bLoc.getWorld().dropItemNaturally(bLoc.add(-0.5d, -0.5d, -0.5d), dataItem);
                  blockPlaceDataConfig.set(bLoc, null);
                  item.remove();
                }
              }
            }
          }
        }
      }
    }, 0L);
  }
}
