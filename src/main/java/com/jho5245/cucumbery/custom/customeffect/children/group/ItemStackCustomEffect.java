package com.jho5245.cucumbery.custom.customeffect.children.group;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemStackCustomEffect
{
  @NotNull ItemStack getItemStack();

  void setItemStack(@NotNull ItemStack itemStack);
}
