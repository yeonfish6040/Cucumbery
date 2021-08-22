package com.jho5245.cucumbery.listeners.entity;

import com.jho5245.cucumbery.deathmessages.LastTrampledBlockManager;
import io.papermc.paper.event.entity.EntityMoveEvent;
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

    this.getLastTrampledBlock(event);
  }

  private void getLastTrampledBlock(EntityMoveEvent event)
  {
    LivingEntity livingEntity = event.getEntity();
    LastTrampledBlockManager.lastTrampledBlock(livingEntity, event.hasChangedBlock());
  }
}
