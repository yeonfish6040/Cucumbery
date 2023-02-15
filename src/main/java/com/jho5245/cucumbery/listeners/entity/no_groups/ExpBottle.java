package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTType;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.inventory.ItemStack;

public class ExpBottle implements Listener
{
  @EventHandler
  public void onExpBottle(ExpBottleEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    ThrownExpBottle expBottle = event.getEntity();
    ItemStack itemStack = expBottle.getItem();
    if (ItemStackUtil.itemExists(itemStack))
    {
      NBTItem nbtItem = new NBTItem(itemStack);
      int minExp = 3, maxExp = 11;
      if (nbtItem.hasTag("MinExp") && nbtItem.getType("MinExp") == NBTType.NBTTagInt)
      {
        minExp = nbtItem.getInteger("MinExp");
      }
      if (nbtItem.hasTag("MaxExp") && nbtItem.getType("MaxExp") == NBTType.NBTTagInt)
      {
        maxExp = nbtItem.getInteger("MaxExp");
      }
      if (minExp > maxExp)
      {
        minExp = maxExp;
      }
      if (minExp > 0)
      {
        int finalExp = Method.random(minExp, maxExp);
        event.setExperience(finalExp);
      }
    }
  }
}
