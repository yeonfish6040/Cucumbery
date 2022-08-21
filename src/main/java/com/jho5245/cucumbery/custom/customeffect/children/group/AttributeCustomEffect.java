package com.jho5245.cucumbery.custom.customeffect.children.group;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.jetbrains.annotations.NotNull;

public interface AttributeCustomEffect extends UUIDCustomEffect
{
  @NotNull
  Attribute getAttribute();

  void setAttribute(@NotNull Attribute attribute);

  @NotNull Operation getOperation();

  void setOperation(@NotNull Operation operation);

  double getMultiplier();

  void setMultiplier(double multiplier);
}
