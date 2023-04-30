package com.jho5245.cucumbery.listeners.entity.item;

import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.Method;
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

    CustomMaterial customMaterial = CustomMaterial.itemStackOf(item1);
    if (customMaterial != null)
    {
      switch (customMaterial)
      {
        case CORE_GEMSTONE, CORE_GEMSTONE_EXPERIENCE, CORE_GEMSTONE_MIRROR, CORE_GEMSTONE_MITRA, CUTE_SUGAR, RUNE_DESTRUCTION ->
        {
          event.setCancelled(true);
          return;
        }
      }
    }
    customMaterial = CustomMaterial.itemStackOf(item2);
    if (customMaterial != null)
    {
      switch (customMaterial)
      {
        case CORE_GEMSTONE, CORE_GEMSTONE_EXPERIENCE, CORE_GEMSTONE_MIRROR, CORE_GEMSTONE_MITRA, CUTE_SUGAR, RUNE_DESTRUCTION ->
        {
          event.setCancelled(true);
          return;
        }
      }
    }
    Method.updateItem(target, item1.getAmount());
  }
}
