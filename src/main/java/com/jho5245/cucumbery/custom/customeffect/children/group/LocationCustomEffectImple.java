package com.jho5245.cucumbery.custom.customeffect.children.group;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class LocationCustomEffectImple extends CustomEffect implements LocationCustomEffect
{
  protected Location location;

  public LocationCustomEffectImple(CustomEffectType effectType, @NotNull Location location)
  {
    super(effectType);
    this.location = location.clone();
  }

  public LocationCustomEffectImple(CustomEffectType effectType, int duration, @NotNull Location location)
  {
    super(effectType, duration);
    this.location = location.clone();
  }

  public LocationCustomEffectImple(CustomEffectType effectType, int duration, int amplifier, @NotNull Location location)
  {
    super(effectType, duration, amplifier);
    this.location = location.clone();
  }

  public LocationCustomEffectImple(CustomEffectType effectType, int duration, int amplifier, @NotNull DisplayType displayType, @NotNull Location location)
  {
    super(effectType, duration, amplifier, displayType);
    this.location = location.clone();
  }

  @Override
  public @NotNull Location getLocation()
  {
    return location;
  }

  @Override
  public void setLocation(@NotNull Location location)
  {
    this.location = location;
  }

  @NotNull
  @Override
  protected LocationCustomEffectImple copy()
  {
    return new LocationCustomEffectImple(this.getType(), this.getDuration(), this.getAmplifier(), this.getDisplayType(), this.getLocation());
  }
}
