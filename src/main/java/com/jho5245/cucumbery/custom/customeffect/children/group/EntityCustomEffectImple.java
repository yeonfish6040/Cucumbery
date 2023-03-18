package com.jho5245.cucumbery.custom.customeffect.children.group;

import com.jho5245.cucumbery.custom.customeffect.CustomEffect;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntityCustomEffectImple extends CustomEffect implements EntityCustomEffect
{
  private Entity entity;
  public EntityCustomEffectImple(@NotNull CustomEffectType effectType, @NotNull Entity entity)
  {
    super(effectType);
    this.entity = entity;
  }

  public EntityCustomEffectImple(@NotNull CustomEffectType effectType, int duration, @NotNull Entity entity)
  {
    super(effectType, duration);
    this.entity = entity;
  }

  public EntityCustomEffectImple(@NotNull CustomEffectType effectType, int duration, int amplifier, @NotNull Entity entity)
  {
    super(effectType, duration, amplifier);
    this.entity = entity;
  }

  public EntityCustomEffectImple(@NotNull CustomEffectType effectType, int duration, int amplifier, @NotNull DisplayType displayType, @NotNull Entity entity)
  {
    super(effectType, duration, amplifier, displayType);
    this.entity = entity;
  }

  @Override
  public @NotNull Entity getEntity()
  {
    return entity;
  }

  @Override
  public void setEntity(@NotNull Entity entity)
  {
    this.entity = entity;
  }

  @Override
  @NotNull
  protected EntityCustomEffectImple copy()
  {
    return new EntityCustomEffectImple(this.getType(), this.getDuration(), this.getAmplifier(), this.getDisplayType(), this.getEntity());
  }
}
