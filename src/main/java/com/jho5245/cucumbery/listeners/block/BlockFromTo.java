package com.jho5245.cucumbery.listeners.block;

import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Variable;
import de.tr7zw.changeme.nbtapi.NBTList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.inventory.ItemStack;

public class BlockFromTo implements Listener
{
  @EventHandler
  public void onBlockFromTo(BlockFromToEvent event)
  {
    Block fromBlock = event.getBlock();
    Block toBlock = event.getToBlock();

    if ((fromBlock.getType() == Material.WATER || fromBlock.getType() == Material.LAVA) && !toBlock.getType().isAir())
    {
      Location location = toBlock.getLocation();
      YamlConfiguration yamlConfiguration = Variable.blockPlaceData.get(location.getWorld().getName());
      if (yamlConfiguration != null)
      {
        String itemData = yamlConfiguration.getString(location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ());
        if (itemData != null)
        {
          ItemStack item = ItemSerializer.deserialize(itemData);
          NBTList<String> extraTag = NBTAPI.getStringList(NBTAPI.getMainCompound(item), CucumberyTag.EXTRA_TAGS_KEY);
          for (ItemStack drop : toBlock.getDrops())
          {
            if (drop.getType() == item.getType() || NBTAPI.arrayContainsValue(extraTag, Constant.ExtraTag.FORCE_PRESERVE_BLOCK_NBT))
            {
              event.setCancelled(true);
              toBlock.setBlockData(fromBlock.getBlockData());
              toBlock.getWorld().dropItemNaturally(toBlock.getLocation(), item);
              break;
            }
          }
        }
        yamlConfiguration.set(location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ(), null);
        Variable.blockPlaceData.put(location.getWorld().getName(), yamlConfiguration);
      }
    }
  }
}
