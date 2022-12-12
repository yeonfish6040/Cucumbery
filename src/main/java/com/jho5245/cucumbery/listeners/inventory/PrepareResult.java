package com.jho5245.cucumbery.listeners.inventory;

import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class PrepareResult implements Listener
{
  public void onPrepareResult(PrepareResultEvent event)
  {
    HumanEntity humanEntity = event.getView().getPlayer();
    if (humanEntity instanceof Player player)
    {
      ItemStack result = event.getResult();
      if (!ItemStackUtil.itemExists(result))
      {
        return;
      }
      if (event.getInventory().getType() == InventoryType.ANVIL)
      {
        return;
      }
      if (Method.usingLoreFeature(player))
      {
        ItemLore.setItemLore(result, new ItemLoreView(player));
        event.setResult(result);
      }
    }
  }
}
