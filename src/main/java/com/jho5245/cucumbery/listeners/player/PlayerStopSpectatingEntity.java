package com.jho5245.cucumbery.listeners.player;

import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerStopSpectatingEntity implements Listener
{
  @EventHandler
  public void onPlayerStopSpectatingEntity(PlayerStopSpectatingEntityEvent event)
  {
    Player player = event.getPlayer();
    if (CustomConfig.UserData.SHOW_SPECTATOR_TARGET_INFO_IN_ACTIONBAR.getBoolean(player))
    {
      MessageUtil.sendActionBar(player, " ");
    }
  }
}
