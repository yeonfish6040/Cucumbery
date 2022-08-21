package com.jho5245.cucumbery.custom.customeffect.children.group;

import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AttributeCustomEffectImple extends UUIDCustomEffectImple implements AttributeCustomEffect
{
  private Attribute attribute;
  private Operation operation;
  private double multiplier;

  public AttributeCustomEffectImple(CustomEffectType effectType, @NotNull UUID uuid, @NotNull Attribute attribute, @NotNull Operation operation, double multiplier)
  {
    super(effectType, uuid);
    this.attribute = attribute;
    this.operation = operation;
    this.multiplier = multiplier;
  }

  public AttributeCustomEffectImple(CustomEffectType effectType, int duration, @NotNull UUID uuid, @NotNull Attribute attribute, @NotNull Operation operation, double multiplier)
  {
    super(effectType, duration, uuid);
    this.attribute = attribute;
    this.operation = operation;
    this.multiplier = multiplier;
  }

  public AttributeCustomEffectImple(CustomEffectType effectType, int duration, int amplifier, @NotNull UUID uuid, @NotNull Attribute attribute, @NotNull Operation operation, double multiplier)
  {
    super(effectType, duration, amplifier, uuid);
    this.attribute = attribute;
    this.operation = operation;
    this.multiplier = multiplier;
  }

  public AttributeCustomEffectImple(CustomEffectType effectType, int duration, int amplifier, @NotNull DisplayType displayType, @NotNull UUID uuid, @NotNull Attribute attribute, @NotNull Operation operation, double multiplier)
  {
    super(effectType, duration, amplifier, displayType, uuid);
    this.attribute = attribute;
    this.operation = operation;
    this.multiplier = multiplier;
  }

  @Override
  public @NotNull Attribute getAttribute()
  {
    return attribute;
  }

  @Override
  public void setAttribute(@NotNull Attribute attribute)
  {
    this.attribute = attribute;
  }

  @Override
  public @NotNull AttributeModifier.Operation getOperation()
  {
    return operation;
  }

  @Override
  public void setOperation(@NotNull AttributeModifier.Operation operation)
  {
    this.operation = operation;
  }

  @Override
  public double getMultiplier()
  {
    return multiplier;
  }

  @Override
  public void setMultiplier(double multiplier)
  {
    this.multiplier = multiplier;
  }

  @Override
  @NotNull
  protected AttributeCustomEffectImple copy()
  {
    return new AttributeCustomEffectImple(this.getType(), this.getDuration(), this.getAmplifier(), this.getDisplayType(), this.getUniqueId(), this.getAttribute(), this.getOperation(), this.getMultiplier());
  }
}
