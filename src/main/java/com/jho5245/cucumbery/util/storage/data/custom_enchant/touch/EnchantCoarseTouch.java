package com.jho5245.cucumbery.util.storage.data.custom_enchant.touch;

import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import org.jetbrains.annotations.NotNull;

public class EnchantCoarseTouch extends CustomEnchant
{
  public EnchantCoarseTouch(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "key:enchantment.cucumbery.coarse_touch|거친 손길";
  }

  @Override
  public boolean isCursed()
  {
    return true;
  }
}
