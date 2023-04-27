package com.jho5245.cucumbery.util.storage.data.custom_enchant;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EnchantIdiotShooter extends CustomEnchant
{
  public EnchantIdiotShooter(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "key:enchantment.cucumbery.idiot_shooter|똥손";
  }

  @Override
  public boolean isCursed()
  {
    return true;
  }

  @Override
  public int getMaxLevel()
  {
    return 20;
  }

  @Override
  public boolean canEnchantItem(@NotNull ItemStack itemStack)
  {
    Material type = itemStack.getType();
    return type == Material.TRIDENT || type == Material.BOW || type == Material.CROSSBOW;
  }
}
