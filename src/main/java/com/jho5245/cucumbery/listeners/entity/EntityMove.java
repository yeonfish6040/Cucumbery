package com.jho5245.cucumbery.listeners.entity;

import com.jho5245.cucumbery.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.deathmessages.DeathManager;
import com.jho5245.cucumbery.deathmessages.LastTrampledBlockManager;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityMove implements Listener
{
  @EventHandler
  public void onEntityMove(EntityMoveEvent event)
  {
    if (event.isCancelled())
    {
      return;
    }
    Entity entity = event.getEntity();
    this.customEffect(event);
    if (DeathManager.deathMessageApplicable(entity))
    {
      this.getLastTrampledBlock(event);
    }
  }

  private void customEffect(EntityMoveEvent event)
  {
    Entity entity = event.getEntity();
    if (CustomEffectManager.hasEffect(entity, CustomEffectType.STOP))
    {
      event.setCancelled(true);
    }
    if (CustomEffectManager.hasEffect(entity, CustomEffectType.CONFUSION))
    {
      Location from = event.getFrom(), to = event.getTo();
      float yaw = Math.min(360f, Math.max(-360f, 2 * from.getYaw() - to.getYaw()));
      float pitch = Math.min(90f, Math.max(-90f, 2 * from.getPitch() - to.getPitch()));
      event.setTo(new Location(to.getWorld(), to.getX(), to.getY(), to.getZ(), yaw, pitch));
    }
  }

  private void getLastTrampledBlock(EntityMoveEvent event)
  {
    LivingEntity livingEntity = event.getEntity();
    LastTrampledBlockManager.lastTrampledBlock(livingEntity, event.hasChangedBlock());
  }
}
