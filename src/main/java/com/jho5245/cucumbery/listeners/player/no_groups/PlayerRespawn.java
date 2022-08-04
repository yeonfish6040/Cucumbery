package com.jho5245.cucumbery.listeners.player.no_groups;

import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import com.jho5245.cucumbery.util.no_groups.Method;
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
    Method.updateInventory(player);
  }
}
