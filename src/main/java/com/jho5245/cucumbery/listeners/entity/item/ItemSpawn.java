package com.jho5245.cucumbery.listeners.entity.item;

import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

public class ItemSpawn implements Listener
{
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onItemSpawn(ItemSpawnEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Item entity = event.getEntity();
    Method.updateItem(entity);
    ItemStack itemStack = entity.getItemStack();
    NBTItem nbtItem = new NBTItem(itemStack);
    try
    {
      CustomMaterial customMaterial = CustomMaterial.valueOf(nbtItem.getString("id").toUpperCase());
      switch (customMaterial)
      {
        case DOEHAERIM_BABO, BAMIL_PABO, CUTE_SUGAR -> entity.setGravity(false);
      }
    }
    catch (Exception ignored)
    {

    }
  }
}
