package com.jho5245.cucumbery.listeners.entity.item;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.inventory.ItemStack;

public class ItemMerge implements Listener
{
  @EventHandler
  @SuppressWarnings("all")
  public void onItemMerge(ItemMergeEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }

    Item entity = event.getEntity();
    Item target = event.getTarget();
    if (Cucumbery.config.getBoolean("glow-dropped-items"))
    {
      entity.setGlowing(true);
      target.setGlowing(true);
    }
    ItemStack item1 = entity.getItemStack(), item2 = target.getItemStack();
    if (NBTAPI.isRestricted(item1, RestrictionType.NO_MERGE))
    {
      event.setCancelled(true);
      return;
    }
    if (NBTAPI.isRestricted(item2, RestrictionType.NO_MERGE))
    {
      event.setCancelled(true);
      return;
    }
    Method.updateItem(target, item1.getAmount());
  }
}
