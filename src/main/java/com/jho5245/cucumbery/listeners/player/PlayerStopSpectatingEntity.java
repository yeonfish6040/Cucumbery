package com.jho5245.cucumbery.listeners.player;

import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;
import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collection;

public class PlayerStopSpectatingEntity implements Listener
{
  @EventHandler
  public void onPlayerStopSpectatingEntity(PlayerStopSpectatingEntityEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Player player = event.getPlayer();
    Entity spectatorTarget = event.getSpectatorTarget();
    Location plLoc = player.getLocation(), tarLoc = spectatorTarget.getLocation();
    Collection<Entity> entities = plLoc.getWorld().getNearbyEntities(plLoc, 1D, 1D, 1D);
    if (CustomEffectManager.hasEffect(player, CustomEffectType.CONTINUAL_SPECTATING) && !spectatorTarget.isDead() && spectatorTarget.isValid() &&
    plLoc.equals(tarLoc) && entities.contains(spectatorTarget))
    {
      event.setCancelled(true);
      return;
    }
    if (CustomConfig.UserData.SHOW_SPECTATOR_TARGET_INFO_IN_ACTIONBAR.getBoolean(player))
    {
      MessageUtil.sendActionBar(player, " ");
    }
  }
}
