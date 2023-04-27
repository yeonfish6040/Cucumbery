package com.jho5245.cucumbery.util.storage.data.custom_enchant;

import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EnchantSunder extends CustomEnchant
{
  public EnchantSunder(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "key:enchantment.cucumbery.sunder|작물 파쇄";
  }

  @Override
  public boolean canEnchantItem(@NotNull ItemStack itemStack)
  {
    CustomMaterial customMaterial = CustomMaterial.itemStackOf(itemStack);
    if (customMaterial != null)
    {
      return customMaterial.toString().endsWith("_AXE");
    }
    return Tag.ITEMS_AXES.isTagged(itemStack.getType());
  }

  @Override
  public int getMaxLevel()
  {
    return 5;
  }
}
