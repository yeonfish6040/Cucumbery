package com.jho5245.cucumbery.custom.customeffect.children.group;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import org.jetbrains.annotations.NotNull;

public class RealDurationCustomEffectImple extends CustomEffect implements RealDurationCustomEffect
{
  long startTime, endTime;

  public RealDurationCustomEffectImple(CustomEffectType effectType, long startTime, long endTime)
  {
    super(effectType);
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public RealDurationCustomEffectImple(CustomEffectType effectType, int duration, long startTime, long endTime)
  {
    super(effectType, duration);
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public RealDurationCustomEffectImple(CustomEffectType effectType, int duration, int amplifier, long startTime, long endTime)
  {
    super(effectType, duration, amplifier);
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public RealDurationCustomEffectImple(CustomEffectType effectType, int duration, int amplifier, @NotNull DisplayType displayType, long startTime, long endTime)
  {
    super(effectType, duration, amplifier, displayType);
    this.startTime = startTime;
    this.endTime = endTime;
  }

  @Override
  public long getStartTimeInMillis()
  {
    return startTime;
  }

  @Override
  public long getEndTimeInMillis()
  {
    return endTime;
  }

  @Override
  @NotNull
  protected RealDurationCustomEffectImple copy()
  {
    return new RealDurationCustomEffectImple(this.getType(), this.getDuration(), this.getAmplifier(), this.getDisplayType(), this.getStartTimeInMillis(), this.getEndTimeInMillis());
  }
}
