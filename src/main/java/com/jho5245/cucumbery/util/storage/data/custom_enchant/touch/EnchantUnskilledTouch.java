package com.jho5245.cucumbery.util.storage.data.custom_enchant.touch;

import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import org.jetbrains.annotations.NotNull;

public class EnchantUnskilledTouch extends CustomEnchant
{
  public EnchantUnskilledTouch(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "key:enchantment.cucumbery.unskilled_touch|서투른 손길";
  }

  @Override
  public boolean isCursed()
  {
    return true;
  }
}
