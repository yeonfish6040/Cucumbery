package com.jho5245.cucumbery.util.storage.data.custom_enchant.ultimate;

import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
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
    return "하이 리스크 하이 리턴";
  }

  @Override
  @NotNull
  public EnchantmentTarget getItemTarget() {
    return EnchantmentTarget.ARMOR_TORSO;
  }

  @Override
  public boolean canEnchantItem(@NotNull ItemStack item)
  {
    String id = ItemStackUtil.itemExists(item) ? new NBTItem(item).getString("id") + "" : "";
    if (id.equals(""))
    {
      Material type = item.getType();
      return type == Material.ENCHANTED_BOOK || Constant.ARMORS.contains(type);
    }
    try
    {
      CustomMaterial customMaterial = CustomMaterial.valueOf(id.toUpperCase());
      return switch (customMaterial)
              {
                default -> false;
              };
    }
    catch (Exception ignored)
    {
    }
    return false;
  }
}
