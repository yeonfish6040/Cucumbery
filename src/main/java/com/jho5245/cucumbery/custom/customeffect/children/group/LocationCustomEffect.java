package com.jho5245.cucumbery.custom.customeffect.children.group;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface LocationCustomEffect
{
  @NotNull Location getLocation();

  void setLocation(@NotNull Location location);
}
