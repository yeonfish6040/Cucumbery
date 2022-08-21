package com.jho5245.cucumbery.custom.customeffect.children.group;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import org.jetbrains.annotations.NotNull;

public class StringCustomEffectImple extends CustomEffect implements StringCustomEffect
{
  private String s;

  public StringCustomEffectImple(@NotNull CustomEffectType effectType, @NotNull String s)
  {
    super(effectType);
    this.s = s;
  }

  public StringCustomEffectImple(@NotNull CustomEffectType effectType, int duration, @NotNull String s)
  {
    super(effectType, duration);
    this.s = s;
  }

  public StringCustomEffectImple(@NotNull CustomEffectType effectType, int duration, int amplifier, @NotNull String s)
  {
    super(effectType, duration, amplifier);
    this.s = s;
  }

  public StringCustomEffectImple(@NotNull CustomEffectType effectType, int duration, int amplifier, @NotNull DisplayType displayType, @NotNull String s)
  {
    super(effectType, duration, amplifier, displayType);
    this.s = s;
  }

  @Override
  public @NotNull String getString()
  {
    return s;
  }

  @Override
  public void setString(@NotNull String s)
  {
    this.s = s;
  }

  @Override
  @NotNull
  protected StringCustomEffectImple copy()
  {
    return new StringCustomEffectImple(this.getType(), this.getDuration(), this.getAmplifier(), this.getDisplayType(), this.getString());
  }
}
