package com.jho5245.cucumbery.util.additemmanager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AddItemUtil
{

  @NotNull
  public static Data addItemResult2(@NotNull CommandSender sender, @NotNull InventoryHolder inventoryHolder, @NotNull ItemStack itemStack, int amount)
  {
    return addItemResult2(sender, new ArrayList<>(Collections.singletonList(inventoryHolder)), itemStack, amount);
  }
  @NotNull
  public static Data addItemResult2(@NotNull CommandSender sender, @NotNull Collection<? extends InventoryHolder> inventoryHolder, @NotNull ItemStack itemStack, int amount)
  {
    itemStack = itemStack.clone();
    HashMap<UUID, Integer> hashMap = new HashMap<>();
    List<UUID> success = new ArrayList<>();
    List<UUID> failure = new ArrayList<>();
    List<UUID> uuids = new ArrayList<>();
    for (InventoryHolder holder : inventoryHolder)
    {
      if (holder instanceof Entity entity)
      {
        UUID uuid = entity.getUniqueId();
        uuids.add(uuid);
        Collection<ItemStack> lostItem = holder.getInventory().addItem(itemStack).values();
        int lostAmount = !lostItem.isEmpty() ? lostItem.iterator().next().getAmount() : 0;
        hashMap.put(uuid, lostAmount);
        if (lostItem.isEmpty() && lostAmount == 0)
        {
          success.add(uuid);
        }
        else
        {
          failure.add(uuid);
        }
      }
    }
    return new Data(sender, uuids, hashMap, success, failure, itemStack, amount);
  }
}
