package com.jho5245.cucumbery.custom.customeffect;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Icon
{
  @NotNull
  ItemStack getItem();
}
