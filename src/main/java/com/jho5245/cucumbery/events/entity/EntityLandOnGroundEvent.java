package com.jho5245.cucumbery.events.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public class EntityLandOnGroundEvent extends EntityEvent
{
  private static final HandlerList handlers = new HandlerList();

  public EntityLandOnGroundEvent(@NotNull Entity what)
  {
    super(what);
  }

  public static HandlerList getHandlerList()
  {
    return handlers;
  }

  @Override
  public @NotNull HandlerList getHandlers()
  {
    return handlers;
  }
}
