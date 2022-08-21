package com.jho5245.cucumbery.custom.customeffect.children.group;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class VelocityCustomEffectImple extends CustomEffect implements VelocityCustomEffect
{
  private Vector vector;

  public VelocityCustomEffectImple(@NotNull CustomEffectType effectType, @NotNull Vector vector)
  {
    super(effectType);
    this.vector = vector;
  }

  public VelocityCustomEffectImple(@NotNull CustomEffectType effectType, int duration, @NotNull Vector vector)
  {
    super(effectType, duration);
    this.vector = vector;
  }

  public VelocityCustomEffectImple(@NotNull CustomEffectType effectType, int duration, int amplifier, @NotNull Vector vector)
  {
    super(effectType, duration, amplifier);
    this.vector = vector;
  }

  public VelocityCustomEffectImple(@NotNull CustomEffectType effectType, int duration, int amplifier, @NotNull DisplayType displayType, @NotNull Vector vector)
  {
    super(effectType, duration, amplifier, displayType);
    this.vector = vector;
  }

  @Override
  public @NotNull Vector getVelocity()
  {
    return vector;
  }

  @Override
  public void setVelocity(@NotNull Vector vector)
  {
    this.vector = vector;
  }

  @Override
  @NotNull
  protected VelocityCustomEffectImple copy()
  {
    return new VelocityCustomEffectImple(this.getType(), this.getDuration(), this.getAmplifier(), this.getDisplayType(), this.getVelocity());
  }
}
