package com.jho5245.cucumbery.custom.customeffect.children.group;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import org.jetbrains.annotations.NotNull;

public class DoubleCustomEffectImple extends CustomEffect implements DoubleCustomEffect
{
  private double d;

  public DoubleCustomEffectImple(@NotNull CustomEffectType effectType, double d)
  {
    super(effectType);
    this.d = d;
  }

  public DoubleCustomEffectImple(@NotNull CustomEffectType effectType, int duration, double d)
  {
    super(effectType, duration);
    this.d = d;
  }

  public DoubleCustomEffectImple(@NotNull CustomEffectType effectType, int duration, int amplifier, double d)
  {
    super(effectType, duration, amplifier);
    this.d = d;
  }

  public DoubleCustomEffectImple(@NotNull CustomEffectType effectType, int duration, int amplifier, @NotNull DisplayType displayType, double d)
  {
    super(effectType, duration, amplifier, displayType);
    this.d = d;
  }

  @Override
  public double getDouble()
  {
    return d;
  }

  @Override
  public void setDouble(double d)
  {
    this.d = d;
  }

  @Override
  @NotNull
  protected DoubleCustomEffectImple copy()
  {
    return new DoubleCustomEffectImple(this.getType(), this.getDuration(), this.getAmplifier(), this.getDisplayType(), this.getDouble());
  }
}
