package com.jho5245.cucumbery.util.storage.data.custom_enchant.touch;

import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import org.jetbrains.annotations.NotNull;

public class EnchantDullTouch extends CustomEnchant
{
  public EnchantDullTouch(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "둔한 손길";
  }

  @Override
  public boolean isCursed()
  {
    return true;
  }
}
