package com.jho5245.cucumbery.util.storage.data.custom_enchant;

import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.touch.*;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.ultimate.CustomEnchantUltimate;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.ultimate.EnchantCloseCall;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.ultimate.EnchantHighRiskHighReturn;
import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;

public abstract class CustomEnchant extends EnchantmentWrapper
{
  /**
   * A decorative enchant for glowing items.
   */
  public static Enchantment GLOW;

  /**
   * If an {@link ItemStack} has this enchant, it never be dropped upon death.
   */
  public static Enchantment KEEP_INVENTORY;

  /**
   * Blocks and mob drops go directly to {@link Player}'s inventory.
   */
  public static Enchantment TELEKINESIS;
  /**
   * If keepInv is false, player kill drops go directly to {@link Player}'s inventory.
   */
  public static Enchantment TELEKINESIS_PVP;

  /**
   * By predefined chance table, mining {@link Material#ICE} will drop certain ice block.
   */
  public static Enchantment COLD_TOUCH;
  /**
   * Removes target's {@link LivingEntity#getNoDamageTicks()} (set to zero).
   */
  public static Enchantment JUSTIFICATION;
  /**
   * Removes target's {@link LivingEntity#getNoDamageTicks()} (set to zero). Only applicable for {@link Material#BOW}
   */
  public static Enchantment JUSTIFICATION_BOW;
  /**
   * Blocks and mob drops become its smelted form.
   */
  public static Enchantment SMELTING_TOUCH;

  /**
   * By predefined chance table, mining {@link Material#CACTUS} will drop certain ice block.
   */
  public static Enchantment WARM_TOUCH;

  /**
   * Blocks and mob drops will disappear.
   */
  public static Enchantment COARSE_TOUCH;
  /**
   * Blocks drops will become {@link Material#FLINT} in chance.
   */
  public static Enchantment DULL_TOUCH;
  /**
   * Blocks and mob drops will not grant any xp.
   */
  public static Enchantment UNSKILLED_TOUCH;
  /**
   * Blocks that is {@link BlockInventoryHolder#getInventory()}'s item will be vanished.
   */
  public static Enchantment VANISHING_TOUCH;

  /**
   * ANY Blocks drops will be doubled
   */
  public static Enchantment FRANTIC_FORTUNE;
  /**
   * Fishing root items will be DOUBLED
   */
  public static Enchantment FRANTIC_LUCK_OF_THE_SEA;

  /**
   * If kill an entity with any item with this Enchant, the killer on death message will be hidden.
   */
  public static Enchantment ASSASSINATION;

  /**
   * If kill an entity with any item with this Enchant, the killer on death message will be hidden. Only applicable for {@link Material#BOW}
   */
  public static Enchantment ASSASSINATION_BOW;

  /**
   * If a throwable weapon/projectile has this, It's accuracy will be dropped.
   */
  public static Enchantment IDIOT_SHOOTER;

  public static Enchantment CLEAVING;

  // Ulitmate Enchants
  public static Enchantment HIGH_RISK_HIGH_RETURN;

  /**
   * 구사 일생
   * <p>Grants chances not to die upon death.
   */
  public static Enchantment CLOSE_CALL;

  private static final Map<NamespacedKey, Enchantment> byKey = new HashMap<>();
  private static final Map<String, Enchantment> byName = new HashMap<>();

  public static void registerEnchants()
  {
    GLOW = registerEnchant(new EnchantGlow("glow"));

    KEEP_INVENTORY = registerEnchant(new EnchantKeepInventory("keep_inventory"));
    TELEKINESIS = registerEnchant(new EnchantTelekinesis("telekinesis"));
    TELEKINESIS_PVP = registerEnchant(new EnchantTelekinesisPVP("telekinesis_pvp"));

    COLD_TOUCH = registerEnchant(new EnchantColdTouch("cold_touch"));
    JUSTIFICATION = registerEnchant(new EnchantJustification("justification"));
    JUSTIFICATION_BOW = registerEnchant(new EnchantJustificationBow("justification_bow"));
    SMELTING_TOUCH = registerEnchant(new EnchantSmeltingTouch("smelting_touch"));
    WARM_TOUCH = registerEnchant(new EnchantWarmTouch("warm_touch"));

    COARSE_TOUCH = registerEnchant(new EnchantCoarseTouch("coarse_touch"));
    DULL_TOUCH = registerEnchant(new EnchantDullTouch("dull_touch"));
    UNSKILLED_TOUCH = registerEnchant(new EnchantUnskilledTouch("unskilled_touch"));
    VANISHING_TOUCH = registerEnchant(new EnchantVanishingTouch("vanishing_touch"));

    FRANTIC_FORTUNE = registerEnchant(new EnchantFranticFortune("frantic_fortune"));
    FRANTIC_LUCK_OF_THE_SEA = registerEnchant(new EnchantFranticLuckOfTheSea("frantic_luck_of_the_sea"));

    ASSASSINATION = registerEnchant(new EnchantAssassination("assassination"));
    ASSASSINATION_BOW = registerEnchant(new EnchantAssassinationBow("assassination_bow"));

    IDIOT_SHOOTER = registerEnchant(new EnchantIdiotShooter("idiot_shooter"));

    CLEAVING = registerEnchant(new EnchantCleaving("cleaving"));

    // Ulitmate Enchants

    HIGH_RISK_HIGH_RETURN = registerEnchant(new EnchantHighRiskHighReturn("high_risk_high_return"));

    CLOSE_CALL = registerEnchant(new EnchantCloseCall("close_call"));
  }

  private static CustomEnchant registerEnchant(@NotNull CustomEnchant enchant)
  {
    Enchantment.registerEnchantment(enchant);
    byKey.put(enchant.getKey(), enchant);
    byName.put(enchant.getName(), enchant);
    return enchant;
  }

  public static boolean isEnabled()
  {
    return GLOW != null;
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
  @SuppressWarnings("deprecation")
  public EnchantmentTarget getItemTarget()
  {
    return EnchantmentTarget.ALL;
  }

  @Override
  public boolean canEnchantItem(@NotNull ItemStack itemStack)
  {
    return true;
  }

  @Override
  @NotNull
  public final String getName()
  {
    return translationKey();
  }

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

  /**
   * @return true if this custom enchant is ultimate enchant.
   */
  public boolean isUltimate()
  {
    return this instanceof CustomEnchantUltimate;
  }

  public int enchantCost()
  {
    return 5;
  }

  public static void onEnable()
  {
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
