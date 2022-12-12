package com.jho5245.cucumbery.listeners.player.no_groups;

import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.Statistic;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.inventory.ItemStack;

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
    String pickUpMode = UserData.ITEM_PICKUP_MODE.getString(player);
    switch (pickUpMode)
    {
      case "sneak" -> {
        if (!player.isSneaking())
        {
          event.setCancelled(true);
          return;
        }
      }
      case "disabled" -> {
        event.setCancelled(true);
        return;
      }
    }
    AbstractArrow abstractArrow = event.getArrow();
    if (Method.usingLoreFeature(player))
    {
      event.setCancelled(true);
      ItemStack itemStack = abstractArrow.getItemStack();
      if (Variable.entityShootBowConsumableMap.containsKey(abstractArrow.getUniqueId()))
      {
        itemStack = ItemSerializer.deserialize(Variable.entityShootBowConsumableMap.get(abstractArrow.getUniqueId()));
        Variable.entityShootBowConsumableMap.remove(abstractArrow.getUniqueId());
      }
      player.getInventory().addItem(ItemLore.setItemLore(itemStack));
      player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.3F, (float) (2F - Math.random() * 0.1f));
      player.incrementStatistic(Statistic.PICKUP, itemStack.getType());
      abstractArrow.remove();
    }
/*    if (Method.usingLoreFeature(player))
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
            case ARROW, SPECTRAL_ARROW, TIPPED_ARROW ->
            {
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
            }
            default ->
            {
            }
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
    }*/
  }
}
