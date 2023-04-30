package com.jho5245.cucumbery.listeners.inventory;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class CraftItem implements Listener
{
  @EventHandler
  public void onCraftItem(CraftItemEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    HumanEntity humanEntity = event.getView().getPlayer();
    if (humanEntity instanceof Player player)
    {
      // 아이템을 제작하고 남은 아이템(양동이, 유리병 등)의 아이템 설명 업데이트
      InventoryType inventoryType = event.getInventory().getType();
      if (inventoryType == InventoryType.WORKBENCH || inventoryType == InventoryType.CRAFTING)
      {
        boolean hasRemaingItem = false;
        for (int i = 0; i < event.getInventory().getSize(); i++)
        {
          ItemStack it = event.getInventory().getItem(i);
          if (it != null && it.getType().getCraftingRemainingItem() != null)
          {
            hasRemaingItem = true;
            int j = i;
            Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
            {
              ItemStack i2 = event.getInventory().getItem(j);
              if (i2 != null)
              {
                ItemStack newItem = new ItemStack(i2.getType());
                ItemLore.setItemLore(newItem, new ItemLoreView(player));
                event.getInventory().setItem(j, newItem);
              }
            }, 0L);
          }
        }
        if (hasRemaingItem)
        {
          Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
                  ItemStackUtil.updateInventory(player), 0L);
        }
      }
    }
  }
}
