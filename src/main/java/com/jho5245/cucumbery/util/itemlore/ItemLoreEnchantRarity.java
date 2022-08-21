package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.util.storage.data.Constant;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemLoreEnchantRarity
{
  @SuppressWarnings("all")
  public static void enchantRarity(@NotNull ItemStack itemStack, @NotNull List<Component> lore, @NotNull Material material, @NotNull ItemMeta itemMeta)
  {
    int enchantSize = itemMeta.getEnchants().size();
    double init = 10d;
//    for (int i = 0; i < enchantSize - 1; i++)
//    {
//      init = init * Math.pow(1.1, 2);
//    }
    long value = 0;
    value += enchantSize * (int) init;
    if (itemMeta.hasEnchant(Enchantment.BINDING_CURSE))
    {
      value -= 25;
    }
    if (itemMeta.hasEnchant(Enchantment.VANISHING_CURSE))
    {
      value -= 25;
    }
    if (Constant.DURABLE_ITEMS.contains(material) && itemMeta.hasEnchant(Enchantment.MENDING))
    {
      value += 200;
    }

    // 보호
    if (itemMeta.hasEnchant(Enchantment.PROTECTION_ENVIRONMENTAL))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.4, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }
      }
      value += level * (int) init;
    }
    // 폭발로부터 보호
    if (itemMeta.hasEnchant(Enchantment.PROTECTION_EXPLOSIONS))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.5, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 화염으로부터 보호
    if (itemMeta.hasEnchant(Enchantment.PROTECTION_FIRE))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.PROTECTION_FIRE);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.3, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 발사체로부터 보호
    if (itemMeta.hasEnchant(Enchantment.PROTECTION_PROJECTILE))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.4, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 가시
    if (itemMeta.hasEnchant(Enchantment.THORNS))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.THORNS);
      init = 15D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.3, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 호흡
    if (itemMeta.hasEnchant(Enchantment.OXYGEN))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.OXYGEN);
      init = 15D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.3, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 친수성
    if (itemMeta.hasEnchant(Enchantment.WATER_WORKER))
    {
      value += 100;
    }
    // 가벼운 착지
    if (itemMeta.hasEnchant(Enchantment.PROTECTION_FALL))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.PROTECTION_FALL);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.4, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 물갈퀴
    if (itemMeta.hasEnchant(Enchantment.DEPTH_STRIDER))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.DEPTH_STRIDER);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.5, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 신발 - 차가운 걸음
    if (itemMeta.hasEnchant(Enchantment.FROST_WALKER))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.FROST_WALKER);
      init = 80D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(2, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 신발 - 영혼 가속
    if (itemMeta.hasEnchant(Enchantment.SOUL_SPEED))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.SOUL_SPEED);
      init = 40D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.8, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 신발 -
    if (itemMeta.hasEnchant(Enchantment.SWIFT_SNEAK))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.SWIFT_SNEAK);
      init = 40D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.8, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 날카로움
    if (itemMeta.hasEnchant(Enchantment.DAMAGE_ALL))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.25, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 강타
    if (itemMeta.hasEnchant(Enchantment.DAMAGE_UNDEAD))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.15, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 살충
    if (itemMeta.hasEnchant(Enchantment.DAMAGE_ARTHROPODS))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.1, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 약탈
    if (itemMeta.hasEnchant(Enchantment.LOOT_BONUS_MOBS))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
      init = 25D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.8, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 검 - 휘몰아치는 칼날
    if (Constant.SWORDS.contains(material) && itemMeta.hasEnchant(Enchantment.SWEEPING_EDGE))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.SWEEPING_EDGE);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.6, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 발화
    if (itemMeta.hasEnchant(Enchantment.FIRE_ASPECT))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.FIRE_ASPECT);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.6, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 밀치기
    if (itemMeta.hasEnchant(Enchantment.KNOCKBACK))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.KNOCKBACK);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.3, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }
      }
      value += level * (int) init;
    }
    // 활 - 힘
    if (material == Material.BOW && itemMeta.hasEnchant(Enchantment.ARROW_DAMAGE))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.ARROW_DAMAGE);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.2, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 활 - 화염
    if (material == Material.BOW && itemMeta.hasEnchant(Enchantment.ARROW_FIRE))
    {
      value += 50;
    }
    // 활 - 무한
    if (material == Material.BOW && itemMeta.hasEnchant(Enchantment.ARROW_INFINITE))
    {
      value += 50;
    }
    // 활 - 밀어내기
    if (material == Material.BOW && itemMeta.hasEnchant(Enchantment.ARROW_KNOCKBACK))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK);
      init = 30D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.6, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 삼지창 - 찌르기
    if (material == Material.TRIDENT && itemMeta.hasEnchant(Enchantment.IMPALING))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.IMPALING);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.3, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 삼지창 - 충절
    if (material == Material.TRIDENT && itemMeta.hasEnchant(Enchantment.LOYALTY))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.LOYALTY);
      init = 10D;
      for (int i = 0; i < level - 1; i++)
      {
        init = init * Math.pow(1.4, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 삼지창 - 급류
    if (material == Material.TRIDENT && itemMeta.hasEnchant(Enchantment.RIPTIDE))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.RIPTIDE);
      init = 10D;
      for (int i = 0; i < level - 1; i++)
      {
        init = init * Math.pow(1.4, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 삼지창 - 집전
    if (material == Material.TRIDENT && itemMeta.hasEnchant(Enchantment.CHANNELING))
    {
      value += 80;
    }
    // 쇠뇌 - 빠른 장전
    if (material == Material.CROSSBOW && itemMeta.hasEnchant(Enchantment.QUICK_CHARGE))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.QUICK_CHARGE);
      if (level <= 5)
      {
        init = 10D;
        for (int i = 0; i < level - 1; i++)
        {
          init = init * Math.pow(1.8, i);
          if ((long) level * (int) init > 1_000_000_000)
          {
            init = 1_000_000_000 / level;
          }

        }
        value += level * (int) init;
      }
      else
      {
        value = -1_000_000_000;
      }
    }
    // 쇠뇌 - 관통
    if (material == Material.CROSSBOW && itemMeta.hasEnchant(Enchantment.PIERCING))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.PIERCING);
      init = 10D;
      for (int i = 0; i < level - 1; i++)
      {
        init = init * Math.pow(1.3, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 쇠뇌 - 다중 발사
    if (material == Material.CROSSBOW && itemMeta.hasEnchant(Enchantment.MULTISHOT))
    {
      value += 50;
    }
    // 도구 - 효율
    if (Constant.TOOLS.contains(material) && itemMeta.hasEnchant(Enchantment.DIG_SPEED))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.DIG_SPEED);
      init = 20D;
      for (int i = 0; i < level - 1; i++)
      {
        init = init * Math.pow(1.2, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 행운
    if (itemMeta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
      init = 30D;
      for (int i = 0; i < level - 1; i++)
      {
        init = init * Math.pow(1.8, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 섬세한 손길
    if (itemMeta.hasEnchant(Enchantment.SILK_TOUCH))
    {
      value += 50;
    }
    // 낚싯대 - 미끼
    if (material == Material.FISHING_ROD && itemMeta.hasEnchant(Enchantment.LURE))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.LURE);
      if (level <= 5)
      {
        init = 10D;
        for (int i = 0; i < level - 1; i++)
        {
          init = init * Math.pow(1.4, i);
          if ((long) level * (int) init > 1_000_000_000)
          {
            init = 1_000_000_000 / level;
          }

        }
        value += level * 30 + level * (int) init;
      }
      else
      {
        value = -1_000_000_000;
      }
    }
    // 낚싯대 - 바다의 행운
    if (material == Material.FISHING_ROD && itemMeta.hasEnchant(Enchantment.LUCK))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.LUCK);
      init = 25D;
      for (int i = 0; i < level - 1; i++)
      {
        init = init * Math.pow(2., i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 내구성
    if (Constant.DURABLE_ITEMS.contains(material) && itemMeta.hasEnchant(Enchantment.DURABILITY))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.DURABILITY);
      init = 20D;
      for (int i = 1; i < level; i++)
      {
        if (Constant.ARMORS.contains(material))
        {
          init = init * Math.pow(1.1, i);
        }
        else
        {
          init = init * Math.pow(1.3, i);
        }
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 내구성과 수선 동시 적용
    if (Constant.DURABLE_ITEMS.contains(material) && itemMeta.hasEnchant(Enchantment.DURABILITY) && itemMeta.hasEnchant(Enchantment.MENDING))
    {
      int level = itemStack.getEnchantmentLevel(Enchantment.DURABILITY);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.2, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }

    ItemLoreUtil.setItemRarityValue(lore, value);
  }

  @SuppressWarnings("all")
  public static void enchantedBookRarity(@NotNull ItemStack itemStack, @NotNull List<Component> lore, @NotNull Material material, @NotNull EnchantmentStorageMeta bookMeta)
  {
    int enchantSize = bookMeta.getEnchants().size();
    double init = 10d;
    long value = 0;
    value += enchantSize * (int) init;
    if (bookMeta.hasStoredEnchant(Enchantment.BINDING_CURSE))
    {
      value -= 25;
    }
    if (bookMeta.hasStoredEnchant(Enchantment.VANISHING_CURSE))
    {
      value -= 25;
    }
    if (bookMeta.hasStoredEnchant(Enchantment.MENDING))
    {
      value += 200;
    }

    // 갑옷 - 보호
    if (bookMeta.hasStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.4, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 갑옷 - 폭발로부터 보호
    if (bookMeta.hasStoredEnchant(Enchantment.PROTECTION_EXPLOSIONS))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.PROTECTION_EXPLOSIONS);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.3, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 갑옷 - 화염으로부터 보호
    if (bookMeta.hasStoredEnchant(Enchantment.PROTECTION_FIRE))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.PROTECTION_FIRE);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.3, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 갑옷 - 발사체로부터 보호
    if (bookMeta.hasStoredEnchant(Enchantment.PROTECTION_PROJECTILE))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.PROTECTION_PROJECTILE);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.4, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 갑옷 - 가시
    if (bookMeta.hasStoredEnchant(Enchantment.THORNS))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.THORNS);
      init = 15D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.3, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 투구 - 호흡
    if (bookMeta.hasStoredEnchant(Enchantment.OXYGEN))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.OXYGEN);
      init = 15D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.3, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 투구 - 친수성
    if (bookMeta.hasStoredEnchant(Enchantment.WATER_WORKER))
    {
      value += 100;
    }
    // 신발 - 가벼운 착지
    if (bookMeta.hasStoredEnchant(Enchantment.PROTECTION_FALL))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.PROTECTION_FALL);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.4, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 신발 - 물갈퀴
    if (bookMeta.hasStoredEnchant(Enchantment.DEPTH_STRIDER))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.DEPTH_STRIDER);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.4, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 신발 - 차가운 걸음
    if (bookMeta.hasStoredEnchant(Enchantment.FROST_WALKER))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.FROST_WALKER);
      init = 80D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(2, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 신발 - 영혼 가속
    if (bookMeta.hasStoredEnchant(Enchantment.SOUL_SPEED))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.SOUL_SPEED);
      init = 40D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.8, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 신발 - 신속한 잠행
    if (bookMeta.hasStoredEnchant(Enchantment.SWIFT_SNEAK))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.SWIFT_SNEAK);
      init = 40D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.8, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 검 - 날카로움
    if (bookMeta.hasStoredEnchant(Enchantment.DAMAGE_ALL))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.DAMAGE_ALL);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.3, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 검 - 강타
    if (bookMeta.hasStoredEnchant(Enchantment.DAMAGE_UNDEAD))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.DAMAGE_UNDEAD);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.25, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 검 - 살충
    if (bookMeta.hasStoredEnchant(Enchantment.DAMAGE_ARTHROPODS))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.DAMAGE_ARTHROPODS);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.1, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 검 - 약탈
    if (bookMeta.hasStoredEnchant(Enchantment.LOOT_BONUS_MOBS))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.LOOT_BONUS_MOBS);
      init = 25D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.8, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 검 - 휘몰아치는 칼날
    if (bookMeta.hasStoredEnchant(Enchantment.SWEEPING_EDGE))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.SWEEPING_EDGE);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.6, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 검 - 발화
    if (bookMeta.hasStoredEnchant(Enchantment.FIRE_ASPECT))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.FIRE_ASPECT);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.6, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 검 - 밀치기
    if (bookMeta.hasStoredEnchant(Enchantment.KNOCKBACK))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.KNOCKBACK);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.1, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 활 - 힘
    if (bookMeta.hasStoredEnchant(Enchantment.ARROW_DAMAGE))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.ARROW_DAMAGE);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.2, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 활 - 화염
    if (bookMeta.hasStoredEnchant(Enchantment.ARROW_FIRE))
    {
      value += 50;
    }
    // 활 - 무한
    if (bookMeta.hasStoredEnchant(Enchantment.ARROW_INFINITE))
    {
      value += 50;
    }
    // 활 - 밀어내기
    if (bookMeta.hasStoredEnchant(Enchantment.ARROW_KNOCKBACK))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.ARROW_KNOCKBACK);
      init = 30D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.6, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 삼지창 - 찌르기
    if (bookMeta.hasStoredEnchant(Enchantment.IMPALING))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.IMPALING);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.3, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 삼지창 - 충절
    if (bookMeta.hasStoredEnchant(Enchantment.LOYALTY))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.LOYALTY);
      init = 10D;
      for (int i = 0; i < level - 1; i++)
      {
        init = init * Math.pow(1.4, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 삼지창 - 급류
    if (bookMeta.hasStoredEnchant(Enchantment.RIPTIDE))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.RIPTIDE);
      init = 10D;
      for (int i = 0; i < level - 1; i++)
      {
        init = init * Math.pow(1.4, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 삼지창 - 집전
    if (bookMeta.hasStoredEnchant(Enchantment.CHANNELING))
    {
      value += 80;
    }
    // 쇠뇌 - 빠른 장전
    if (bookMeta.hasStoredEnchant(Enchantment.QUICK_CHARGE))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.QUICK_CHARGE);
      if (level <= 5)
      {
        init = 10D;
        for (int i = 0; i < level - 1; i++)
        {
          init = init * Math.pow(1.7, i);
          if ((long) level * (int) init > 1_000_000_000)
          {
            init = 1_000_000_000 / level;
          }

        }
        value += level * (int) init;
      }
      else
      {
        value = -1_000_000_000;
      }
    }
    // 쇠뇌 - 관통
    if (bookMeta.hasStoredEnchant(Enchantment.PIERCING))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.PIERCING);
      init = 10D;
      for (int i = 0; i < level - 1; i++)
      {
        init = init * Math.pow(1.5, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 쇠뇌 - 다중 발사
    if (bookMeta.hasStoredEnchant(Enchantment.MULTISHOT))
    {
      value += 50;
    }
    // 도구 - 효율
    if (bookMeta.hasStoredEnchant(Enchantment.DIG_SPEED))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.DIG_SPEED);
      init = 20D;
      for (int i = 0; i < level - 1; i++)
      {
        init = init * Math.pow(1.2, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }
      }
      value += level * (int) init;
    }
    // 도구 - 행운
    if (bookMeta.hasStoredEnchant(Enchantment.LOOT_BONUS_BLOCKS))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);
      init = 30D;
      for (int i = 0; i < level - 1; i++)
      {
        init = init * Math.pow(1.8, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 도구 - 섬세한 손길
    if (bookMeta.hasStoredEnchant(Enchantment.SILK_TOUCH))
    {
      value += 50;
    }
    // 낚싯대 - 미끼
    if (bookMeta.hasStoredEnchant(Enchantment.LURE))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.LURE);
      if (level <= 5)
      {
        init = 10D;
        for (int i = 0; i < level - 1; i++)
        {
          init = init * Math.pow(1.4, i);
          if ((long) level * (int) init > 1_000_000_000)
          {
            init = 1_000_000_000 / level;
          }

        }
        value += level * 30 + level * (int) init;
      }
      else
      {
        value = -1_000_000_000;
      }
    }
    // 낚싯대 - 바다의 행운
    if (bookMeta.hasStoredEnchant(Enchantment.LUCK))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.LUCK);
      init = 25D;
      for (int i = 0; i < level - 1; i++)
      {
        init = init * Math.pow(2., i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    // 내구성
    if (bookMeta.hasStoredEnchant(Enchantment.DURABILITY))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.DURABILITY);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.5, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }

    // 내구성과 수선 동시 적용
    if (bookMeta.hasStoredEnchant(Enchantment.DURABILITY) && bookMeta.hasStoredEnchant(Enchantment.MENDING))
    {
      int level = bookMeta.getStoredEnchantLevel(Enchantment.DURABILITY);
      init = 10D;
      for (int i = 1; i < level; i++)
      {
        init = init * Math.pow(1.1, i);
        if ((long) level * (int) init > 1_000_000_000)
        {
          init = 1_000_000_000 / level;
        }

      }
      value += level * (int) init;
    }
    ItemLoreUtil.setItemRarityValue(lore, value);
  }
}
