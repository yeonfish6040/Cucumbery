package com.jho5245.cucumbery.util.storage.data.custom_enchant.ultimate;

import org.bukkit.enchantments.EnchantmentTarget;
import org.jetbrains.annotations.NotNull;

public class EnchantHighRiskHighReturn extends CustomEnchantUltimate
{
  public EnchantHighRiskHighReturn(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "하이 리스크 하이 리턴";
  }

  @Override
  @NotNull
  public EnchantmentTarget getItemTarget() {
    return EnchantmentTarget.ARMOR_TORSO;
  }
}
