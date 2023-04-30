package com.jho5245.cucumbery.util.storage.data.custom_enchant.ultimate;

import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EnchantHighRiskHighReturn extends CustomEnchantUltimate
{
  public EnchantHighRiskHighReturn(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "key:enchantment.cucumbery.high_risk_high_return|하이 리스크 하이 리턴";
  }

  @Override
  @NotNull
  public EnchantmentTarget getItemTarget() {
    return EnchantmentTarget.ARMOR_TORSO;
  }

  @Override
  public boolean canEnchantItem(@NotNull ItemStack itemStack)
  {
    CustomMaterial customMaterial = CustomMaterial.itemStackOf(itemStack);
    if (customMaterial == null)
    {
      Material type = itemStack.getType();
      return type == Material.ENCHANTED_BOOK || Constant.ARMORS.contains(type);
    }
    return false;
  }
}
