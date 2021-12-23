package com.jho5245.cucumbery.listeners.player;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJump implements Listener
{
  @EventHandler
  public void onPlayerJump(PlayerJumpEvent event)
  {
    Player player = event.getPlayer();
    if (CustomEffectManager.hasEffect(player, CustomEffectType.CURSE_OF_JUMPING))
    {
      event.setCancelled(true);
    }
  }
}
