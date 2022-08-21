package com.jho5245.cucumbery.listeners.player.no_groups;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectTypeRune;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

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
    if (CustomEffectManager.hasEffect(player, CustomEffectTypeRune.RUNE_EARTHQUAKE))
    {
      Vector vector = player.getVelocity();
      Bukkit.getScheduler().runTaskLater(Cucumbery.getPlugin(), () ->
      {
        if (player.isSneaking())
        {
          player.setVelocity(new Vector(vector.getX(), 1.5d, vector.getZ()));
        }
        else
        {
          player.setVelocity(new Vector(vector.getX(), 1d, vector.getZ()));
        }
      }, 0L);
    }
  }
}
