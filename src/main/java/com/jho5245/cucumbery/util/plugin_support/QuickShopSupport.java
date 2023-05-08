package com.jho5245.cucumbery.util.plugin_support;

import com.jho5245.cucumbery.util.itemlore.ItemLore;
import org.bukkit.inventory.ItemStack;
import org.maxgamer.quickshop.QuickShop;
import org.maxgamer.quickshop.api.shop.Shop;

import java.util.List;

public class QuickShopSupport
{
  public static int updateQuickShopItems()
  {
    List<Shop> shops = QuickShop.getInstance().getShopManager().getAllShops();
    for (Shop shop : shops)
    {
      ItemStack item = shop.getItem();
      ItemLore.setItemLore(item);
      shop.setItem(item);
    }
    return shops.size();
  }
}
