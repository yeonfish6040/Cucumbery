package com.jho5245.cucumbery.events.entity;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public class EntityCustomEffectPreApplyEvent extends EntityCustomEffectAbstractApplyEvent implements Cancellable
{
  private boolean cancelled;

  public EntityCustomEffectPreApplyEvent(@NotNull Entity what, @NotNull CustomEffect customEffect)
  {
    this(what, customEffect, ApplyReason.PLUGIN);
  }

  public EntityCustomEffectPreApplyEvent(@NotNull Entity what, @NotNull CustomEffect customEffect, @NotNull ApplyReason reason)
  {
    super(what, customEffect, reason);
  }

  @Override
  public boolean isCancelled()
  {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled)
  {
    this.cancelled = cancelled;
  }
}
