package com.jho5245.cucumbery.listeners.player.no_groups;

import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerRespawnEvent.RespawnFlag;

public class PlayerRespawn implements Listener
{
  @EventHandler
  public void onPlayerRespawn(PlayerRespawnEvent event)
  {
    Player player = event.getPlayer();
    if (!event.getRespawnFlags().contains(RespawnFlag.END_PORTAL))
    {
      CustomEffectManager.addEffect(player, CustomEffectType.INVINCIBLE_RESPAWN);
    }
  }
}