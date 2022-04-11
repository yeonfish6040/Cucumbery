package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.deathmessages.DeathManager;
import com.jho5245.cucumbery.util.storage.data.Variable;
import io.papermc.paper.event.entity.TameableDeathMessageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TameableDeathMessage implements Listener
{
  @EventHandler
  public void onTameableDeathMessage(TameableDeathMessageEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    if (Variable.deathMessages.getBoolean("death-messages.enable") && DeathManager.deathMessageApplicable(event.getEntity()))
    {
      event.setCancelled(true);
    }
  }
}
