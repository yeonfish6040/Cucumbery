package com.jho5245.cucumbery.util.storage.data.custom_enchant;

import org.jetbrains.annotations.NotNull;

public class EnchantFranticFortune extends CustomEnchant
{
  public EnchantFranticFortune(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "key:enchantment.cucumbery.frantic_fortune|광기의 행운";
  }
}
