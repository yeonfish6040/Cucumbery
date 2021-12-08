package com.jho5245.cucumbery.listeners.entity;

import com.jho5245.cucumbery.customeffect.CustomEffect;
import com.jho5245.cucumbery.customeffect.DisplayType;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityCustomEffectRemove implements Listener
{
  @EventHandler
  public void onEntityCustomEffectRemove(EntityCustomEffectRemoveEvent event)
  {
    Entity entity = event.getEntity();
    CustomEffect customEffect = event.getCustomEffect();
    if (customEffect.getDisplayType() == DisplayType.PLAYER_LIST && entity instanceof Player player && player.getActivePotionEffects().isEmpty())
    {
      player.sendPlayerListFooter(Component.empty());
    }
  }
}
