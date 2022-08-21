package com.jho5245.cucumbery.events.entity;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.events.entity.EntityCustomEffectRemoveEvent.RemoveReason;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public class EntityCustomEffectPreRemoveEvent extends EntityCustomEffectEvent implements Cancellable
{
  private boolean cancelled;
  private final RemoveReason reason;
  public EntityCustomEffectPreRemoveEvent(@NotNull Entity what, @NotNull CustomEffect customEffect)
  {
    this(what, customEffect, RemoveReason.PLUGIN);
  }
  public EntityCustomEffectPreRemoveEvent(@NotNull Entity what, @NotNull CustomEffect customEffect, @NotNull EntityCustomEffectRemoveEvent.RemoveReason reason)
  {
    super(what, customEffect);
    this.reason = reason;
  }

  /**
   * see {@link RemoveReason}
   * @return the reason that the {@link CustomEffect} was removed
   */
  @NotNull
  public EntityCustomEffectRemoveEvent.RemoveReason getReason()
  {
    return reason;
  }

  @Override
  public boolean isCancelled()
  {
    return cancelled;
  }

  /**
   * WARNING! should only be cancelled when the {@link RemoveReason} is {@link RemoveReason#GUI}!
   * @param cancelled whether the event is cancelled or not.
   * @throws IllegalStateException when the {@link RemoveReason} is not {@link RemoveReason#GUI}
   */
  @Override
  public void setCancelled(boolean cancelled) throws IllegalStateException
  {
    if (this.reason != RemoveReason.GUI)
    {
      throw new IllegalStateException();
    }
    this.cancelled = cancelled;
  }
}
