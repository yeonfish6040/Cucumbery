package com.jho5245.cucumbery.custom.customeffect.children.group;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public interface OfflinePlayerCustomEffect
{
  @NotNull OfflinePlayer getOfflinePlayer();

  void setOfflinePlayer(@NotNull OfflinePlayer offlinePlayer);
}
