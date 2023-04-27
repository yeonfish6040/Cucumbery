package com.jho5245.cucumbery.util.storage.data.custom_enchant;

import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EnchantFarmersGrace extends CustomEnchant
{
  public EnchantFarmersGrace(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "key:enchantment.cucumbery.farmers_grace|농부의 우아함";
  }

  @Override
  public boolean canEnchantItem(@NotNull ItemStack itemStack)
  {
    CustomMaterial customMaterial = CustomMaterial.itemStackOf(itemStack);
    if (customMaterial != null)
    {
      return customMaterial.toString().endsWith("_BOOTS");
    }
    return Constant.BOOTSES.contains(itemStack.getType());
  }
}
