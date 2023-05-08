package com.jho5245.cucumbery.listeners.inventory;

import com.gmail.nossr50.api.ExperienceAPI;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.itemlore.ItemLoreView;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Brew implements Listener
{
  @EventHandler
  public void onBrew(BrewEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    BrewerInventory brewerInventory = event.getContents();
    Location brewerInventoryLocation = brewerInventory.getLocation();
    Player player = !brewerInventory.getViewers().isEmpty() && brewerInventory.getViewers().get(0) instanceof Player p ? p : null;
    Bukkit.getServer().getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
    {
      Object param = player != null ? ItemLoreView.of(player) : null;
      ItemStack item1After = brewerInventory.getItem(0);
      ItemStack item2After = brewerInventory.getItem(1);
      ItemStack item3After = brewerInventory.getItem(2);
      if (item1After != null)
      {
        ItemLore.setItemLore(item1After, param);
      }
      if (item2After != null)
      {
        ItemLore.setItemLore(item2After, param);
      }
      if (item3After != null)
      {
        ItemLore.setItemLore(item3After, param);
      }
    }, 0L);

    if (Cucumbery.using_mcMMO && brewerInventoryLocation != null)
    {
      Player brewerPlayer = player;
      if (brewerPlayer == null)
      {
        UUID uuid = InventoryOpen.mcMMOBrewingStandMap.get(brewerInventoryLocation.toString());
        if (uuid != null && Bukkit.getPlayer(uuid) != null)
        {
          brewerPlayer = Bukkit.getPlayer(uuid);
        }
      }
      if (brewerPlayer != null)
      {
        ExperienceAPI.addXP(brewerPlayer, "ALCHEMY", 100, "UNKNOWN");
        InventoryOpen.mcMMOBrewingStandMap.remove(brewerInventoryLocation.toString());
      }
    }
  }
}
