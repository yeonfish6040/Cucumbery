package com.jho5245.cucumbery.customeffect.children.group;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface UUIDCustomEffect
{
  @NotNull UUID getUniqueId();

  void setUniqueId(@NotNull UUID uuid);
}
