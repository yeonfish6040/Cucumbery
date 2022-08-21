package com.jho5245.cucumbery.custom.customeffect.children.group;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerCustomEffectImple extends CustomEffect implements PlayerCustomEffect
{
  private Player player;

  public PlayerCustomEffectImple(CustomEffectType effectType, @NotNull Player player)
  {
    super(effectType);
    this.player = player;
  }

  public PlayerCustomEffectImple(CustomEffectType effectType, int duration, @NotNull Player player)
  {
    super(effectType, duration);
    this.player = player;
  }

  public PlayerCustomEffectImple(CustomEffectType effectType, int duration, int amplifier, @NotNull Player player)
  {
    super(effectType, duration, amplifier);
    this.player = player;
  }

  public PlayerCustomEffectImple(CustomEffectType effectType, int duration, int amplifier, @NotNull DisplayType displayType, @NotNull Player player)
  {
    super(effectType, duration, amplifier, displayType);
    this.player = player;
  }

  @Override
  public @NotNull Player getPlayer()
  {
    return player;
  }

  @Override
  public void setPlayer(@NotNull Player player)
  {
    this.player = player;
  }

  @Override
  @NotNull
  protected PlayerCustomEffectImple copy()
  {
    return new PlayerCustomEffectImple(this.getType(), this.getDuration(), this.getAmplifier(), this.getDisplayType(), this.getPlayer());
  }
}
