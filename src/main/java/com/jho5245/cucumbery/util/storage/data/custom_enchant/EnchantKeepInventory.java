package com.jho5245.cucumbery.util.storage.data.custom_enchant;

import org.jetbrains.annotations.NotNull;

public class EnchantKeepInventory extends CustomEnchant
{
  public EnchantKeepInventory(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "key:enchantment.cucumbery.keep_inventory|인벤토리 보존";
  }
}
