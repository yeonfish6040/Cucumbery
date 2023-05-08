package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemLore2Enchant
{
  protected static void setItemLore(@NotNull ItemStack item, @NotNull Material type, @NotNull ItemMeta itemMeta, @NotNull List<Component> lore, @Nullable Player viewer, boolean hideEnchant, boolean eventAccessMode)
  {
    if (itemMeta.hasEnchants())
    {
      itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
      if (!hideEnchant || eventAccessMode)
      {
        List<Component> enchantLore = new ArrayList<>();
        Map<Enchantment, Integer> enchants = new HashMap<>(itemMeta.getEnchants());
        enchants.keySet().removeIf(enchantment -> enchantment.equals(CustomEnchant.GLOW));
        int size = enchants.size();
        if (size >= 8)
        {
          StringBuilder key = new StringBuilder("&7");
          List<Component> args = new ArrayList<>();
          for (Enchantment enchantment : enchants.keySet())
          {
            key.append("%s, ");
            int level = itemMeta.getEnchantLevel(enchantment);
            if (level > 255)
            {
              level = 255;
            }
            if (level <= 0)
            {
              level = 1;
            }
            Component component;
            if (enchantment.getMaxLevel() == 1)
            {
              component = ComponentUtil.translate(enchantment.translationKey());
            }
            else
            {
              component = ComponentUtil.translate("%s %s", ComponentUtil.translate(enchantment.translationKey()), ComponentUtil.translate("enchantment.level." + level).fallback(level + ""));
            }
            component = component.color(enchantment.isCursed() ? TextColor.color(255, 85, 85) : TextColor.color(154, 84, 255));
            if (enchantment instanceof CustomEnchant customEnchant && customEnchant.isUltimate())
            {
              component = component.color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.BOLD, State.TRUE);
            }
            args.add(component);
            if (args.size() == 2)
            {
              key = new StringBuilder(key.substring(0, key.length() - 2));
              enchantLore.add(ComponentUtil.translate(key.toString(), args));
              key = new StringBuilder("&7");
              args = new ArrayList<>();
            }
          }
          if (!args.isEmpty())
          {
            key = new StringBuilder(key.substring(0, key.length() - 2));
            enchantLore.add(ComponentUtil.translate(key.toString(), args));
          }
        }
        else
        {
          for (Enchantment enchantment : itemMeta.getEnchants().keySet())
          {
            if (enchantment.equals(CustomEnchant.GLOW))
            {
              continue;
            }
            int level = itemMeta.getEnchantLevel(enchantment);
            if (level > 255)
            {
              level = 255;
            }
            if (level <= 0)
            {
              level = 1;
            }
            enchantLore.addAll(ItemLoreUtil.enchantTMIDescription(viewer, item, itemMeta, type, enchantment, level, viewer == null || UserData.SHOW_ENCHANTMENT_TMI_DESCRIPTION.getBoolean(viewer)));
          }
        }
        if (!enchantLore.isEmpty())
        {
          lore.add(Component.empty());
          if (hideEnchant)
          {
            lore.add(ComponentUtil.translate("&b관리자 권한으로 숨겨진 마법을 참조합니다"));
          }
          lore.add(ComponentUtil.translate(Constant.ITEM_LORE_ENCHANTED));
          lore.addAll(enchantLore);
        }
      }
    }

    ItemLoreEnchantRarity.enchantRarity(item, lore, type, itemMeta);

    if (type == Material.ENCHANTED_BOOK)
    {
      EnchantmentStorageMeta storageMeta = (EnchantmentStorageMeta) itemMeta;
      if (storageMeta.hasStoredEnchants())
      {
        itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        List<Component> enchantLore = new ArrayList<>();
        Map<Enchantment, Integer> enchants = new HashMap<>(storageMeta.getStoredEnchants());
        enchants.keySet().removeIf(enchantment -> enchantment.equals(CustomEnchant.GLOW));
        int size = enchants.size();
        if (size >= 8)
        {
          StringBuilder key = new StringBuilder("&7");
          List<Component> args = new ArrayList<>();
          for (Enchantment enchantment : enchants.keySet())
          {
            key.append("%s, ");
            int level = storageMeta.getStoredEnchantLevel(enchantment);
            if (level > 255)
            {
              level = 255;
            }
            if (level <= 0)
            {
              level = 1;
            }
            Component component;
            if (enchantment.getMaxLevel() == 1)
            {
              component = ComponentUtil.translate(enchantment.translationKey());
            }
            else
            {
              component = ComponentUtil.translate("%s %s", ComponentUtil.translate(enchantment.translationKey()), ComponentUtil.translate("enchantment.level." + level).fallback(level + ""));
            }
            component = component.color(enchantment.isCursed() ? TextColor.color(255, 85, 85) : TextColor.color(154, 84, 255));
            if (enchantment instanceof CustomEnchant customEnchant && customEnchant.isUltimate())
            {
              component = component.color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.BOLD, State.TRUE);
            }
            args.add(component);
            if (args.size() == 2)
            {
              key = new StringBuilder(key.substring(0, key.length() - 2));
              enchantLore.add(ComponentUtil.translate(key.toString(), args));
              key = new StringBuilder("&7");
              args = new ArrayList<>();
            }
          }
          if (!args.isEmpty())
          {
            key = new StringBuilder(key.substring(0, key.length() - 2));
            enchantLore.add(ComponentUtil.translate(key.toString(), args));
          }
        }
        else
        {
          for (Enchantment enchantment : enchants.keySet())
          {
            int level = storageMeta.getStoredEnchantLevel(enchantment);
            if (level > 255)
            {
              level = 255;
            }
            if (level <= 0)
            {
              level = 1;
            }
            enchantLore.addAll(ItemLoreUtil.enchantTMIDescription(viewer, item, itemMeta, type, enchantment, level, viewer == null || UserData.SHOW_ENCHANTMENT_TMI_DESCRIPTION.getBoolean(viewer)));
          }
        }
        if (!enchantLore.isEmpty())
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate(Constant.ITEM_LORE_STORED_ENCHANT));
          lore.addAll(enchantLore);
        }
        ItemLoreEnchantRarity.enchantedBookRarity(item, lore, type, storageMeta);
      }
      else
      {
        itemMeta.removeItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
      }
    }
  }
}
