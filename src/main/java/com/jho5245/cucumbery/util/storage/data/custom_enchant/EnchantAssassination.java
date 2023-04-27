package com.jho5245.cucumbery.util.storage.data.custom_enchant;

import com.jho5245.cucumbery.util.storage.data.Constant;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EnchantAssassination extends CustomEnchant
{
  public EnchantAssassination(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "key:enchantment.cucumbery.assassination|암살";
  }

  @Override
  public boolean canEnchantItem(@NotNull ItemStack itemStack)
  {
    Material type = itemStack.getType();
    return type != Material.BOW && (Constant.TOOLS.contains(type) || Constant.SWORDS.contains(type));
  }
}
