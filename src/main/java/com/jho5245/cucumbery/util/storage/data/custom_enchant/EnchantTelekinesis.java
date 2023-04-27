package com.jho5245.cucumbery.util.storage.data.custom_enchant;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EnchantTelekinesis extends CustomEnchant
{
  public EnchantTelekinesis(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "key:enchantment.cucumbery.telekinesis|염력";
  }

  @Override
  public boolean canEnchantItem(@NotNull ItemStack itemStack)
  {
    Material material = itemStack.getType();
    return Tag.ITEMS_SWORDS.isTagged(material) || Tag.ITEMS_TOOLS.isTagged(material);
  }
}
