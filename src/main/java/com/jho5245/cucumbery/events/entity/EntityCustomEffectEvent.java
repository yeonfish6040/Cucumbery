package com.jho5245.cucumbery.events.entity;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public abstract class EntityCustomEffectEvent extends EntityEvent
{
  private static final HandlerList handlers = new HandlerList();
  private final CustomEffect customEffect;
  public EntityCustomEffectEvent(@NotNull Entity what, @NotNull CustomEffect customEffect)
  {
    super(what);
    this.customEffect = customEffect;
  }

  @NotNull
  public CustomEffect getCustomEffect()
  {
    return customEffect;
  }

  @NotNull
  public HandlerList getHandlers()
  {
    return handlers;
  }

  @NotNull
  public static HandlerList getHandlerList() {
    return handlers;
  }
}
