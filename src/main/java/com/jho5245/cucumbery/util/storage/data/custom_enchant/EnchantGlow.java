package com.jho5245.cucumbery.util.storage.data.custom_enchant;

import org.jetbrains.annotations.NotNull;

public class EnchantGlow extends CustomEnchant
{
  public EnchantGlow(@NotNull String name)
  {
    super(name);
  }

  @Override
  @NotNull
  public String translationKey()
  {
    return "발광";
  }
}
