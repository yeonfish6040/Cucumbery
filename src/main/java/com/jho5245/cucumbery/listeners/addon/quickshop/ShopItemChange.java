package com.jho5245.cucumbery.listeners.addon.quickshop;

import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.maxgamer.quickshop.api.event.ShopItemChangeEvent;

public class ShopItemChange implements Listener
{
  @EventHandler
  public void onShopItemChange(ShopItemChangeEvent event)
  {
    Variable.shops.remove(event.getShop());
    Variable.shops.add(event.getShop());
  }
}
