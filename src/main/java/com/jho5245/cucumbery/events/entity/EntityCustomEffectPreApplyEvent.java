package com.jho5245.cucumbery.events.entity;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public class EntityCustomEffectPreApplyEvent extends EntityCustomEffectEvent implements Cancellable
{
  private boolean cancelled;
  public EntityCustomEffectPreApplyEvent(@NotNull Entity what, @NotNull CustomEffect customEffect)
  {
    super(what, customEffect);
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
