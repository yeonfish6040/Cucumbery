package com.jho5245.cucumbery.listeners.entity.no_groups;

import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

public class EntityTarget implements Listener
{
  @EventHandler
  public void onEntityTarget(EntityTargetEvent event)
  {
    Entity entity = event.getEntity(); // 쫓아오는 개체
    Entity target = event.getTarget(); // 쫓기는 개체
    TargetReason reason = event.getReason();
    if (target instanceof Player player)
    {
      if (UserData.SPECTATOR_MODE.getBoolean(player))
      {
        event.setCancelled(true);
        return;
      }
    }
    if (reason != TargetReason.TEMPT && entity.getType() != EntityType.EXPERIENCE_ORB && target instanceof Player player)
    {
      GameMode gamemode = player.getGameMode();
      if (gamemode != GameMode.SURVIVAL && gamemode != GameMode.ADVENTURE)
      {
        return;
      }
      if (!UserData.ENTITY_AGGRO.getBoolean(player.getUniqueId()) || CustomEffectManager.hasEffect(player, CustomEffectType.NO_ENTITY_AGGRO))
      {
        event.setTarget(null);
        event.setCancelled(true);
      }
    }
  }
}
