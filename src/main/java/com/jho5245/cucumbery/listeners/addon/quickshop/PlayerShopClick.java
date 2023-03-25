package com.jho5245.cucumbery.listeners.addon.quickshop;

import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.maxgamer.quickshop.api.event.PlayerShopClickEvent;
import org.maxgamer.quickshop.api.shop.Shop;

public class PlayerShopClick implements Listener
{
  @EventHandler
  public void onShopClick(PlayerShopClickEvent event)
  {
    Shop shop = event.getShop();
    Player player = event.getPlayer();
    shop.setItem(ItemLore.setItemLore(shop.getItem(), ItemLoreView.of(player)));
  }
}
