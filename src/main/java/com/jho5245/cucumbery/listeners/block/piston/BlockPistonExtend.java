package com.jho5245.cucumbery.listeners.block.piston;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.blockplacedata.BlockPlaceDataConfig;
import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import com.jho5245.cucumbery.util.storage.data.Constant;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPistonExtend implements Listener
{
  public static boolean isPistonWorking = false;

  @EventHandler
  public void onBlockPistonExtend(BlockPistonExtendEvent event)
  {
    BlockFace blockFace = event.getDirection();
    Location pistonLocation = event.getBlock().getLocation();
    World world = pistonLocation.getWorld();
    Boolean gameRuleValue = world.getGameRuleValue(GameRule.DO_TILE_DROPS);
    BlockPistonExtend.isPistonWorking = true;
    Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
            BlockPistonExtend.isPistonWorking = false, 3L);
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
      if (NBTAPI.isRestricted(dataItem, Constant.RestrictionType.NO_BLOCK_PISTON_MOVE) || NBTAPI.isRestricted(dataItem, Constant.RestrictionType.NO_BLOCK_PISTON_PUSH))
      {
        event.setCancelled(true);
        return;
      }
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        Block afterPushedBlock = beforePushedBlock.getRelative(blockFace);
        Location afterLocation = afterPushedBlock.getLocation();
        boolean removeItem = false;
        for (Item item : beforePushedBlock.getLocation().add(0.5d, 0.5d, 0.5d).getNearbyEntitiesByType(Item.class, 0.5d, 0.5d, 0.5d))
        {
          ItemStack itemStack = item.getItemStack();
          if (itemStack.getType() == dataItem.getType() && item.getPickupDelay() != 0)
          {
            itemStack = itemStack.clone();
            ItemLore.removeItemLore(itemStack);
            if (!itemStack.hasItemMeta())
            {
              item.remove();
              removeItem = true;
              if (gameRuleValue == null || gameRuleValue)
              {
                world.dropItemNaturally(beforeLocation.add(blockFace.getModX() / 4d, blockFace.getModY(), blockFace.getModZ() / 4d), dataItem);
              }
            }
          }
        }
        if (!removeItem)
        {
          BlockPlaceDataConfig blockPlaceDataConfig1 = BlockPlaceDataConfig.getInstance(afterLocation.getChunk());
          blockPlaceDataConfig1.set(afterLocation, beforeItemString);
          BlockPlaceDataConfig.spawnItemDisplay(afterLocation);
        }
      }, 0L);

      BlockPlaceDataConfig.removeData(beforeLocation);
    }
  }
}
