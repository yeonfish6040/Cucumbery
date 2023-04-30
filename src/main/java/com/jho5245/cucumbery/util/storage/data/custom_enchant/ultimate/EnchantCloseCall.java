package com.jho5245.cucumbery.util.storage.data.custom_enchant.ultimate;

import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EnchantCloseCall extends CustomEnchantUltimate
{
  public EnchantCloseCall(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "key:enchantment.cucumbery.close_call|구사 일생";
  }

  @Override
  public boolean canEnchantItem(@NotNull ItemStack itemStack)
  {
    CustomMaterial customMaterial = CustomMaterial.itemStackOf(itemStack);
    if (customMaterial != null)
    {
      return customMaterial.toString().endsWith("_CHESTPLATE");
    }
    return Constant.CHESTPLATES.contains(itemStack.getType());
  }

  @Override
  public int getMaxLevel()
  {
    return 5;
  }
}
