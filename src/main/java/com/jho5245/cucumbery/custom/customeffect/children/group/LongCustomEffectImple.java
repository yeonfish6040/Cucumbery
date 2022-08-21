package com.jho5245.cucumbery.custom.customeffect.children.group;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import org.jetbrains.annotations.NotNull;

public class LongCustomEffectImple extends CustomEffect implements LongCustomEffect
{
  private long l;

  public LongCustomEffectImple(CustomEffectType effectType, long l)
  {
    super(effectType);
    this.l = l;
  }

  public LongCustomEffectImple(CustomEffectType effectType, int duration, long l)
  {
    super(effectType, duration);
    this.l = l;
  }

  public LongCustomEffectImple(CustomEffectType effectType, int duration, int amplifier, long l)
  {
    super(effectType, duration, amplifier);
    this.l = l;
  }

  public LongCustomEffectImple(CustomEffectType effectType, int duration, int amplifier, @NotNull DisplayType displayType, long l)
  {
    super(effectType, duration, amplifier, displayType);
    this.l = l;
  }

  @Override
  public long getLong()
  {
    return l;
  }

  @Override
  public void setLong(long l)
  {
    this.l = l;
  }

  @Override
  @NotNull
  protected LongCustomEffectImple copy()
  {
    return new LongCustomEffectImple(this.getType(), this.getDuration(), this.getAmplifier(), this.getDisplayType(), this.getLong());
  }
}
