package com.jho5245.cucumbery.listeners.addon.quickshop;

import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.maxgamer.quickshop.api.event.ShopClickEvent;
import org.maxgamer.quickshop.api.shop.Shop;

import java.util.ArrayList;
import java.util.List;

public class ShopClick implements Listener
{
  @EventHandler
  public void onShopClick(ShopClickEvent event)
  {
    Shop shop = event.getShop();
    Location location = shop.getLocation();
    List<Player> players = new ArrayList<>(location.getNearbyPlayers(5d));
    if (!players.isEmpty())
    {
      shop.setItem(ItemLore.setItemLore(shop.getItem(), ItemLoreView.of(players.get(0))));
    }
  }
}
