package com.jho5245.cucumbery.listeners.addon.gsit;

import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import dev.geco.gsit.api.event.PreEntitySitEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PreEntitySit implements Listener
{
  @EventHandler
  public void onPreEntitySit(PreEntitySitEvent event)
  {
    LivingEntity livingEntity = event.getEntity();
    if (livingEntity instanceof Player player)
    {
      if (UserData.GSIT_EXEMPT.getBoolean(player))
      {
        event.setCancelled(true);
      }
    }
  }
}
