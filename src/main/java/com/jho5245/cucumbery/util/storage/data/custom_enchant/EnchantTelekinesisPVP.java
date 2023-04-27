package com.jho5245.cucumbery.util.storage.data.custom_enchant;

import org.jetbrains.annotations.NotNull;

public class EnchantTelekinesisPVP extends EnchantTelekinesis
{
  public EnchantTelekinesisPVP(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "key:enchantment.cucumbery.telekinesis_pvp|염력(PvP)";
  }
}
