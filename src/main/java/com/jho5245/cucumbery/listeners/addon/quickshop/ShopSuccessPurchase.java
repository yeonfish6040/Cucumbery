package com.jho5245.cucumbery.listeners.addon.quickshop;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.Method2;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.maxgamer.quickshop.api.event.ShopSuccessPurchaseEvent;
import org.maxgamer.quickshop.api.shop.Shop;

import java.util.UUID;

public class ShopSuccessPurchase implements Listener
{
  @EventHandler
  public void onShopSuccessPurchase(ShopSuccessPurchaseEvent event)
  {
    Shop shop = event.getShop();
    UUID uuid = event.getPurchaser();
    Entity entity = Method2.getEntityAsync(uuid);
    if (entity instanceof Player player)
    {
      ItemStack itemStack = shop.getItem().clone();
      String expireDate = NBTAPI.getString(NBTAPI.getMainCompound(itemStack), CucumberyTag.EXPIRE_DATE_KEY);
      if (expireDate != null)
      {
        Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> {
          Inventory inventory = player.getInventory();
          for (int i = 0; i < inventory.getSize(); i++)
          {
            ItemStack item = inventory.getItem(i);
            if (ItemStackUtil.itemExists(item))
            {
              String expireDate2 = NBTAPI.getString(NBTAPI.getMainCompound(item), CucumberyTag.EXPIRE_DATE_KEY);
              if (expireDate2 != null && expireDate2.startsWith("~"))
              {
                Method.isTimeUp(item, expireDate2);
              }
            }
          }
        }, 0L);
      }
    }
  }
}
