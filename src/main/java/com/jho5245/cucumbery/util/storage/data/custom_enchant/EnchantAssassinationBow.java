package com.jho5245.cucumbery.util.storage.data.custom_enchant;

import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EnchantAssassinationBow extends CustomEnchant
{
  public EnchantAssassinationBow(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "key:enchantment.cucumbery.assassination_bow|암살(활)";
  }

  @Override
  public boolean canEnchantItem(@NotNull ItemStack itemStack)
  {
    CustomMaterial customMaterial = CustomMaterial.itemStackOf(itemStack);
    if (customMaterial != null)
    {
      return customMaterial.toString().startsWith("BOW");
    }
    return itemStack.getType() == Material.BOW;
  }
}
