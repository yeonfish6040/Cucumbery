package com.jho5245.cucumbery.util.storage.data.custom_enchant;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.EntityCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;

@SuppressWarnings("deprecation")
public abstract class CustomEnchant extends EnchantmentWrapper
{
  public static Enchantment GLOW;
  public static Enchantment TELEKINESIS;
  private static final Map<NamespacedKey, Enchantment> byKey = new HashMap<>();
  private static final Map<String, Enchantment> byName = new HashMap<>();

  private static void registerEnchants()
  {
    GLOW = registerEnchant(new EnchantGlow("glow"));
    TELEKINESIS = registerEnchant(new EnchantTelekinesis("telekinesis"));
  }

  private static CustomEnchant registerEnchant(@NotNull CustomEnchant enchant)
  {
    Enchantment.registerEnchantment(enchant);
    byKey.put(enchant.getKey(), enchant);
    byName.put(enchant.getName(), enchant);
    return enchant;
  }

  public CustomEnchant(@NotNull String name)
  {
    super(name);
  }

  @Override
  public int getMaxLevel()
  {
    return 1;
  }

  @Override
  public int getStartLevel()
  {
    return 1;
  }

  @Override
  @NotNull
  public EnchantmentTarget getItemTarget()
  {
    return EnchantmentTarget.ALL;
  }

  @Override
  public boolean canEnchantItem(@NotNull ItemStack item)
  {
    return true;
  }

  @Override
  @NotNull
  public abstract String getName();

  @Override
  public boolean isTreasure()
  {
    return false;
  }

  @Override
  public boolean isCursed()
  {
    return false;
  }

  @Override
  public boolean conflictsWith(@NotNull Enchantment other)
  {
    return false;
  }

  @Override
  @NotNull
  public Component displayName(int level)
  {
    return ComponentUtil.translate("%s %s", translationKey(), level);
  }

  @Override
  @NotNull
  public abstract String translationKey();

  @Override
  public boolean isTradeable()
  {
    return false;
  }

  @Override
  public boolean isDiscoverable()
  {
    return true;
  }

  @Override
  @NotNull
  public EnchantmentRarity getRarity()
  {
    return EnchantmentRarity.COMMON;
  }

  @Override
  public float getDamageIncrease(int level, @NotNull EntityCategory entityCategory)
  {
    return 0f;
  }

  @Override
  @NotNull
  public Set<EquipmentSlot> getActiveSlots()
  {
    return new HashSet<>(Arrays.asList(EquipmentSlot.values()));
  }

  public static void onEnable()
  {
    if (!Cucumbery.config.getBoolean("use-custom-enchant-features"))
    {
      return;
    }
    try
    {
      Field f = Enchantment.class.getDeclaredField("acceptingNew");
      f.setAccessible(true);
      f.set(null, true);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    registerEnchants();
  }

  @SuppressWarnings("unchecked")
  public static void onDisable()
  {
    try
    {
      Field byKeyField = Enchantment.class.getDeclaredField("byKey");
      Field byNameField = Enchantment.class.getDeclaredField("byName");

      byKeyField.setAccessible(true);
      byNameField.setAccessible(true);

      HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) byKeyField.get(null);
      HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) byNameField.get(null);

      byKey.keySet().removeIf(CustomEnchant.byKey::containsKey);
      byName.keySet().removeIf(CustomEnchant.byName::containsKey);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
