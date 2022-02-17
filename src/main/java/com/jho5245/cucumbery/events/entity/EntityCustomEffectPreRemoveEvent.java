package com.jho5245.cucumbery.events.entity;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent.Reason;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public class EntityCustomEffectPreRemoveEvent extends EntityCustomEffectEvent implements Cancellable
{
  private boolean cancelled;
  private final Reason reason;
  public EntityCustomEffectPreRemoveEvent(@NotNull Entity what, @NotNull CustomEffect customEffect)
  {
    this(what, customEffect, Reason.PLUGIN);
  }
  public EntityCustomEffectPreRemoveEvent(@NotNull Entity what, @NotNull CustomEffect customEffect, @NotNull Reason reason)
  {
    super(what, customEffect);
    this.reason = reason;
  }

  /**
   * see {@link Reason}
   * @return the reason that the {@link CustomEffect} was removed
   */
  @NotNull
  public Reason getReason()
  {
    return reason;
  }

  @Override
  public boolean isCancelled()
  {
    return cancelled;
  }

  /**
   * WARNING! should only be cancelled when the {@link Reason} is {@link Reason#PLAYER}!
   * @param cancelled whether the event is cancelled or not.
   * @throws IllegalStateException when the {@link Reason} is not {@link Reason#PLAYER}
   */
  @Override
  public void setCancelled(boolean cancelled) throws IllegalStateException
  {
    if (this.reason != Reason.PLAYER)
    {
      throw new IllegalStateException();
    }
    this.cancelled = cancelled;
  }
}
