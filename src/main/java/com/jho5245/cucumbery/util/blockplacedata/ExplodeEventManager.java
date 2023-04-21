package com.jho5245.cucumbery.util.blockplacedata;

import com.jho5245.cucumbery.custom.customeffect.custom_mining.MiningManager;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExplodeEventManager
{
  public static void manage(int explodePower, float yield, @NotNull List<Block> blockList, @NotNull List<Block> removeList, @NotNull List<Block> removeListButNoBreak)
  {
    for (Block block : blockList)
    {
      int blockTier = MiningManager.getVanillaBlockTier(block.getType());
      Location location = block.getLocation();
      ItemStack itemStack = BlockPlaceDataConfig.getItem(location);
      if (ItemStackUtil.itemExists(itemStack))
      {
        // TNT로 폭발한 블록이 다른 커스텀 TNT일 경우 드롭하지 않고 폭발
        if (itemStack.getType() == Material.TNT)
        {
          continue;
        }
        removeList.add(block);
        NBTItem nbtItem = new NBTItem(itemStack);
        blockTier = nbtItem.hasTag(MiningManager.BLOCK_TIER) && nbtItem.getType(MiningManager.BLOCK_TIER) == NBTType.NBTTagInt ? nbtItem.getInteger(MiningManager.BLOCK_TIER) : blockTier;
        if (blockTier > explodePower)
        {
          removeListButNoBreak.add(block);
        }
        else
        {
          if (Math.random() <= yield)
          {
            location.getWorld().dropItemNaturally(location, itemStack);
          }
          BlockPlaceDataConfig.removeData(location);
        }
      }
      else if (blockTier > explodePower)
      {
        removeList.add(block);
        removeListButNoBreak.add(block);
      }
    }
  }
}
