package com.jho5245.cucumbery.events.entity;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntityCustomEffectRemoveEvent extends EntityCustomEffectEvent
{
  private final Reason reason;

  public EntityCustomEffectRemoveEvent(@NotNull Entity what, @NotNull CustomEffect customEffect)
  {
    this(what, customEffect, Reason.PLUGIN);
  }

  public EntityCustomEffectRemoveEvent(@NotNull Entity what, @NotNull CustomEffect customEffect, @NotNull Reason reason)
  {
    super(what, customEffect);
    this.reason = reason;
  }

  /**
   * see {@link Reason}
   * @return the reason that the {@link CustomEffect} was removed
   */
  public Reason getReason()
  {
    return reason;
  }

  /**
   * Type of effect removal
   */
  public enum Reason
  {
    /**
     * when effect is removed by
     * {@link com.jho5245.cucumbery.custom.customeffect.CustomEffectManager#removeEffect(Entity, CustomEffectType)}
     */
    PLUGIN,
    /**
     * When the {@link CustomEffect#getDuration()} expires
     */
    TIME_OUT,
    /**
     * When the {@link org.bukkit.entity.Player} has right-clicked the effect icon in GUI
     */
    PLAYER,
  }
}
