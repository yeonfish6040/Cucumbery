package com.jho5245.cucumbery.events.entity;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.CustomEffectManager;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
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
     * When the {@link CustomEffectType} is conflict with others.
     * <p>It means that {@link CustomEffectType#getConflictEffects()} contains this.
     */
    CONFLICT,
    /**
     * When the {@link Player} is died and the {@link CustomEffectType#isKeepOnDeath()} is <code>false</code>.
     */
    DEATH,
    /**
     * When the {@link Player} has right-clicked the effect icon in GUI
     */
    GUI,
    /**
     * When the {@link Player} uses {@link org.bukkit.Material#MILK_BUCKET} and the {@link CustomEffectType#isKeepOnMilk()} is <code>false</code>.
     */
    MILK,
    /**
     * when effect is removed by
     * {@link CustomEffectManager#removeEffect(Entity, CustomEffectType)}
     */
    PLUGIN,
    /**
     * When the {@link Player} quit the server and the {@link CustomEffectType#isKeepOnQuit()} is <code>false</code>.
     */
    QUIT,
    /**
     * When the {@link CustomEffect#getDuration()} expires
     */
    TIME_OUT,
  }
}
