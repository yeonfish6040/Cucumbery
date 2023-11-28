package com.jho5245.cucumbery.listeners.player.item;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerItemMend implements Listener
{
  @EventHandler
  public void onPlayerItemMend(PlayerItemMendEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    ItemStack item = event.getItem();
    NBTItem nbtItem = new NBTItem(item);
    NBTCompound itemTag = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
    NBTCompound duraTag = NBTAPI.getCompound(itemTag, CucumberyTag.CUSTOM_DURABILITY_KEY);
    if (duraTag != null)
    {
      long maxDura = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY);
      long curDura = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY);
      if (maxDura != 0)
      {
        curDura -= event.getRepairAmount();
        if (curDura < 0)
        {
          curDura = 0;
        }
        duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, curDura);
        duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY, maxDura);
        ItemStack itemClone = nbtItem.getItem();
        ItemMeta itemMeta = itemClone.getItemMeta();
        item.setItemMeta(itemMeta);
      }
    }
    Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> ItemLore.setItemLore(item, ItemLoreView.of(event.getPlayer())), 0L);
  }
}
