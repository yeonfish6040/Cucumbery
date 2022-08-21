package com.jho5245.cucumbery.util.storage.data.custom_enchant.ultimate;

import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

/**
 * A custom enchant that can be applied only once against other ultimate enchants
 */
public abstract class CustomEnchantUltimate extends CustomEnchant
{
  public CustomEnchantUltimate(@NotNull String name)
  {
    super(name);
  }

  @Override
  @NotNull
  public abstract String translationKey();

  @Override
  public final boolean conflictsWith(@NotNull Enchantment other)
  {
    return other instanceof CustomEnchantUltimate && !other.equals(this);
  }
}
