package com.jho5245.cucumbery.util.storage.data.custom_enchant;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EnchantDefenseChance extends CustomEnchant
{
  public EnchantDefenseChance(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "key:enchantment.cucumbery.defense_chance|우연한 방어";
  }

  @Override
  public boolean canEnchantItem(@NotNull ItemStack itemStack)
  {
    return itemStack.getType() == Material.SHIELD;
  }

  @Override
  public boolean conflictsWith(@NotNull Enchantment other)
  {
    return other.equals(Enchantment.DURABILITY);
  }

  @Override
  public int getMaxLevel()
  {
    return 5;
  }
}
