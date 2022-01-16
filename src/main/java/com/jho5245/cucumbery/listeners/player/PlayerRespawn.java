package com.jho5245.cucumbery.listeners.player;

import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawn implements Listener
{
  @EventHandler
  public void onPlayerRespawn(PlayerRespawnEvent event)
  {
    Player player = event.getPlayer();
    CustomEffectManager.addEffect(player, CustomEffectType.INVINCIBLE_RESPAWN);
  }
}
