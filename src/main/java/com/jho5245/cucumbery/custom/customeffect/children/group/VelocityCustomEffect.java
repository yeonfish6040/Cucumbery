package com.jho5245.cucumbery.custom.customeffect.children.group;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public interface VelocityCustomEffect
{
  @NotNull Vector getVelocity();

  void setVelocity(@NotNull Vector vector);
}
