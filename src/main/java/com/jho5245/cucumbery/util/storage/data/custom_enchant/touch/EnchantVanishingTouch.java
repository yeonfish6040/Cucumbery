package com.jho5245.cucumbery.util.storage.data.custom_enchant.touch;

import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import org.jetbrains.annotations.NotNull;

public class EnchantVanishingTouch extends CustomEnchant
{
  public EnchantVanishingTouch(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "key:enchantment.cucumbery.vanishing_touch|소실의 손길";
  }

  @Override
  public boolean isCursed()
  {
    return true;
  }
}
