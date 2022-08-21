package com.jho5245.cucumbery.events.entity;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Called when {@link CustomEffect} is applied to an {@link Entity}. Contains {@link ApplyReason}.
 */
public abstract class EntityCustomEffectAbstractApplyEvent extends EntityCustomEffectEvent
{
  private final ApplyReason reason;

  public EntityCustomEffectAbstractApplyEvent(@NotNull Entity what, @NotNull CustomEffect customEffect, @NotNull ApplyReason reason)
  {
    super(what, customEffect);
    this.reason = reason;
  }

  /**
   * @return the reason that {@link CustomEffect} is applied
   */
  public ApplyReason getReason()
  {
    return reason;
  }

  /**
   * Represents reasons why the {@link CustomEffect} is applied
   */
  public enum ApplyReason
  {
    /**
     * default. added by plugin itself internally.
     */
    PLUGIN,
    /**
     * by /effect3 give command
     */
    COMMAND,
    /**
     * by effect distribution (i.g {@link CustomEffectType#HEROS_ECHO}
     */
    DISTRIBUTE_ALL_PLAYERS,
    /**
     * when a {@link Player} consumes {@link org.bukkit.inventory.ItemStack} with {@link org.bukkit.Material#POTION},
     * when called {@link org.bukkit.event.player.PlayerItemConsumeEvent}
     */
    POTION,
    /**
     * when a {@link org.bukkit.entity.LivingEntity} was applied {@link org.bukkit.entity.ThrownPotion} with {@link org.bukkit.Material#SPLASH_POTION},
     * when called {@link org.bukkit.event.entity.PotionSplashEvent}
     */
    SPLASH_POTION,
    /**
     * when a {@link org.bukkit.entity.LivingEntity} was applied {@link org.bukkit.entity.AreaEffectCloud} with {@link org.bukkit.Material#LINGERING_POTION},
     * when called {@link org.bukkit.event.entity.AreaEffectCloudApplyEvent}
     */
    LINGERING_POTION,
    /**
     * when a {@link org.bukkit.entity.LivingEntity} was shot by {@link org.bukkit.entity.Arrow} with {@link org.bukkit.Material#TIPPED_ARROW},
     * when called {@link org.bukkit.event.entity.EntityDamageByEntityEvent}
     */
    TIPPED_ARROW,
  }
}
