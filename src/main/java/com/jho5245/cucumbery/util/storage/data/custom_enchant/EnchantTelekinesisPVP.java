package com.jho5245.cucumbery.util.storage.data.custom_enchant;

import org.jetbrains.annotations.NotNull;

public class EnchantTelekinesisPVP extends CustomEnchant
{
  public EnchantTelekinesisPVP(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "염력(PvP)";
  }
}
