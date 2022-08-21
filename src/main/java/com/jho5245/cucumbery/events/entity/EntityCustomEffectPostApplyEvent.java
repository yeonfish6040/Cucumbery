package com.jho5245.cucumbery.events.entity;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntityCustomEffectPostApplyEvent extends EntityCustomEffectAbstractApplyEvent
{
  public EntityCustomEffectPostApplyEvent(@NotNull Entity what, @NotNull CustomEffect customEffect)
  {
    this(what, customEffect, ApplyReason.PLUGIN);
  }

  public EntityCustomEffectPostApplyEvent(@NotNull Entity what, @NotNull CustomEffect customEffect, @NotNull ApplyReason reason)
  {
    super(what, customEffect, reason);
  }
}
