package com.jho5245.cucumbery.util.plugin_support;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.maxgamer.quickshop.QuickShop;
import org.maxgamer.quickshop.api.shop.Shop;

import java.util.List;

public class QuickShopSupport
{
  public static int updateQuickShopItems()
  {
    List<Shop> shops = QuickShop.getInstance().getShopManager().getAllShops();
    boolean usefulLore = Cucumbery.config.getBoolean("use-helpful-lore-feature");
    List<String> worldList = Cucumbery.config.getStringList("no-use-helpful-lore-feature-worlds");
    for (Shop shop : shops)
    {
      World world = shop.getLocation().getWorld();
      boolean contains = worldList.contains(world.getName());
      ItemStack item = shop.getItem();
      if (usefulLore && !contains)
      {
        ItemLore.setItemLore(item);
      }
      else
      {
        ItemLore.removeItemLore(item);
      }
      shop.setItem(item);
    }
    return shops.size();
  }
}
