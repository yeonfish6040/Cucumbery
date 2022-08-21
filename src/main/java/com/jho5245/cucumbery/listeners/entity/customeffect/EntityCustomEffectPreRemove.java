package com.jho5245.cucumbery.listeners.entity.customeffect;

import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectPreRemoveEvent;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent.RemoveReason;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityCustomEffectPreRemove implements Listener
{
  @EventHandler
  public void onEntityCustomEffectRemove(EntityCustomEffectPreRemoveEvent event)
  {
    RemoveReason reason = event.getReason();
    if (reason == RemoveReason.GUI && event.getEntity() instanceof Player player)
    {
      if (CustomEffectManager.hasEffect(player, CustomEffectType.NO_BUFF_REMOVE))
      {
        event.setCancelled(true);
      }
    }
  }
}
