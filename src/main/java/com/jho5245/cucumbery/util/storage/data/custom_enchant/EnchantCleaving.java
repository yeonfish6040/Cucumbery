package com.jho5245.cucumbery.util.storage.data.custom_enchant;

import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EnchantCleaving extends CustomEnchant
{
  public EnchantCleaving(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "key:enchantment.cucumbery.cleaving|갈리치기";
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
    return 3;
  }
}
