package com.jho5245.cucumbery.events.entity;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntityCustomEffectApplyEvent extends EntityCustomEffectEvent
{
  public EntityCustomEffectApplyEvent(@NotNull Entity what, @NotNull CustomEffect customEffect)
  {
    super(what, customEffect);
  }
}
