package com.jho5245.cucumbery.listeners.addon.quickshop;

import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.maxgamer.quickshop.api.event.ShopDeleteEvent;

public class ShopDelete implements Listener
{
  @EventHandler
  public void onShopDelete(ShopDeleteEvent event)
  {
    Variable.shops.remove(event.getShop());
  }
}
