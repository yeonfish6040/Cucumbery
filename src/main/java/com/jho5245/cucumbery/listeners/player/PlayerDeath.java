package com.jho5245.cucumbery.listeners.player;

import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerDeath implements Listener
{
  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }

    Player player = event.getEntity();

    if (UserData.GOD_MODE.getBoolean(player))
    {
      event.setCancelled(true);
    }
    boolean keepInv = UserData.SAVE_INVENTORY_UPON_DEATH.getBoolean(player.getUniqueId());
    boolean keepExp = UserData.SAVE_EXPERIENCE_UPON_DEATH.getBoolean(player.getUniqueId());
    if (keepInv)
    {
      event.setKeepInventory(true);
      event.getDrops().removeAll(event.getDrops());
    }
    if (keepExp)
    {
      event.setKeepLevel(true);
      event.setDroppedExp(0);
    }

    if (!event.getKeepInventory())
    {
      List<ItemStack> drops = event.getDrops();
      List<ItemStack> removals = new ArrayList<>();
      for (ItemStack drop : drops)
      {
        if (NBTAPI.isRestricted(player, drop, RestrictionType.NO_TRADE))
          removals.add(drop);
      }
      drops.removeAll(removals);
    }
  }
}
