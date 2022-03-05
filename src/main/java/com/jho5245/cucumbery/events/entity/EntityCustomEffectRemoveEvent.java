package com.jho5245.cucumbery.events.entity;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EntityCustomEffectRemoveEvent extends EntityCustomEffectEvent
{
  private final RemoveReason reason;

  public EntityCustomEffectRemoveEvent(@NotNull Entity what, @NotNull CustomEffect customEffect)
  {
    this(what, customEffect, RemoveReason.PLUGIN);
  }

  public EntityCustomEffectRemoveEvent(@NotNull Entity what, @NotNull CustomEffect customEffect, @NotNull RemoveReason reason)
  {
    super(what, customEffect);
    this.reason = reason;
  }

  /**
   * see {@link RemoveReason}
   * @return the reason that the {@link CustomEffect} was removed
   */
  public RemoveReason getReason()
  {
    return reason;
  }

  /**
   * Type of effect removal
   */
  public enum RemoveReason
  {
    /**
     * when effect is removed by
     * {@link CustomEffectManager#removeEffect(Entity, CustomEffectType)}
     */
    PLUGIN,
    /**
     * When the {@link CustomEffect#getDuration()} expires
     */
    TIME_OUT,
    /**
     * When the {@link Player} has right-clicked the effect icon in GUI
     */
    PLAYER,
  }
}
