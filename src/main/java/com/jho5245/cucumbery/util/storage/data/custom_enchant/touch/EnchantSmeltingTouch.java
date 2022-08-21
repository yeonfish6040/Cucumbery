package com.jho5245.cucumbery.util.storage.data.custom_enchant.touch;

import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EnchantSmeltingTouch extends CustomEnchant
{
  public EnchantSmeltingTouch(@NotNull String name)
  {
    super(name);
  }

  @Override
  public @NotNull String translationKey()
  {
    return "제련의 손길";
  }

  @Override
  @NotNull
  public EnchantmentTarget getItemTarget()
  {
    return EnchantmentTarget.TOOL;
  }

  @Override
  public boolean canEnchantItem(@NotNull ItemStack item)
  {
    String id = ItemStackUtil.itemExists(item) ? new NBTItem(item).getString("id") + "" : "";
    if (id.equals(""))
    {
      Material type = item.getType();
      return type == Material.ENCHANTED_BOOK || Constant.TOOLS.contains(type) || Constant.SWORDS.contains(type);
    }
    try
    {
      CustomMaterial customMaterial = CustomMaterial.valueOf(id.toUpperCase());
      return switch (customMaterial)
              {
                case TITANIUM_DRILL_R266, TITANIUM_DRILL_R366, TITANIUM_DRILL_R466, TITANIUM_DRILL_R566 -> true;
                default -> false;
              };
    }
    catch (Exception ignored)
    {
    }
    return false;
  }

  @Override
  public boolean conflictsWith(@NotNull Enchantment other)
  {
    return other.equals(Enchantment.SILK_TOUCH);
  }
}
