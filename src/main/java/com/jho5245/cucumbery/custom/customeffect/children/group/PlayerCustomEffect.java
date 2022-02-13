package com.jho5245.cucumbery.custom.customeffect.children.group;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerCustomEffect
{
  @NotNull Player getPlayer();

  void setPlayer(@NotNull Player player);
}
