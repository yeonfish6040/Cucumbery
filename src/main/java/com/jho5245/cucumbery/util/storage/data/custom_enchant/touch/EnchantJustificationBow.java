package com.jho5245.cucumbery.util.storage.data.custom_enchant.touch;

import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import org.jetbrains.annotations.NotNull;

public class EnchantJustificationBow extends CustomEnchant
{
  public EnchantJustificationBow(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "key:enchantment.cucumbery.justification_bow|칭의(활)";
  }
}
