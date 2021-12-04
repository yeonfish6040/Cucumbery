package com.jho5245.cucumbery.listeners.player;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerPickupArrow implements Listener
{
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerPickupArrow(PlayerPickupArrowEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getPlayer();
    if (UserData.SPECTATOR_MODE.getBoolean(player))
    {
      event.setCancelled(true);
      return;
    }
    if (Method.usingLoreFeature(player))
    {
      Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        ItemStack item = null;
        PlayerInventory inv = player.getInventory();
        Outter:
        for (int i = 0; i < inv.getSize(); i++)
        {
          item = inv.getItem(i);
          if (item != null && !item.getItemMeta().hasLore())
          {
            Material type = item.getType();
            switch (type)
            {
              case ARROW:
              case SPECTRAL_ARROW:
              case TIPPED_ARROW:
                break Outter;
              default:
                break;
            }
          }
        }
        if (item != null && !item.getItemMeta().hasLore())
        {
          Material type = item.getType();
          switch (type)
          {
            case ARROW:
            case SPECTRAL_ARROW:
            case TIPPED_ARROW:
              ItemStack newItem = item.clone();
              inv.remove(item);
              ItemLore.setItemLore(newItem);
              ItemStack offHand = inv.getItemInOffHand();
              type = offHand.getType();
              switch (type)
              {
                case ARROW:
                case SPECTRAL_ARROW:
                case TIPPED_ARROW:
                  if (newItem.isSimilar(offHand))
                  {
                    int remainSpace = offHand.getMaxStackSize() - offHand.getAmount();
                    if (remainSpace > newItem.getAmount())
                    {
                      offHand.setAmount(offHand.getAmount() + newItem.getAmount());
                      newItem.setAmount(0);
                    }
                    else
                    {
                      offHand.setAmount(offHand.getMaxStackSize());
                      newItem.setAmount(newItem.getAmount() - remainSpace);
                    }
                  }
                  break;
                default:
                  break;
              }
              if (newItem.getAmount() > 0)
              {
                inv.addItem(newItem);
              }
              Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () -> Method.updateInventory(player), 0L);
              break;
            default:
              break;
          }
        }
        for (int i = 0; i < inv.getSize(); i++)
        {
          ItemStack trident = inv.getItem(i);
          if (trident != null && trident.getType() == Material.TRIDENT)
          {
            ItemLore.setItemLore(trident);
          }
        }
      }, 0L);
    }
  }
}
