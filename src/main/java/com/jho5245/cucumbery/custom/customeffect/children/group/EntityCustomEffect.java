package com.jho5245.cucumbery.custom.customeffect.children.group;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface EntityCustomEffect
{
  @NotNull Entity getEntity();
  void setEntity(@NotNull Entity entity);
}
