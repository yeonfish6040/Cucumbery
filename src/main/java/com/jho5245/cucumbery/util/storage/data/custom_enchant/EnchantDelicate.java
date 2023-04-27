package com.jho5245.cucumbery.util.storage.data.custom_enchant;

import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EnchantDelicate extends CustomEnchant
{
  public EnchantDelicate(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "key:enchantment.cucumbery.delicate|정교한 손길";
  }

  @Override
  public boolean canEnchantItem(@NotNull ItemStack itemStack)
  {
    CustomMaterial customMaterial = CustomMaterial.itemStackOf(itemStack);
    if (customMaterial != null)
    {
      return customMaterial.toString().endsWith("_AXE") || customMaterial.toString().endsWith("_HOE");
    }
    return Tag.ITEMS_AXES.isTagged(itemStack.getType()) || Tag.ITEMS_HOES.isTagged(itemStack.getType());
  }
}
