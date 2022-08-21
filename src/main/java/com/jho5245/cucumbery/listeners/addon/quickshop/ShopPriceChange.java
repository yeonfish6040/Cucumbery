package com.jho5245.cucumbery.listeners.addon.quickshop;

import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.maxgamer.quickshop.api.event.ShopPriceChangeEvent;

public class ShopPriceChange implements Listener
{
  @EventHandler
  public void onShopPriceChange(ShopPriceChangeEvent event)
  {
    Variable.shops.remove(event.getShop());
    Variable.shops.add(event.getShop());
  }
}
