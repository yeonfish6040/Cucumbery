package com.jho5245.cucumbery.listeners.player;

import com.destroystokyo.paper.event.player.PlayerRecipeBookClickEvent;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerRecipeBookClick implements Listener
{
  @EventHandler
  public void onPlayerRecipeBookClick(PlayerRecipeBookClickEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getPlayer();
    if (Method.usingLoreFeature(player))
    {
      PlayerInventory playerInventory = player.getInventory();
      for (int i = 0; i < 36; i++)
      {
        ItemStack item = playerInventory.getItem(i);
        if (item != null)
        {
          ItemLore.removeItemLore(item);
        }
      }
      event.setRecipe(event.getRecipe());
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
              Method.updateInventory(player), 0L);
    }
  }
}
