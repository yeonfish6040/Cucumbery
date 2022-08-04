package com.jho5245.cucumbery.listeners.entity.item;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
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
    NBTItem nbtItem1 = new NBTItem(item1), nbtItem2 = new NBTItem(item2);
    String id1 = nbtItem1.getString("id") + "", id2 = nbtItem2.getString("id") + "";
    try
    {
      CustomMaterial customMaterial = CustomMaterial.valueOf(id1.toUpperCase());
      switch (customMaterial)
      {
        case CORE_GEMSTONE, CORE_GEMSTONE_EXPERIENCE, CORE_GEMSTONE_MIRROR, CORE_GEMSTONE_MITRA, CUTE_SUGAR -> {
          event.setCancelled(true);
          return;
        }
      }
    }
    catch (Exception ignored)
    {

    }
    try
    {
      CustomMaterial customMaterial = CustomMaterial.valueOf(id2.toUpperCase());
      switch (customMaterial)
      {
        case CORE_GEMSTONE, CORE_GEMSTONE_EXPERIENCE, CORE_GEMSTONE_MIRROR, CORE_GEMSTONE_MITRA, CUTE_SUGAR -> {
          event.setCancelled(true);
          return;
        }
      }
    }
    catch (Exception ignored)
    {

    }
    Method.updateItem(target, item1.getAmount());
  }
}
