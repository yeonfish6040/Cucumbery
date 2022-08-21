package com.jho5245.cucumbery.util.storage.data.custom_enchant;

import org.jetbrains.annotations.NotNull;

public class EnchantAssassinationBow extends CustomEnchant
{
  public EnchantAssassinationBow(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "암살(활)";
  }
}
