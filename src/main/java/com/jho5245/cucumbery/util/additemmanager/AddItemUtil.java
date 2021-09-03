package com.jho5245.cucumbery.util.additemmanager;

import org.bukkit.command.CommandSender;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AddItemUtil
{

  @NotNull
  public static Data addItemResult2(@NotNull CommandSender sender, InventoryHolder inventoryHolder, @NotNull ItemStack itemStack, int amount)
  {
    return addItemResult2(sender, new ArrayList<>(Collections.singletonList(inventoryHolder)), itemStack, amount);
  }
  @NotNull
  public static Data addItemResult2(@NotNull CommandSender sender, @NotNull Collection<? extends InventoryHolder> inventoryHolder, @NotNull ItemStack itemStack, int amount)
  {
    HashMap<InventoryHolder, Integer> hashMap = new HashMap<>();
    List<InventoryHolder> success = new ArrayList<>();
    List<InventoryHolder> failure = new ArrayList<>();
    for (InventoryHolder holder : inventoryHolder)
    {
      Collection<ItemStack> lostItem = holder.getInventory().addItem(itemStack).values();
      int lostAmount = lostItem.size() > 0 ? lostItem.iterator().next().getAmount() : 0;
      hashMap.put(holder, lostAmount);
      if (lostItem.size() == 0)
      {
        success.add(holder);
      }
      else
      {
        failure.add(holder);
      }
    }
    return new Data(sender, inventoryHolder, hashMap, success, failure, itemStack, amount);
  }
}
