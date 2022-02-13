package com.jho5245.cucumbery.listeners.player.no_groups;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
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
    if (CustomEffectManager.hasEffect(player, CustomEffectType.VAR_PODAGRA) && !player.isSneaking())
    {
      player.damage(1);
      player.setNoDamageTicks(0);
    }
  }
}
