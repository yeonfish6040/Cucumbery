package com.jho5245.cucumbery.custom.customeffect.children.group;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class LocationVelocityCustomEffectImple extends CustomEffect implements LocationVelocityCustomEffect
{
  private Location location;
  private Vector vector;
  public LocationVelocityCustomEffectImple(@NotNull CustomEffectType effectType, @NotNull Location location, @NotNull Vector vector)
  {
    super(effectType);
    this.location = location;
    this.vector = vector;
  }

  public LocationVelocityCustomEffectImple(@NotNull CustomEffectType effectType, int duration, @NotNull Location location, @NotNull Vector vector)
  {
    super(effectType, duration);
    this.location = location;
    this.vector = vector;
  }

  public LocationVelocityCustomEffectImple(@NotNull CustomEffectType effectType, int duration, int amplifier, @NotNull Location location, @NotNull Vector vector)
  {
    super(effectType, duration, amplifier);
    this.location = location;
    this.vector = vector;
  }

  public LocationVelocityCustomEffectImple(@NotNull CustomEffectType effectType, int duration, int amplifier, @NotNull DisplayType displayType, @NotNull Location location, @NotNull Vector vector)
  {
    super(effectType, duration, amplifier, displayType);
    this.location = location;
    this.vector = vector;
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
  protected LocationVelocityCustomEffectImple copy()
  {
    return new LocationVelocityCustomEffectImple(this.getType(), this.getDuration(), this.getAmplifier(), this.getDisplayType(), this.getLocation(), this.getVelocity());
  }
}
