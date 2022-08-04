package com.jho5245.cucumbery.util.storage.data.custom_enchant;

import org.jetbrains.annotations.NotNull;

public class EnchantIdiotShooter extends CustomEnchant
{
  public EnchantIdiotShooter(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "똥손";
  }

  @Override
  public boolean isCursed()
  {
    return true;
  }
}
