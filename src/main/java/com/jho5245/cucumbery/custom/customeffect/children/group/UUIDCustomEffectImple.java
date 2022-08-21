package com.jho5245.cucumbery.custom.customeffect.children.group;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UUIDCustomEffectImple extends CustomEffect implements UUIDCustomEffect
{
  private UUID uuid;

  public UUIDCustomEffectImple(CustomEffectType effectType, @NotNull UUID uuid)
  {
    super(effectType);
    this.uuid = uuid;
  }

  public UUIDCustomEffectImple(CustomEffectType effectType, int duration, @NotNull UUID uuid)
  {
    super(effectType, duration);
    this.uuid = uuid;
  }

  public UUIDCustomEffectImple(CustomEffectType effectType, int duration, int amplifier, @NotNull UUID uuid)
  {
    super(effectType, duration, amplifier);
    this.uuid = uuid;
  }

  public UUIDCustomEffectImple(CustomEffectType effectType, int duration, int amplifier, @NotNull DisplayType displayType, @NotNull UUID uuid)
  {
    super(effectType, duration, amplifier, displayType);
    this.uuid = uuid;
  }

  @Override
  public @NotNull UUID getUniqueId()
  {
    return uuid;
  }

  @Override
  public void setUniqueId(@NotNull UUID uuid)
  {
    this.uuid = uuid;
  }
  @Override
  @NotNull
  protected UUIDCustomEffectImple copy()
  {
    return new UUIDCustomEffectImple(this.getType(), this.getDuration(), this.getAmplifier(), this.getDisplayType(), this.getUniqueId());
  }
}
