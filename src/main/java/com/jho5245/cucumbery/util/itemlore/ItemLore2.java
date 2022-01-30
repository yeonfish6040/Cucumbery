package com.jho5245.cucumbery.util.itemlore;

import com.destroystokyo.paper.Namespaced;
import com.google.common.collect.Multimap;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.Method2;
import com.jho5245.cucumbery.util.PlaceHolderUtil;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.ColorCode;
import com.jho5245.cucumbery.util.storage.ItemCategory;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.RecipeChecker;
import com.jho5245.cucumbery.util.storage.component.ItemStackComponent;
import com.jho5245.cucumbery.util.storage.component.LocationComponent;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.CucumberyHideFlag;
import com.jho5245.cucumbery.util.storage.data.Constant.ExtraTag;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import com.jho5245.cucumbery.util.storage.data.custom_enchant.CustomEnchant;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ItemLore2
{
  protected static ItemStack setItemLore(@NotNull ItemStack item, ItemMeta itemMeta, List<Component> lore, @Nullable Object params)
  {
    Player player = params instanceof Player p ? p : null;
    Material type = item.getType();
    NBTItem nbtItem = new NBTItem(item.clone());
    @Nullable NBTCompound itemTag = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
    if (lore == null)
    {
      lore = new ArrayList<>();
    }

    long defaultConfigDura = Cucumbery.config.getLong("custom-item-durability." + type);
    if (defaultConfigDura == 0)
    {
      String defaultConfigDuraStr = Cucumbery.config.getString("custom-item-durability." + type);
      if (defaultConfigDuraStr != null)
      {
        defaultConfigDuraStr = PlaceHolderUtil.placeholder(Bukkit.getConsoleSender(), defaultConfigDuraStr, null);
        defaultConfigDuraStr = PlaceHolderUtil.evalString(defaultConfigDuraStr);
        try
        {
          defaultConfigDura = Long.parseLong(defaultConfigDuraStr);
        }
        catch (Exception ignored)
        {

        }
      }
    }

    NBTCompound duraTag = itemTag != null ? itemTag.getCompound(CucumberyTag.CUSTOM_DURABILITY_KEY) : null;
    if (defaultConfigDura > 0 && duraTag == null)
    {
      if (itemTag == null)
      {
        itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
      }
      duraTag = itemTag.addCompound(CucumberyTag.CUSTOM_DURABILITY_KEY);
      duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, defaultConfigDura);
      duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY, defaultConfigDura);
      itemMeta = nbtItem.getItem().getItemMeta();
    }

    NBTList<String> extraTags = NBTAPI.getStringList(itemTag, CucumberyTag.EXTRA_TAGS_KEY);
    if (player != null && NBTAPI.arrayContainsValue(extraTags, ExtraTag.NAME_TAG))
    {
      lore.add(Component.empty());
      lore.add(ComponentUtil.translate("&a새겨진 이름 : %s", player.displayName().hoverEvent(null).clickEvent(null)));
    }

    NBTCompound customRarityTag = NBTAPI.getCompound(itemTag, CucumberyTag.CUSTOM_RARITY_KEY);
    String customRarityBase = NBTAPI.getString(customRarityTag, CucumberyTag.CUSTOM_RARITY_BASE_KEY);
    NBTList<String> hideFlags = NBTAPI.getStringList(itemTag, CucumberyTag.HIDE_FLAGS_KEY);
    boolean hideFlagsTagExists = hideFlags != null;
    if (customRarityBase != null)
    {
      ItemLoreUtil.setItemRarityValue(lore, ItemCategory.Rarity.valueOf(customRarityBase).getRarityValue(), false);
    }

    // CucumberyItemTag - AboveCustomLore
    if (!NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.ABOVE_CUSTOM_LORE))
    {
      NBTList<String> aboveCustomLores = NBTAPI.getStringList(itemTag, CucumberyTag.ABOVE_CUSTOM_LORE_KEY);
      if (aboveCustomLores != null && !aboveCustomLores.isEmpty())
      {
        for (Object customLore : aboveCustomLores)
        {
          lore.add(ComponentUtil.create(customLore.toString()));
        }
      }
    }

    String expireDate = NBTAPI.getString(itemTag, CucumberyTag.EXPIRE_DATE_KEY);
    if (expireDate != null && !NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.EXPIRE_DATE))
    {
      boolean hideAbsolute = NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.EXPIRE_DATE_ABSOLUTE);
      boolean hideRelative = NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.EXPIRE_DATE_RELATIVE);
      if (!hideAbsolute || !hideRelative)
      {
        lore.add(Component.empty());
        String prefix = "&e유효 기간 : ";
        boolean relative = expireDate.startsWith("~");
        if (relative)
        {
          prefix += "획득 후 ";
          expireDate = expireDate.replace("~", "");
        }
        long expireMills = Method.getTimeDifference(Calendar.getInstance(), expireDate);
        if (!relative && expireMills <= 20000)
        {
          lore.add(ComponentUtil.create("&e유효 기간 : 유효 기간이 만료되었습니다"));
        }
        else
        {
          String relativeDate = "";
          if (!relative && !hideAbsolute && !hideRelative)
          {
            relativeDate = "(" + Method.timeFormatMilli(expireMills) + " 남음)";
          }

          if (!relative && !hideRelative && hideAbsolute)
          {
            expireDate = Method.timeFormatMilli(expireMills);
          }
          lore.add(ComponentUtil.create(prefix + expireDate + relativeDate + (hideAbsolute || relative ? "동안" : "까지") + " 사용 가능"));
        }
      }
    }

    if (!NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.RESTRICTION.toString()))
    {
      NBTCompoundList restrictionTags = NBTAPI.getCompoundList(NBTAPI.getMainCompound(item), CucumberyTag.ITEM_USAGE_RESTRICTIONS_KEY);
      if (restrictionTags != null && !restrictionTags.isEmpty())
      {
        StringBuilder restrictionTagLore = new StringBuilder();
        for (NBTCompound restrictionTag : restrictionTags)
        {
          String value = restrictionTag.getString(CucumberyTag.VALUE_KEY);
          for (RestrictionType restrictionType : RestrictionType.values())
          {
            if (restrictionType.toString().equals(value))
            {
              if (!NBTAPI.hideFromLore(restrictionTag))
              {
                restrictionTagLore.append(restrictionType.getTag());
              }
            }
          }
        }
        if (!restrictionTagLore.toString().equals(""))
        {
          String restrictionTagLoreString = restrictionTagLore.toString();
          if (type.isEdible())
          {
            restrictionTagLoreString = restrictionTagLoreString.replace("제련 불가", "조리 불가");
          }
          lore.add(Component.empty());
          lore.add(ComponentUtil.create("rgb200,30,30;" + restrictionTagLoreString));
        }
      }
    }

    boolean hideDurability = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.DURABILITY.toString());
    boolean hideDurabilityChanceNotToConsume = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.DURABILITY_CHANCE_NOT_TO_CONSUME.toString());

    if (itemMeta.isUnbreakable())
    {
      ItemLoreUtil.setItemRarityValue(lore, ItemCategory.Rarity.UNIQUE.getRarityValue());
      if (!hideDurability)
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("rgb225,100,205;내구도 : %s / %s", Component.text("∞"), Component.text("∞")));
      }
      itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
    }
    else if (Constant.DURABLE_ITEMS.contains(type) || duraTag != null)
    {
      long currentDurability = 0, maxDurability = 0;
      double chanceNotToConsumeDura = 0d;
      boolean duraTagExists = duraTag != null;
      if (duraTagExists)
      {
        try
        {
          maxDurability = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY);
          currentDurability = duraTag.getLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY);
        }
        catch (Exception e)
        {
          currentDurability = 0;
          maxDurability = 0;
        }
        try
        {
          chanceNotToConsumeDura = duraTag.getDouble(CucumberyTag.CUSTOM_DURABILITY_CHANCE_NOT_TO_CONSUME_DURABILITY);
        }
        catch (Exception e)
        {
          chanceNotToConsumeDura = 0d;
        }

        if (maxDurability != 0)
        {
          int originMaxDura = type.getMaxDurability();
          double originItemDuraDouble = (originMaxDura * ((maxDurability - currentDurability) * 1d / maxDurability));
          if (originItemDuraDouble > 0d && originItemDuraDouble < 1d)
          {
            originItemDuraDouble = 1d;
          }
          if (type == Material.ELYTRA)
          {
            if (currentDurability == 1)
            {
              originItemDuraDouble = Material.ELYTRA.getMaxDurability() - 1;
            }
            else if (currentDurability > 1 && originItemDuraDouble > 430)
            {
              originItemDuraDouble = Material.ELYTRA.getMaxDurability() - 2;
            }
          }
          Damageable damageable = (Damageable) itemMeta;
          damageable.setDamage((int) originItemDuraDouble);
          item.setItemMeta(damageable);
//          long duraDifference = originMaxDura - maxDurability;
//          if (duraDifference > 0)
//          {
//            ItemLoreUtil.setItemRarityValue(lore, (long) Math.pow(duraDifference / (originMaxDura == 0 ? 60d : 30d), 2));
//          }
//          else
//          {
//            ItemLoreUtil.setItemRarityValue(lore, (long) -Math.pow(Math.abs(duraDifference / 30d), 1.05));
//          }
        }
      }

      if (maxDurability == 0 && Constant.DURABLE_ITEMS.contains(type))
      {
        maxDurability = type.getMaxDurability();
        currentDurability = maxDurability - ((Damageable) item.getItemMeta()).getDamage();
      }

      if (maxDurability != 0 || Constant.DURABLE_ITEMS.contains(type))
      {
        if (!hideDurability)
        {
          lore.add(Component.empty());
          String color = Method2.getPercentageColor(currentDurability, maxDurability);
          lore.add(ComponentUtil.translate("&e내구도 : %s",
                  ComponentUtil.translate("&7%s / %s", color + Constant.Jeongsu.format(currentDurability), "g255;" + Constant.Jeongsu.format(maxDurability))));
        }
      }

      int dura = 0;
      if (itemMeta.hasEnchant(Enchantment.DURABILITY))
      {
        dura = itemMeta.getEnchantLevel(Enchantment.DURABILITY);
      }
      if (itemMeta.hasEnchant(Enchantment.MENDING))
      {
        dura += 9;
      }
      double ratio = 1d * currentDurability / maxDurability;
      // 내구도로 인한 아이템 등급 수치 감소에서는 내구도가 꽉 찼을 때 비율이 0으로 되게 함
      ItemLoreUtil.setItemRarityValue(lore, (long) (ItemLoreUtil.getItemRarityValue(lore) * 0.01 + maxDurability * 0.05));
      long duraNegative = (long) ((1 + ItemLoreUtil.getItemRarityValue(lore) / 10000) * Math.pow(1.055 - (dura / 1000d), (100 - ratio * 100)) - 1);
      //(long) Math.pow(ratio * (2.0 + (20.0 / maxDurability)), Math.abs(Math.pow(3.0 - dura / 10.0, Math.abs(ratio)) + 1.7 + (200.0 / maxDurability) - maxDurability / 1300.0)); // 내구도로 인한 아이템 등급 수치 감소
      if (duraNegative > 0)
      {
        ItemLoreUtil.setItemRarityValue(lore, -duraNegative);
      }
      if (maxDurability != 0 && chanceNotToConsumeDura > 0d && chanceNotToConsumeDura <= 100d)
      {
        if (!hideDurabilityChanceNotToConsume)
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.create("&a내구도 감소 무효 확률 : +" + Constant.Sosu4.format(chanceNotToConsumeDura) + "%"));
        }
      }
    }
    else if (type != Material.MAP)
    {
      if (itemMeta instanceof Damageable duraMeta)
      {
        if (duraMeta.getDamage() != 0)
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&6내구도 손상 : %s", duraMeta.getDamage() + ""));
        }
      }
    }

    int anvilUsedTime = ItemStackUtil.getAnvilUsedTime(itemMeta);
    if (anvilUsedTime > 0)
    {
      if (anvilUsedTime > 30)
      {
        itemMeta.displayName(Component.translatable(""));
        itemMeta.lore(null);
        item.setItemMeta(itemMeta);
        return item;
      }
      long penalty = switch (anvilUsedTime)
              {
                case 1 -> 20;
                case 2 -> 40;
                case 3 -> 60;
                case 4 -> 100;
                case 5 -> 200;
                default -> (long) Math.pow(2, anvilUsedTime + 3);
              };
      // 아이템의 현재 내구도의 비율에 따른 아이템 희귀도 감소
      int maxDura = type.getMaxDurability();
      if (maxDura != -1 && itemMeta instanceof Damageable)
      {
        int dura = ((Damageable) itemMeta).getDamage();
        penalty *= dura * 1d / maxDura;
        // 내구도가 높을 수록 희귀도 차감량 감소
        double what = dura / 200d;
        if (what < 0.5d)
        {
          what = 0.5d;
        }
        if (what > 0d)
        {
          penalty /= what;
        }
      }

      // 수선이 있을 경우 모루 합성 횟수로 인한 아이템 희귀도 차감량 반감
      if (itemMeta.hasEnchant(Enchantment.MENDING) && itemMeta.getEnchantLevel(Enchantment.MENDING) > 0)
      {
        penalty /= 2.0;
      }
      // 내구성이 있을 경우 모루 합성 횟수로 인한 아이템 희귀도 차감량 감소
      int duraEnch = -1;
      if (itemMeta.hasEnchant(Enchantment.DURABILITY))
      {
        duraEnch = itemMeta.getEnchantLevel(Enchantment.DURABILITY);
      }
      if (duraEnch > 0)
      {
        penalty /= duraEnch;
      }

      ItemLoreUtil.setItemRarityValue(lore, -penalty);

      lore.add(Component.empty());
      lore.add(ComponentUtil.translate("rgb203,164,12;누적 모루 합성 횟수 : %s", ComponentUtil.translate("&e%s회", anvilUsedTime + "")));
    }

    boolean hideEnchant = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.ENCHANTS.toString());
    NBTCompoundList customEnchantsTag = NBTAPI.getCompoundList(itemTag, CucumberyTag.CUSTOM_ENCHANTS_KEY);
    boolean hasCustomEnchants = customEnchantsTag != null && !customEnchantsTag.isEmpty();

    if (itemMeta.hasEnchants())
    {
      itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    if (itemMeta.hasEnchants() || hasCustomEnchants)
    {
      if (!hideEnchant)
      {
        List<Component> enchantLore = new ArrayList<>();
        if (hasCustomEnchants)
        {
          for (NBTCompound customEnchant : customEnchantsTag)
          {
            String id = customEnchant.getString(CucumberyTag.ID_KEY);
            Integer level = customEnchant.getInteger(CucumberyTag.CUSTOM_ENCHANTS_LEVEL_KEY);
            if (level > 255)
            {
              level = 255;
            }
            if (level <= 0)
            {
              level = 1;
            }
            enchantLore.addAll(ItemLoreUtil.enchantTMIDescription(itemMeta, type, id, level));
          }
        }
        for (Enchantment enchantment : itemMeta.getEnchants().keySet())
        {
          if (enchantment.equals(CustomEnchant.GLOW))
          {
            continue;
          }
          if (type == Material.BOW && enchantment.equals(Enchantment.RIPTIDE))
          {
            continue;
          }
          if (type != Material.BOW && enchantment.equals(Enchantment.ARROW_INFINITE))
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
          enchantLore.addAll(ItemLoreUtil.enchantTMIDescription(itemMeta, type, enchantment, level));
        }
        if (!enchantLore.isEmpty())
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate(Constant.ITEM_LORE_ENCHANTED));
          lore.addAll(enchantLore);
        }
      }
    }

    ItemLoreEnchantRarity.enchantRarity(item, lore, type, itemMeta);

    if (type == Material.ENCHANTED_BOOK)
    {
      if (((EnchantmentStorageMeta) itemMeta).hasStoredEnchants())
      {
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate(Constant.ITEM_LORE_STORED_ENCHANT));
        for (Enchantment enchant : Enchantment.values())
        {
          if (((EnchantmentStorageMeta) itemMeta).hasStoredEnchant(enchant))
          {
            int level = ((EnchantmentStorageMeta) itemMeta).getStoredEnchantLevel(enchant);
            if (level > 255)
            {
              level = 255;
            }
            if (level <= 0)
            {
              level = 1;
            }
            lore.addAll(ItemLoreUtil.enchantTMIDescription(itemMeta, type, enchant, level));
          }
        }
        ItemLoreEnchantRarity.enchantedBookRarity(item, lore, type, (EnchantmentStorageMeta) itemMeta);
      }
      else
      {
        itemMeta.removeItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
      }
    }

    if (!Constant.DEFAULT_MODIFIER_ITEMS.contains(type) && !itemMeta.hasAttributeModifiers())
    {
      itemMeta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    }
    else
    {
      itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
      if (!Constant.DEFAULT_MODIFIER_ITEMS.contains(type) || (Constant.DEFAULT_MODIFIER_ITEMS.contains(type) && itemMeta.hasAttributeModifiers()))
      {
        for (EquipmentSlot slot : EquipmentSlot.values())
        {
          Multimap<Attribute, AttributeModifier> attrs = itemMeta.getAttributeModifiers(slot);
          if (attrs.isEmpty())
          {
            continue;
          }
          lore.add(Component.empty());
          switch (slot)
          {
            case HAND -> lore.add(Constant.ITEM_MODIFIERS_MAINHAND);
            case OFF_HAND -> lore.add(Constant.ITEM_MODIFIERS_OFFHAND);
            case FEET -> lore.add(Constant.ITEM_MODIFIERS_FEET);
            case LEGS -> lore.add(Constant.ITEM_MODIFIERS_LEGS);
            case CHEST -> lore.add(Constant.ITEM_MODIFIERS_CHEST);
            case HEAD -> lore.add(Constant.ITEM_MODIFIERS_HEAD);
          }
          for (Attribute attribute : Attribute.values())
          {
            Collection<AttributeModifier> attr = attrs.get(attribute);
            for (AttributeModifier modifier : attr)
            {
              double amount = modifier.getAmount();
              if (amount == 0d)
              {
                continue;
              }
              AttributeModifier.Operation operation = modifier.getOperation();
              if (operation != AttributeModifier.Operation.ADD_NUMBER)
              {
                amount *= 100d;
              }
              String operationString = ItemLoreUtil.operationValue(operation);
              Component component = ComponentUtil.translate("rgb255,142,82;%s : %s",
                      Component.translatable(attribute.translationKey()), (amount > 0 ? "+" : "") + Constant.Sosu2.format(amount) + operationString);
              lore.add(component);
            }
          }
        }
      }
      else if (!itemMeta.hasAttributeModifiers())
      {
        Component mainHand = Constant.ITEM_MODIFIERS_MAINHAND;
        Component helmet = Constant.ITEM_MODIFIERS_HEAD;
        Component chestplate = Constant.ITEM_MODIFIERS_CHEST;
        Component leggings = Constant.ITEM_MODIFIERS_LEGS;
        Component boots = Constant.ITEM_MODIFIERS_FEET;
        Component attackSpeed = Component.translatable("attribute.name.generic.attack_speed");
        Component damage = Component.translatable("attribute.name.generic.attack_damage");
        Component armor = Component.translatable("attribute.name.generic.armor");
        Component armorToughness = Component.translatable("attribute.name.generic.armor_toughness");
        Component knockbackResistance = Component.translatable("attribute.name.generic.knockback_resistance");
        lore.add(Component.empty());
        switch (type)
        {
          case WOODEN_AXE:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+6"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3.2"));
            break;
          case STONE_AXE:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+8"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3.2"));
            break;
          case IRON_AXE:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+8"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3.1"));
            break;
          case DIAMOND_AXE:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+8"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3"));
            break;
          case GOLDEN_AXE:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+6"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3"));
            break;
          case WOODEN_PICKAXE:
          case GOLDEN_PICKAXE:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+1"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.8"));
            break;
          case STONE_PICKAXE:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+2"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.8"));
            break;
          case IRON_PICKAXE:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+3"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.8"));
            break;
          case DIAMOND_PICKAXE:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+4"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.8"));
            break;
          case WOODEN_SHOVEL:
          case GOLDEN_SHOVEL:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+1.5"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3"));
            break;
          case STONE_SHOVEL:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+2.5"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3"));
            break;
          case IRON_SHOVEL:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+3.5"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3"));
            break;
          case DIAMOND_SHOVEL:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+4.5"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3"));
            break;
          case WOODEN_HOE:
          case GOLDEN_HOE:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3"));
            break;
          case STONE_HOE:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2"));
            break;
          case IRON_HOE:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-1"));
            break;
          case DIAMOND_HOE:
          case NETHERITE_HOE:
            lore.remove(lore.size() - 1);
            break;
          case WOODEN_SWORD:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+3"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.4"));
            break;
          case STONE_SWORD:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+4"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.4"));
            break;
          case IRON_SWORD:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+5"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.4"));
            break;
          case DIAMOND_SWORD:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+6"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.4"));
            break;
          case GOLDEN_SWORD:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+3"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.4"));
            break;
          case TRIDENT:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+8"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.9"));
            break;
          case LEATHER_HELMET:
            lore.add(helmet);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+1"));
            break;
          case LEATHER_CHESTPLATE:
            lore.add(chestplate);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+3"));
            break;
          case LEATHER_LEGGINGS:
            lore.add(leggings);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+2"));
            break;
          case LEATHER_BOOTS:
          case CHAINMAIL_BOOTS:
          case GOLDEN_BOOTS:
            lore.add(boots);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+1"));
            break;
          case CHAINMAIL_HELMET:
          case TURTLE_HELMET:
          case IRON_HELMET:
          case GOLDEN_HELMET:
            lore.add(helmet);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+2"));
            break;
          case CHAINMAIL_CHESTPLATE:
          case GOLDEN_CHESTPLATE:
            lore.add(chestplate);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+5"));
            break;
          case CHAINMAIL_LEGGINGS:
            lore.add(leggings);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+4"));
            break;
          case IRON_CHESTPLATE:
            lore.add(chestplate);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+6"));
            break;
          case IRON_LEGGINGS:
            lore.add(leggings);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+5"));
            break;
          case IRON_BOOTS:
            lore.add(boots);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+2"));
            break;
          case GOLDEN_LEGGINGS:
            lore.add(leggings);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+3"));
            break;
          case DIAMOND_HELMET:
            lore.add(helmet);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armorToughness, "+2"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+3"));
            break;
          case DIAMOND_CHESTPLATE:
            lore.add(chestplate);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+8"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armorToughness, "+2"));
            break;
          case DIAMOND_LEGGINGS:
            lore.add(leggings);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+6"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armorToughness, "+2"));
            break;
          case DIAMOND_BOOTS:
            lore.add(boots);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+3"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armorToughness, "+2"));
            break;
          case NETHERITE_AXE:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+9"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3"));
            break;
          case NETHERITE_PICKAXE:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+5"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.8"));
            break;
          case NETHERITE_SHOVEL:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+5.5"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-3"));
            break;
          case NETHERITE_SWORD:
            lore.add(mainHand);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", damage, "+7"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", attackSpeed, "-2.4"));
            break;
          case NETHERITE_HELMET:
            lore.add(helmet);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+3"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armorToughness, "+3"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", knockbackResistance, "+1"));
            break;
          case NETHERITE_CHESTPLATE:
            lore.add(chestplate);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+8"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armorToughness, "+3"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", knockbackResistance, "+1"));
            break;
          case NETHERITE_LEGGINGS:
            lore.add(leggings);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+6"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armorToughness, "+3"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", knockbackResistance, "+1"));
            break;
          case NETHERITE_BOOTS:
            lore.add(boots);
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armor, "+3"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", armorToughness, "+3"));
            lore.add(ComponentUtil.translate("rgb255,142,82;%s : %s", knockbackResistance, "+1"));
            break;
          default:
            break;
        }
      }
    }

    boolean hideStatusEffects = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.STATUS_EFFECTS.toString());
    boolean hideFireworkEffects = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.FIREWORK_EFFECTS.toString());
    boolean hideFireworkFlightTime = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.FIREWORK_FLIGHT_TIME.toString());
    boolean hideBannerPatterns = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.BANNER_PATTERN.toString());
    boolean hideBannerPatternItem = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.BANNER_PATTERN_ITEM.toString());
    boolean hideStorageContents = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.STORAGE_CONTENTS.toString());
    boolean hideBookTag = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.BOOK_TAG.toString());
    boolean hideMusicDisc = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.MUSIC_DISC.toString());
    boolean hideTotemOfUndying = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.TOTEM_OF_UNDYING.toString());
    boolean hideBlockData = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.BLOCK_DATA);
    boolean hideBlockState = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.BLOCK_STATE);


    // 음식 추가 상태 효과

    NBTCompound foodTag = NBTAPI.getCompound(itemTag, CucumberyTag.FOOD_KEY);

    List<Component> foodLore = new ArrayList<>();

    if (!hideStatusEffects && (!NBTAPI.isRestricted(item, RestrictionType.NO_CONSUME) || NBTAPI.getRestrictionOverridePermission(item, RestrictionType.NO_CONSUME) != null)
            && (foodTag == null || !foodTag.hasKey(CucumberyTag.FOOD_DISABLE_STATUS_EFFECT_KEY) || !foodTag.getBoolean(CucumberyTag.FOOD_DISABLE_STATUS_EFFECT_KEY)))
    {
      switch (type)
      {
        case GOLDEN_APPLE:
          foodLore.addAll(Arrays.asList(
                  ItemLorePotionDescription.getDescription(100d, ItemLorePotionDescription.REGENERATION, 5 * 20, 2),
                  ItemLorePotionDescription.getDescription(100d, ItemLorePotionDescription.ABSORPTION, 2 * 60 * 20, 1)));
          break;
        case ENCHANTED_GOLDEN_APPLE:
          foodLore.addAll(Arrays.asList(
                  ItemLorePotionDescription.getDescription(100d, ItemLorePotionDescription.REGENERATION, 20 * 20, 2),
                  ItemLorePotionDescription.getDescription(100d, ItemLorePotionDescription.ABSORPTION, 2 * 60 * 20, 4),
                  ItemLorePotionDescription.getDescription(100d, ItemLorePotionDescription.RESISTANCE, 5 * 60 * 20, 1),
                  ItemLorePotionDescription.getDescription(100d, ItemLorePotionDescription.FIRE_RESISTANCE, 5 * 60 * 20, 1)));
          break;
        case POISONOUS_POTATO:
          foodLore.addAll(List.of(
                  ItemLorePotionDescription.getDescription(60d, ItemLorePotionDescription.POISON, 4 * 20, 1)));
          break;
        case SPIDER_EYE:
          foodLore.addAll(List.of(
                  ItemLorePotionDescription.getDescription(100d, ItemLorePotionDescription.POISON, 4 * 20, 1)));
          break;
        case PUFFERFISH:
          foodLore.addAll(Arrays.asList(
                  ItemLorePotionDescription.getDescription(100d, ItemLorePotionDescription.HUNGER, 15 * 20, 3),
                  ItemLorePotionDescription.getDescription(100d, ItemLorePotionDescription.NAUSEA, 15 * 20, 2),
                  ItemLorePotionDescription.getDescription(100d, ItemLorePotionDescription.POISON, 60 * 20, 4)));
          break;
        case ROTTEN_FLESH:
          foodLore.addAll(List.of(
                  ItemLorePotionDescription.getDescription(80d, ItemLorePotionDescription.HUNGER, 30 * 20, 1)));
          break;
        case CHICKEN:
          foodLore.addAll(List.of(
                  ItemLorePotionDescription.getDescription(30d, ItemLorePotionDescription.HUNGER, 30 * 20, 1)));
          break;
        case HONEY_BOTTLE:
          foodLore.addAll(List.of(
                  ComponentUtil.translate("rgb255,97,144;%s 확률로 %s 효과 제거", "100%", ItemLorePotionDescription.getComponent(PotionEffectType.POISON))));
          break;
        case MILK_BUCKET:
          foodLore.addAll(List.of(
                  ComponentUtil.translate("rgb255,97,144;%s 확률로 모든 효과 제거", "100%")));
          break;
        default:
          break;
      }
    }

    if (ItemStackUtil.isEdible(type) && type != Material.POTION && type != Material.SUSPICIOUS_STEW)
    {
      foodLore.addAll(ItemLorePotionDescription.getCustomEffectList(item));
    }

    if (!foodLore.isEmpty())
    {
      lore.addAll(Arrays.asList(Component.empty(), ComponentUtil.translate(Constant.ITEM_LORE_STATUS_EFFECT)));
      lore.addAll(foodLore);
    }

    switch (type)
    {
      case POTION -> {
        if (!hideStatusEffects)
        {
          lore.addAll(ItemLorePotionDescription.getPotionList(item));
        }
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        PotionMeta potionMeta = (PotionMeta) itemMeta;
        PotionData data = potionMeta.getBasePotionData();
        PotionType potionType = data.getType();
        if (!(potionType == PotionType.AWKWARD || potionType == PotionType.MUNDANE || potionType == PotionType.THICK || potionType == PotionType.UNCRAFTABLE || potionType == PotionType.WATER))
        {
          ItemLoreUtil.setItemRarityValue(lore, +50);
        }
        if (data.isExtended())
        {
          ItemLoreUtil.setItemRarityValue(lore, +50);
        }
        if (data.isUpgraded())
        {
          ItemLoreUtil.setItemRarityValue(lore, +50);
        }
        for (PotionEffect effect : potionMeta.getCustomEffects())
        {
          ItemLoreUtil.setItemRarityValue(lore, 10L * ((effect.getDuration() / 200) + 1) * (effect.getAmplifier() + 1));
        }
      }
      case SPLASH_POTION -> {
        if (!hideStatusEffects)
        {
          lore.addAll(ItemLorePotionDescription.getSplashPotionList(item));
        }
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        PotionMeta potionMeta = (PotionMeta) itemMeta;
        PotionData data = potionMeta.getBasePotionData();
        PotionType potionType = data.getType();
        if (!(potionType == PotionType.AWKWARD || potionType == PotionType.MUNDANE || potionType == PotionType.THICK || potionType == PotionType.UNCRAFTABLE || potionType == PotionType.WATER))
        {
          ItemLoreUtil.setItemRarityValue(lore, 50);
        }
        if (data.isExtended())
        {
          ItemLoreUtil.setItemRarityValue(lore, 50);
        }
        if (data.isUpgraded())
        {
          ItemLoreUtil.setItemRarityValue(lore, 50);
        }
        for (PotionEffect effect : potionMeta.getCustomEffects())
        {
          ItemLoreUtil.setItemRarityValue(lore, 10L * ((effect.getDuration() / 200) + 1) * (effect.getAmplifier() + 1));
        }
      }
      case LINGERING_POTION -> {
        if (!hideStatusEffects)
        {
          lore.addAll(ItemLorePotionDescription.getLingeringPotionList(item));
        }
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        PotionMeta potionMeta = (PotionMeta) itemMeta;
        PotionData data = potionMeta.getBasePotionData();
        PotionType potionType = data.getType();
        if (!(potionType == PotionType.AWKWARD || potionType == PotionType.MUNDANE || potionType == PotionType.THICK || potionType == PotionType.UNCRAFTABLE || potionType == PotionType.WATER))
        {
          ItemLoreUtil.setItemRarityValue(lore, +50);
        }
        if (data.isExtended())
        {
          ItemLoreUtil.setItemRarityValue(lore, 50);
        }
        if (data.isUpgraded())
        {
          ItemLoreUtil.setItemRarityValue(lore, 50);
        }
        for (PotionEffect effect : potionMeta.getCustomEffects())
        {
          ItemLoreUtil.setItemRarityValue(lore, 10L * ((effect.getDuration() / 200) + 1) * (effect.getAmplifier() + 1));
        }
      }
      case TIPPED_ARROW -> {
        if (!hideStatusEffects)
        {
          lore.addAll(ItemLorePotionDescription.getTippedArrowList(item));
        }
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        PotionMeta potionMeta = (PotionMeta) itemMeta;
        PotionData data = potionMeta.getBasePotionData();
        PotionType potionType = data.getType();
        if (!(potionType == PotionType.AWKWARD || potionType == PotionType.MUNDANE || potionType == PotionType.THICK || potionType == PotionType.UNCRAFTABLE || potionType == PotionType.WATER))
        {
          ItemLoreUtil.setItemRarityValue(lore, 50);
        }
        if (data.isExtended())
        {
          ItemLoreUtil.setItemRarityValue(lore, 50);
        }
        if (data.isUpgraded())
        {
          ItemLoreUtil.setItemRarityValue(lore, 50);
        }
        for (PotionEffect effect : potionMeta.getCustomEffects())
        {
          ItemLoreUtil.setItemRarityValue(lore, 10L * ((effect.getDuration() / 200) + 1) * (effect.getAmplifier() + 1));
        }
      }
      case SPECTRAL_ARROW -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate(Constant.ITEM_LORE_STATUS_EFFECT));
        lore.add(ItemLorePotionDescription.getDescription(ItemLorePotionDescription.GLOWING, 10 * 20));
      }
      case WRITABLE_BOOK -> {
        // 야생에서는 불가능. 명령어로 강제로 책의 서명을 없앨때만 생기는 현상
        if (itemMeta.hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS))
        {
          itemMeta.removeItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        }
        BookMeta bookMeta = (BookMeta) item.getItemMeta();
        if (bookMeta.hasPages())
        {
          int pageCount = bookMeta.getPageCount();
          ItemLoreUtil.setItemRarityValue(lore, pageCount);
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&7쪽수 : %s", ComponentUtil.translate("&6%s장", pageCount)));
        }
      }
      case WRITTEN_BOOK -> {
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        BookMeta bookMeta = (BookMeta) item.getItemMeta();
        Component author = bookMeta.author();
        if (author != null && author.color() == null)
        {
          author = author.color(TextColor.color(255, 170, 0));
        }
        int pageCount = bookMeta.getPageCount();
        ItemLoreUtil.setItemRarityValue(lore, pageCount);
        BookMeta.Generation g = bookMeta.getGeneration();
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&7저자 : %s", author != null ? author : ComponentUtil.translate("알 수 없음")));
        lore.add(ComponentUtil.translate("&7출판 : %s", ComponentUtil.translate("&6book.generation." + (g != null ? g.ordinal() : "0"))));
        lore.add(ComponentUtil.translate("&7쪽수 : %s", ComponentUtil.translate("&6%s장", pageCount)));
      }
      case WHITE_BANNER, BLACK_BANNER, BLUE_BANNER, BROWN_BANNER, CYAN_BANNER, GRAY_BANNER, GREEN_BANNER, LIGHT_BLUE_BANNER, LIGHT_GRAY_BANNER
              , LIME_BANNER, MAGENTA_BANNER, ORANGE_BANNER, PURPLE_BANNER, PINK_BANNER, RED_BANNER, YELLOW_BANNER -> {
        BannerMeta bannerMeta = (BannerMeta) itemMeta;
        if (bannerMeta.numberOfPatterns() != 0)
        {
          bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
          List<Pattern> patterns = bannerMeta.getPatterns();
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("#D0DCDE;[현수막 무늬 목록]"));
          for (Pattern pattern : patterns)
          {
            ItemLoreUtil.setItemRarityValue(lore, +10);
            switch (pattern.getPattern())
            {
              case CREEPER:
              case SKULL:
                ItemLoreUtil.setItemRarityValue(lore, ItemCategory.Rarity.UNIQUE.getRarityValue());
                break;
              case FLOWER:
                ItemLoreUtil.setItemRarityValue(lore, ItemCategory.Rarity.NORMAL.getRarityValue());
                break;
              case GLOBE:
                ItemLoreUtil.setItemRarityValue(lore, ItemCategory.Rarity.ELITE.getRarityValue());
                break;
              case MOJANG:
                ItemLoreUtil.setItemRarityValue(lore, ItemCategory.Rarity.UNIQUE.getRarityValue() + 300);
                break;
              case PIGLIN:
                ItemLoreUtil.setItemRarityValue(lore, ItemCategory.Rarity.UNIQUE.getRarityValue() + 100);
                break;
              default:
                break;
            }
            String patternTranslate = ColorCode.getColorCode(pattern.getColor())
                    + "block.minecraft.banner." + pattern.getPattern().toString().toLowerCase()
                    .replace("_middle", "").replace("stripe_small", "small_stripes")
                    .replace("_mirror", "") + "." +
                    pattern.getColor().toString().toLowerCase();
            lore.add(ComponentUtil.translate(patternTranslate.replace("stripe.", "stripe_middle.")));
          }
          if (bannerMeta.numberOfPatterns() > 6)
          {
            ItemLoreUtil.setItemRarityValue(lore, 20L * bannerMeta.numberOfPatterns());
          }
        }
        else
        {
          itemMeta.removeItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        }
      }
      case SHIELD -> {
        Banner bannerMeta = (Banner) ((BlockStateMeta) itemMeta).getBlockState();
        if (bannerMeta.numberOfPatterns() != 0)
        {
          itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
          List<Pattern> patterns = bannerMeta.getPatterns();
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("#D0DCDE;[방패 무늬 목록]"));
          for (Pattern pattern : patterns)
          {
            ItemLoreUtil.setItemRarityValue(lore, +10);
            switch (pattern.getPattern())
            {
              case CREEPER:
              case SKULL:
                ItemLoreUtil.setItemRarityValue(lore, ItemCategory.Rarity.UNIQUE.getRarityValue());
                break;
              case FLOWER:
                ItemLoreUtil.setItemRarityValue(lore, ItemCategory.Rarity.NORMAL.getRarityValue());
                break;
              case GLOBE:
                ItemLoreUtil.setItemRarityValue(lore, ItemCategory.Rarity.ELITE.getRarityValue());
                break;
              case MOJANG:
                ItemLoreUtil.setItemRarityValue(lore, ItemCategory.Rarity.UNIQUE.getRarityValue() + 300);
                break;
              case PIGLIN:
                ItemLoreUtil.setItemRarityValue(lore, ItemCategory.Rarity.UNIQUE.getRarityValue() + 100);
                break;
              default:
                break;
            }
            String patternTranslate = ColorCode.getColorCode(pattern.getColor())
                    + "block.minecraft.banner." + pattern.getPattern().toString().toLowerCase()
                    .replace("_middle", "").replace("stripe_small", "small_stripes")
                    .replace("_mirror", "") + "." +
                    pattern.getColor().toString().toLowerCase();
            lore.add(ComponentUtil.translate(patternTranslate.replace("stripe.", "stripe_middle.")));
          }
          if (bannerMeta.numberOfPatterns() > 6)
          {
            ItemLoreUtil.setItemRarityValue(lore, 20L * bannerMeta.numberOfPatterns());
          }
        }
        else
        {
          itemMeta.removeItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        }
      }
      case TROPICAL_FISH_BUCKET -> {
        TropicalFishBucketMeta bucketMeta = (TropicalFishBucketMeta) itemMeta;
        if (bucketMeta.hasVariant())
        {
          bucketMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
          lore.add(Component.empty());
          String key = TropicalFishLore.getTropicalFishKey(bucketMeta.getBodyColor(), bucketMeta.getPatternColor(), bucketMeta.getPattern());

          Component arg;
          DyeColor bodyColor = bucketMeta.getBodyColor();
          DyeColor patternColor = bucketMeta.getPatternColor();
          if (key.contains("predefined"))
          {
            arg = ComponentUtil.translate("&6" + key);
          }
          else
          {
            String bodyColorKey = "color.minecraft." + bodyColor.toString().toLowerCase();
            String patternColorKey = "color.minecraft." + patternColor.toString().toLowerCase();
            arg = ComponentUtil.translate("&6" + bodyColorKey);
            if (bodyColor != patternColor)
            {
              arg = arg.append(ComponentUtil.create(", ").append(ComponentUtil.translate("&6" + patternColorKey)));
            }
            arg = arg.append(ComponentUtil.create(" ")).append(ComponentUtil.translate("&6" + key));
          }
          lore.add(ComponentUtil.translate("&7물고기 종 : %s", arg));
        }
      }
      case SUSPICIOUS_STEW -> {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate(Constant.ITEM_LORE_STATUS_EFFECT));
        SuspiciousStewMeta stewMeta = (SuspiciousStewMeta) itemMeta;
        List<Component> customPotionEffects = ItemLorePotionDescription.getCustomEffectList(item);
        if (!stewMeta.hasCustomEffects() && customPotionEffects.isEmpty())
        {
          lore.add(ItemLorePotionDescription.NONE);
        }
        else
        {
          for (PotionEffect effect : stewMeta.getCustomEffects())
          {
            lore.add(ItemLorePotionDescription.getDescription(ItemLorePotionDescription.getComponent(effect.getType()), effect.getDuration(), effect.getAmplifier() + 1));
            ItemLoreUtil.setItemRarityValue(lore, 10L * ((effect.getDuration() / 200) + 1) * (effect.getAmplifier() + 1));
          }
          lore.addAll(customPotionEffects);
        }
      }
      case FILLED_MAP -> {
        MapMeta mapMeta = (MapMeta) itemMeta;
        mapMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        if (mapMeta.hasMapView() && mapMeta.getMapView() != null)
        {
          MapView mapView = mapMeta.getMapView();
          int centerX = mapView.getCenterX(), centerZ = mapView.getCenterZ();
          int id = mapView.getId();
          ItemLoreUtil.setItemRarityValue(lore, id * 2L);
          MapView.Scale scale = mapView.getScale();
          String scaleString = switch (scale)
                  {
                    case CLOSE -> "1:2";
                    case CLOSEST -> "1:1";
                    case FAR -> "1:8";
                    case FARTHEST -> "1:16";
                    case NORMAL -> "1:4";
                  };
          World world = mapView.getWorld();
          if (world != null)
          {
            String worldName = Method.getWorldDisplayName(world);
            lore.add(Component.empty());
            lore.add(ComponentUtil.translate("&7지도 ID : %s", "&6" + id));
            lore.add(ComponentUtil.translate("&7축척 : %s", "&6" + scaleString));
            if (Cucumbery.config.getBoolean("use-center-coord-of-map-lore-feature"))
            {
              lore.add(ComponentUtil.translate("&7월드 : %s", "&6" + worldName));
              lore.add(ComponentUtil.translate("&7지도 중심 좌표 : %s", "x=&6" + centerX + "&7, z=&6" + centerZ));
            }
          }
        }
      }
      case FIREWORK_STAR -> {
        FireworkEffectMeta fireworkEffectMeta = (FireworkEffectMeta) itemMeta;
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        if (fireworkEffectMeta.hasEffect())
        {
          lore.add(Component.empty());
          FireworkEffect fireworkEffect = fireworkEffectMeta.getEffect();
          ItemLoreUtil.addFireworkEffectLore(lore, fireworkEffect);
        }
      }
      case FIREWORK_ROCKET -> {
        FireworkMeta fireworkMeta = (FireworkMeta) itemMeta;
        fireworkMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        int power = fireworkMeta.getPower();
        if (power >= 0 && power <= 127)
        {
          ItemLoreUtil.setItemRarityValue(lore, 20 * power);
        }

        if (!hideFireworkEffects)
        {
          lore.add(Component.empty());
          if (power >= 0 && power <= 127)
          {
            lore.add(ComponentUtil.translate("&7체공 시간 : %s", ComponentUtil.translate("&6약 ").append(ComponentUtil.translate("&6%s초", "" + (0.5d * (power + 1d) + 0.3)))));
          }
          else if (power == 255)
          {
            lore.add(ComponentUtil.translate("&7체공 시간 : %s", ComponentUtil.translate("&6약 ").append(ComponentUtil.translate("&6%s초", "0.3"))));
          }
          else
          {
            lore.add(ComponentUtil.translate("&7체공 시간 : %s", ComponentUtil.translate("&6즉시 폭발")));
          }

          if (fireworkMeta.hasEffects())
          {
            lore.add(Component.empty());
            lore.add(ComponentUtil.translate("&e[폭죽 효과 목록]"));

            int effectSize = fireworkMeta.getEffectsSize();
            for (int i = 0; i < fireworkMeta.getEffectsSize(); i++)
            {
              if (effectSize > 5)
              {
                int skipped = effectSize - 5;
                if (i > 2 && i < effectSize - 2)
                {
                  if (i == 3)
                  {
                    lore.add(ComponentUtil.translate("&7&ocontainer.shulkerBox.more", Component.text(skipped)));
                  }
                  continue;
                }
              }
              Component add = ComponentUtil.translate("&3&m          %s          ", ComponentUtil.translate("&m&q[%s]", ComponentUtil.translate("&9%s번째 효과", i + 1)));
              lore.add(add);
              FireworkEffect fireworkEffect = fireworkMeta.getEffects().get(i);
              ItemLoreUtil.addFireworkEffectLore(lore, fireworkEffect);
            }
          }
        }
      }
      case CROSSBOW -> {
        CrossbowMeta crossbowMeta = (CrossbowMeta) itemMeta;
        if (crossbowMeta.hasChargedProjectiles())
        {
          itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
          lore.add(Component.empty());
          ItemStack chargedProjectile = crossbowMeta.getChargedProjectiles().get(0).clone();
          if (ItemStackUtil.itemExists(chargedProjectile))
          {
            lore.addAll(ItemStackUtil.getItemInfoAsComponents(chargedProjectile, ComponentUtil.translate("&e[발사체]"), true));
          }
          else
          {
            lore.remove(lore.size() - 1);
            itemMeta.removeItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
          }
        }
        else
        {
          itemMeta.removeItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        }
      }
      case BOW -> {
        if (params instanceof EntityShootBowEvent event)
        {
          ItemStack consumable = event.getConsumable();
          if (ItemStackUtil.itemExists(consumable))
          {
            consumable = consumable.clone();
            lore.add(Component.empty());
            lore.addAll(ItemStackUtil.getItemInfoAsComponents(consumable, ComponentUtil.translate("&e[발사체]"), true));
          }
        }
      }
      case LEATHER_BOOTS, LEATHER_LEGGINGS, LEATHER_CHESTPLATE, LEATHER_HELMET, LEATHER_HORSE_ARMOR -> {
        itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
        Color color = leatherArmorMeta.getColor();
        int red = color.getRed(), green = color.getGreen(), blue = color.getBlue();
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("#a88932;[%s의 색상]", ItemNameUtil.itemName(item)));
        lore.add(ComponentUtil.create2("rgb" + red + "," + green + "," + blue + ";#" +
                Integer.toHexString(0x100 | red).substring(1) + Integer.toHexString(0x100 | green).substring(1) + Integer.toHexString(0x100 | blue).substring(1)));
      }
      case COMPASS -> {
        CompassMeta compassMeta = (CompassMeta) itemMeta;
        if (compassMeta.hasLodestone())
        {
          Location lodestoneLocation = compassMeta.getLodestone();
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("#BEBABA;[%s의 좌표]", ItemNameUtil.itemName(Material.LODESTONE)));
          lore.add(LocationComponent.locationComponent(lodestoneLocation));
        }
        else if (compassMeta.isLodestoneTracked())
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("#BEBABA;[%s의 좌표]", ItemNameUtil.itemName(Material.LODESTONE)));
          lore.add(ComponentUtil.translate("#BD443C;자석석이 " + (Math.random() * 100d > 10d ? "" : "&m미국감&q") + "분실됨"));
        }
      }
      case NOTE_BLOCK -> {
        if (!hideBlockData)
        {
          NBTCompound blockStateTag = nbtItem.getCompound(CucumberyTag.MINECRAFT_BLOCK_STATE_TAG_KEY);
          if (blockStateTag == null)
          {
            break;
          }
          String instrument = blockStateTag.getString("instrument");
          String note = blockStateTag.getString("note");
          lore.add(Component.empty());
          if (instrument != null && !instrument.equals(""))
          {
            lore.add(ComponentUtil.translate("&e악기 : %s", instrument));
          }
          if (note != null)
          {
            try
            {
              String noteString = ItemStackUtil.getNoteString(Integer.parseInt(note));
              lore.add(ComponentUtil.translate("&e음높이 : %s", noteString));
            }
            catch (Exception ignored)
            {

            }
          }
        }
      }
      case CREEPER_BANNER_PATTERN, FLOWER_BANNER_PATTERN, GLOBE_BANNER_PATTERN, MOJANG_BANNER_PATTERN,
              PIGLIN_BANNER_PATTERN, SKULL_BANNER_PATTERN -> {
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&e무늬 : %s", ComponentUtil.translate("&7" + type.translationKey() + ".desc")));
      }
      case MUSIC_DISC_11, MUSIC_DISC_13, MUSIC_DISC_BLOCKS, MUSIC_DISC_CAT, MUSIC_DISC_CHIRP,
              MUSIC_DISC_FAR, MUSIC_DISC_MALL, MUSIC_DISC_MELLOHI, MUSIC_DISC_PIGSTEP, MUSIC_DISC_STAL,
              MUSIC_DISC_STRAD, MUSIC_DISC_WAIT, MUSIC_DISC_WARD -> {
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&e작곡가 : %s", "&7" + (type == Material.MUSIC_DISC_PIGSTEP ? "Lena Raine" : "C418")));
        lore.add(ComponentUtil.translate("&e곡 : %s", "&7" + (type == Material.MUSIC_DISC_PIGSTEP ? "PigStep" : type.toString().toLowerCase().split("music_disc_")[1])));
      }
      case DEBUG_STICK -> {
        if (NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.DEBUG_STICK.toString()))
        {
          break;
        }
        NBTCompound debugProperty = nbtItem.getCompound("DebugProperty");
        if (debugProperty != null && !debugProperty.getKeys().isEmpty())
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&9[디버그 속성]"));
          for (String key : debugProperty.getKeys())
          {
            String value = debugProperty.getString(key);
            Component itemType;
            try
            {
              itemType = ItemNameUtil.itemName(Material.valueOf(key.replace("minecraft:", "").replace(".", "_").toUpperCase()));
            }
            catch (Exception ignored)
            {
              itemType = ItemNameUtil.itemName(Material.STONE);
            }
            lore.add(ComponentUtil.translate("&7%s : %s", itemType, "&e" + value));
          }
        }
      }
    }

    if (itemMeta instanceof BlockStateMeta blockStateMeta)
    {
      itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
      if (!hideBlockState && blockStateMeta.hasBlockState())
      {
        NBTCompound blockEntityTag = nbtItem.getCompound("BlockEntityTag");
        BlockState blockState = blockStateMeta.getBlockState();
        Component customName = blockState instanceof Nameable nameable ? nameable.customName() : null;
        if (customName == null)
        {
          customName = ItemNameUtil.itemName(item);
        }

        String colorPrefix = "&2";
        switch (type)
        {
          case SHULKER_BOX -> colorPrefix = "#B274E2;";
          case BLACK_SHULKER_BOX, GRAY_SHULKER_BOX, LIGHT_GRAY_SHULKER_BOX -> colorPrefix = "#808080;";
          case BLUE_SHULKER_BOX -> colorPrefix = "#415DFF;";
          case BROWN_SHULKER_BOX -> colorPrefix = "#995700;";
          case CYAN_SHULKER_BOX -> colorPrefix = "&3";
          case GREEN_SHULKER_BOX -> colorPrefix = "#00A800;";
          case LIGHT_BLUE_SHULKER_BOX -> colorPrefix = "#46CDEC;";
          case LIME_SHULKER_BOX -> colorPrefix = "#73DC00;";
          case MAGENTA_SHULKER_BOX -> colorPrefix = "#D043C4;";
          case ORANGE_SHULKER_BOX -> colorPrefix = "#FF7414;";
          case PINK_SHULKER_BOX -> colorPrefix = "#FF8FA8;";
          case PURPLE_SHULKER_BOX -> colorPrefix = "#A400D6;";
          case RED_SHULKER_BOX -> colorPrefix = "#E9483A;";
          case WHITE_SHULKER_BOX -> colorPrefix = "#F5F5F5;";
          case YELLOW_SHULKER_BOX -> colorPrefix = "#FFD400;";
          case CHEST, TRAPPED_CHEST -> colorPrefix = "#CC8500;";
          case BARREL -> colorPrefix = "#B5703B;";
          case CAMPFIRE -> colorPrefix = "#FFC700;";
          case SOUL_CAMPFIRE -> colorPrefix = "#81E9F4;";
          case BEE_NEST -> colorPrefix = "#F8AD0D;";
          case BEEHIVE -> colorPrefix = "#CAA863;";
          case FURNACE, BLAST_FURNACE, SMOKER, DROPPER, DISPENSER, HOPPER -> colorPrefix = "#978E87;";
        }
        Component customNameLore = ComponentUtil.translate(colorPrefix + "[%s의 내용물]", customName);

        if (blockState instanceof Sign sign)
        {
          boolean isGlowingText = sign.isGlowingText();
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&9" + (isGlowingText ? "&l" : "") + "[%s의 내용]", customName));

          DyeColor dyeColor = sign.getColor();
          Color color = dyeColor != null ? dyeColor.getColor() : null;
          TextColor textColor = color != null ? TextColor.color(color.asRGB()) : NamedTextColor.WHITE;
          if (dyeColor == DyeColor.BLACK)
          {
            textColor = NamedTextColor.WHITE;
          }
          List<Component> lines = sign.lines();
          boolean hasAtleastOne = false;
          for (int i = 0; i < lines.size(); i++)
          {
            Component line = lines.get(i);
            if (line.color() == null)
            {
              line = line.color(textColor);
            }
            if (!(line.equals(Component.empty())))
            {
              hasAtleastOne = true;
              lore.add(ComponentUtil.translate("&7%s번째 텍스트 : %s", (i + 1), line));
            }
          }

          if (!hasAtleastOne)
          {
            lore.remove(lore.size() - 1);
            lore.remove(lore.size() - 1);
          }
        }
        if (blockState instanceof Lockable lockable && lockable.isLocked())
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&7잠금 태그 : %s", ComponentUtil.create2(lockable.getLock(), false)));
        }
        if (blockState instanceof BrewingStand brewingStand)
        {
          int brewingTime = brewingStand.getBrewingTime();
          int fuelLevel = brewingStand.getFuelLevel();
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&6양조 완료까지 남은 시간 : %s", ComponentUtil.translate("&e%s초", Constant.Sosu2.format(brewingTime / 20D))));
          lore.add(ComponentUtil.translate("&6남은 땔감 : %s", "&e" + fuelLevel));
        }
        if (blockState instanceof Campfire campfire)
        {
          lore.add(Component.empty());
          lore.add(customNameLore);
          boolean hasAtLeastOne = false;
          for (int i = 0; i < campfire.getSize(); i++)
          {
            ItemStack itemStack = campfire.getItem(i);
            if (ItemStackUtil.itemExists(itemStack))
            {
              hasAtLeastOne = true;
              int cookTime = campfire.getCookTime(i);
              int cookTimeTotal = campfire.getCookTimeTotal(i);
              Component itemStackComponent = ItemStackComponent.itemStackComponent(itemStack);
              lore.add(ComponentUtil.translate("&7%s번째 아이템 : %s, 조리 진행도 : %s / %s (%s)",
                      i + 1,
                      itemStackComponent, ComponentUtil.translate("&e%s초", Constant.Sosu2.format(cookTime / 20d)),
                      ComponentUtil.translate("&6%s초", Constant.Sosu2.format(cookTimeTotal / 20d)),
                      ComponentUtil.translate("&a%s%%", Constant.Sosu2.format(100d * cookTime / cookTimeTotal))));
            }
          }

          if (!hasAtLeastOne)
          {
            lore.remove(lore.size() - 1);
            lore.remove(lore.size() - 1);
          }
        }
        if (blockState instanceof Furnace furnace)
        {
          FurnaceInventory furnaceInventory = furnace.getInventory();
          ItemStack ingredient = furnaceInventory.getSmelting();
          String smeltType = "제련";
          if (ItemStackUtil.itemExists(ingredient) && ItemStackUtil.isEdible(ingredient.getType()))
          {
            smeltType = "조리";
          }
          int cookTimeTotal = furnace.getCookTimeTotal();
          if (cookTimeTotal != 0)
          {
            short burnTime = furnace.getBurnTime();
            short cookTime = furnace.getCookTime();
            double speed = furnace.getCookSpeedMultiplier();
            lore.add(Component.empty());
            lore.add(ComponentUtil.translate("&7땔감 지속 시간 : %s초", Constant.Sosu2.format(burnTime / 20d)));
            if (speed != 1d)
            {
              lore.add(ComponentUtil.translate("&7" + smeltType + " 진행 속도 : %s배", Constant.Sosu2.format(speed)));
            }
            lore.add(ComponentUtil.translate("&7" + smeltType + " 진행도 : %s / %s (%s)",
                    ComponentUtil.translate("%s초", Constant.Sosu2.format(cookTime / 20d)),
                    ComponentUtil.translate("%s초", Constant.Sosu2.format(cookTimeTotal / 20d)),
                    ComponentUtil.translate("%s%%", Constant.Sosu2.format(100d * cookTime / cookTimeTotal))));
          }
          if (!Method.inventoryEmpty(furnaceInventory))
          {
            lore.add(Component.empty());
            lore.add(customNameLore);
            if (ItemStackUtil.itemExists(ingredient))
            {
              Component itemStackComponent = ItemStackComponent.itemStackComponent(ingredient);
              lore.add(ComponentUtil.translate("&7" + smeltType + " 중인 아이템 : %s", itemStackComponent));
            }
            ItemStack fuel = furnaceInventory.getFuel();
            if (ItemStackUtil.itemExists(fuel))
            {
              Component itemStackComponent = ItemStackComponent.itemStackComponent(fuel);
              lore.add(ComponentUtil.translate("&7땔감 아이템 : %s", itemStackComponent));
            }
            ItemStack result = furnaceInventory.getResult();
            if (ItemStackUtil.itemExists(result))
            {
              Component itemStackComponent = ItemStackComponent.itemStackComponent(result);
              lore.add(ComponentUtil.translate("&7결과물 아이템 : %s", itemStackComponent));
            }
          }
        }
        try
        {
          if (blockState instanceof InventoryHolder inventoryHolder && !(blockState instanceof Furnace))
          {
            Inventory inventory = inventoryHolder.getInventory();
            if (!Method.inventoryEmpty(inventory))
            {
              lore.add(Component.empty());
              List<ItemStack> itemStackList = new ArrayList<>();
              for (int i = 0; i < inventory.getSize(); i++)
              {
                ItemStack itemStack = inventory.getItem(i);
                if (ItemStackUtil.itemExists(itemStack))
                {
                  itemStackList.add(itemStack);
                }
              }
              if (itemStackList.size() == 1)
              {
                lore.addAll(ItemStackUtil.getItemInfoAsComponents(itemStackList.get(0), customNameLore, true));
              }
              else
              {
                lore.add(customNameLore);
                for (int i = 0; i < itemStackList.size(); i++)
                {
                  if (i == 27)
                  {
                    lore.add(ComponentUtil.translate("&7&ocontainer.shulkerBox.more", Component.text(itemStackList.size() - i)));
                    break;
                  }
                  ItemStack itemStack = itemStackList.get(i);
                  lore.add(ItemStackComponent.itemStackComponent(ItemLore.removeItemLore(itemStack)));
                }
              }
            }
          }
        }
        catch (Exception e)
        {
          lore.add(Component.empty());
          lore.add(ComponentUtil.translate("&7일해라 페이퍼 오류 안고치니"));
        }
        if (blockState instanceof Beehive beehive)
        {
          customNameLore = ComponentUtil.translate(colorPrefix + "[%s의 벌들]", customName);
          int beeCount = beehive.getEntityCount(), maxBeeCount = beehive.getMaxEntities();
          lore.add(Component.empty());
          lore.add(customNameLore);
          lore.add(ComponentUtil.translate("#b07c15;벌 %s", beeCount == 0 ? "없음" : "#e6ac6d;" + beeCount + "마리"));
          //Location flowerPos = beehive.getFlower(); must be placed
          NBTCompound flowerPos = blockEntityTag.getCompound("FlowerPos");
          if (flowerPos != null)
          {
            lore.add(ComponentUtil.translate("#b07c15;꽃 좌표 : %s",
                    ComponentUtil.translate("#b07c15;%s, %s, %s", "#e6ac6d;" + flowerPos.getInteger("X"), "#e6ac6d;" + flowerPos.getInteger("Y"), "#e6ac6d;" + flowerPos.getInteger("Z"))));
          }
        }
        if (blockState instanceof Lootable lootable)
        {
          LootTable lootTable = lootable.getLootTable();
          if (lootTable != null)
          {
            Long seed = blockEntityTag.getLong("LootTableSeed");
            lore.add(Component.empty());
            lore.add(ComponentUtil.translate("&7루트테이블 : %s", lootTable.getKey()));
            if (seed != null)
            {
              lore.add(ComponentUtil.translate("&7시드 : %s", seed));
            }
          }
        }
        if (blockState instanceof Jukebox jukebox)
        {
          ItemStack record = jukebox.getRecord();
          if (ItemStackUtil.itemExists(record))
          {
            lore.addAll(ItemStackUtil.getItemInfoAsComponents(record, ComponentUtil.translate("&e[음반]"), true));
          }
        }
      }
    }

    NBTCompound blockStateTag = nbtItem.getCompound(CucumberyTag.MINECRAFT_BLOCK_STATE_TAG_KEY);
    if (!hideBlockData && type != Material.NOTE_BLOCK && blockStateTag != null && !blockStateTag.getKeys().isEmpty())
    {
      lore.add(Component.empty());
      lore.add(ComponentUtil.translate("&b[블록 데이터 태그]"));
      for (String key : blockStateTag.getKeys())
      {
        String value = blockStateTag.getString(key);
        if (value == null || value.equals(""))
        {
          Integer integer = blockStateTag.getInteger(key);
          if (integer != null)
          {
            value = integer.toString();
          }
          else
          {
            Boolean bool = blockStateTag.getBoolean(key);
            if (bool != null)
            {
              value = bool.toString();
            }
          }
        }
        lore.add(ComponentUtil.translate("&7%s : %s", "&b" + key, "&e" + value));
      }
    }

    if (itemMeta.hasDestroyableKeys())
    {
      itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
      Set<Namespaced> destroyableKeys = itemMeta.getDestroyableKeys();
      lore.add(Component.empty());
      lore.add(ComponentUtil.translate("&6[%s]", ComponentUtil.translate("item.canBreak")));
      for (Namespaced key : destroyableKeys)
      {
        if (key.getNamespace().equals("minecraft"))
        {
          try
          {
            lore.add(ComponentUtil.create("&7", Material.valueOf(key.getKey().toUpperCase())));
          }
          catch (Exception e)
          {
            lore.add(ComponentUtil.translate("&7" + key.toString()));
          }
        }
      }
    }
    else
    {
      itemMeta.removeItemFlags(ItemFlag.HIDE_DESTROYS);
    }
    if (itemMeta.hasPlaceableKeys())
    {
      itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
      Set<Namespaced> destroyableKeys = itemMeta.getPlaceableKeys();
      lore.add(Component.empty());
      lore.add(ComponentUtil.translate("&6[%s]", ComponentUtil.translate("item.canPlace")));
      for (Namespaced key : destroyableKeys)
      {
        if (key.getNamespace().equals("minecraft"))
        {
          try
          {
            lore.add(ComponentUtil.create("&7", Material.valueOf(key.getKey().toUpperCase())));
          }
          catch (Exception e)
          {
            lore.add(ComponentUtil.translate("&7" + key.toString()));
          }
        }
      }
    }
    else
    {
      itemMeta.removeItemFlags(ItemFlag.HIDE_PLACED_ON);
    }

    // easterEggs
    if (type == Material.FISHING_ROD)
    {
      if (itemMeta.getEnchantLevel(Enchantment.DURABILITY) == 3
              && itemMeta.getEnchantLevel(Enchantment.MENDING) == 1
              && itemMeta.getEnchantLevel(Enchantment.LURE) == 3
              && itemMeta.getEnchantLevel(Enchantment.LUCK) == 3
      )
      {
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&b[이스터 에그]"));
        lore.add(ComponentUtil.translate("&7%s가 가장 좋아하는 만렙 낚싯대! 낚시하시져", "&a오이"));
      }
    }

    // 설치 가능
    if (ItemStackUtil.isPlacable(type) && !NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.PLACABLE.toString())
            && !NBTAPI.isRestrictedFinal(item, RestrictionType.NO_PLACE))
    {
      lore.add(Component.empty());
      lore.add(ComponentUtil.translate(Constant.ITEM_LORE_PLACABLE));
    }

    if (RecipeChecker.hasCraftingRecipe(type) && !NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.CRAFTABLE.toString()))
    {
      boolean noCraft = NBTAPI.isRestrictedFinal(item, RestrictionType.NO_CRAFT);
      boolean noCraftInventory = NBTAPI.isRestrictedFinal(item, RestrictionType.NO_CRAFT_IN_INVENTORY);
      boolean noCraftCraftingTable = NBTAPI.isRestrictedFinal(item, RestrictionType.NO_CRAFT_IN_CRAFTING_TABLE);
      if (!noCraft && !(noCraftInventory && noCraftCraftingTable))
      {
        lore.add(Component.empty());
        if (!noCraftInventory && !noCraftCraftingTable)
        {
          lore.add(ComponentUtil.translate(Constant.ITEM_LORE_MATERIAL_CRAFTABLE));
        }
        else if (!noCraftCraftingTable)
        {
          lore.add(ComponentUtil.translate(Constant.ITEM_LORE_MATERIAL_CRAFTABLE_ONLY_CRAFTING_TABLE));
        }
        else
        {
          lore.add(ComponentUtil.translate(Constant.ITEM_LORE_MATERIAL_CRAFTABLE_ONLY_INVENTORY));
        }
      }
    }

    if (ItemStackUtil.isBrewable(type) && !NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.BREWABLE.toString())
            && (!NBTAPI.isRestricted(item, RestrictionType.NO_BREW) || NBTAPI.getRestrictionOverridePermission(item, RestrictionType.NO_BREW) != null))
    {
      lore.add(Component.empty());
      lore.add(ComponentUtil.translate(Constant.ITEM_LORE_MATERIAL_BREWABLE));
    }

    boolean noFurnace = NBTAPI.isRestrictedFinal(item, RestrictionType.NO_FURNACE);
    boolean noSmoker = NBTAPI.isRestrictedFinal(item, RestrictionType.NO_SMOKER);
    boolean noBlastFurnace = NBTAPI.isRestrictedFinal(item, RestrictionType.NO_BLAST_FURNACE);
    boolean noCampfire = NBTAPI.isRestrictedFinal(item, RestrictionType.NO_CAMPFIRE);

    if (RecipeChecker.hasSmeltingRecipe(type) &&
            !NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.SMELTABLE.toString()) &&
            !NBTAPI.isRestrictedFinal(item, RestrictionType.NO_SMELT) &&
            !(noBlastFurnace && noSmoker && noFurnace && noCampfire))
    {
      lore.add(Component.empty());
      if (noBlastFurnace || noCampfire || noFurnace || noSmoker)
      {
        String available = "";

        if (!noFurnace)
        {
          available += "화로, ";
        }
        if (!noBlastFurnace)
        {
          available += "용광로, ";
        }
        if (!noSmoker)
        {
          available += "훈연기, ";
        }
        if (!noCampfire)
        {
          available += "모닥불, ";
        }
        available = available.substring(0, available.length() - 2);
        if (type.isEdible())
        {
          lore.add(ComponentUtil.translate("#F07447;[" + available + "에서만 조리 가능]"));
        }
        else
        {
          lore.add(ComponentUtil.translate("rgb255,79,48;[" + available + "에서만 제련 가능]"));
        }
      }
      else
      {
        if (type.isEdible())
        {
          lore.add(ComponentUtil.translate(Constant.ITEM_LORE_MATERIAL_SMELTABLE_COOK));
        }
        else
        {
          lore.add(ComponentUtil.translate(Constant.ITEM_LORE_MATERIAL_SMELTABLE));
        }
      }
    }

    if ((ItemStackUtil.isEdible(type) || type == Material.CAKE) && !NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.CONSUMABLE.toString())
            && !NBTAPI.isRestrictedFinal(item, RestrictionType.NO_CONSUME))
    {
      String nourishment = ItemStackUtil.getNourishment(type);
      lore.add(Component.empty());
      lore.add(ComponentUtil.translate(Constant.ITEM_LORE_CONSUMABLE));
      if (foodTag != null)
      {
        Integer foodLevel = foodTag.getInteger(CucumberyTag.FOOD_LEVEL_KEY);
        if (foodLevel == null)
        {
          foodLevel = 0;
        }
        Double saturation = foodTag.getDouble(CucumberyTag.SATURATION_KEY);
        if (saturation == null)
        {
          saturation = 0d;
        }
        if (foodTag.hasKey(CucumberyTag.NOURISHMENT_KEY))
        {
          nourishment = foodTag.getString(CucumberyTag.NOURISHMENT_KEY);
          lore.add(ComponentUtil.translate("rgb235,163,0;든든함 : %s", ComponentUtil.translate(nourishment)));
        }
        else if (foodLevel != 0 || saturation != 0)
        {
          nourishment = ItemStackUtil.getNourishment(foodLevel, saturation);
          lore.add(ComponentUtil.translate("rgb235,163,0;든든함 : %s", ComponentUtil.translate(nourishment)));
        }
        if (!foodTag.hasKey(CucumberyTag.FOOD_LEVEL_KEY) && ItemStackUtil.getFoodLevel(type) != 0)
        {
          lore.add(ComponentUtil.translate("rgb255,183,0;음식 포인트 : %s", "+" + ItemStackUtil.getFoodLevel(type)));
        }
        else if (foodLevel != null && foodLevel != 0)
        {
          lore.add(ComponentUtil.translate("rgb255,183,0;음식 포인트 : %s", (foodLevel > 0 ? "+" : "") + foodLevel));
        }
        if (!foodTag.hasKey(CucumberyTag.SATURATION_KEY) && ItemStackUtil.getSaturation(type) != 0d)
        {
          lore.add(ComponentUtil.translate("rgb255,183,0;포화도 : %s", "+" + Constant.Sosu2.format(ItemStackUtil.getSaturation(type))));
        }
        else if (saturation != null && saturation != 0d)
        {
          lore.add(ComponentUtil.translate("rgb255,183,0;포화도 : %s", (saturation > 0d ? "+" : "") + Constant.Sosu2.format(saturation)));
        }
      }
      else if (nourishment != null && !nourishment.equals("기본"))
      {
        lore.add(ComponentUtil.translate("rgb235,163,0;든든함 : %s", ComponentUtil.translate(nourishment)));
        lore.add(ComponentUtil.translate("rgb255,183,0;음식 포인트 : %s", "+" + ItemStackUtil.getFoodLevel(type)));
        lore.add(ComponentUtil.translate("rgb255,183,0;포화도 : %s", "+" + Constant.Sosu2.format(ItemStackUtil.getSaturation(type))));
      }
    }

    if (type.isFuel() && !NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.FUEL.toString())
            && !NBTAPI.isRestrictedFinal(item, RestrictionType.NO_SMELT))
    {
      lore.add(Component.empty());
      lore.add(ComponentUtil.translate(Constant.ITEM_LORE_FUEL));
      lore.add(ComponentUtil.translate("rgb232,99,79;지속 시간 : %s"
              , ComponentUtil.translate("%s초", Component.text(Constant.Sosu2.format(ItemStackUtil.getFuelTimeInSecond(type))))));
    }

    if (Constant.COMPOSTABLE_ITEMS.contains(type) && !NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.COMPOSTABLE.toString())
            && !NBTAPI.isRestrictedFinal(item, RestrictionType.NO_COMPOSTER))
    {
      lore.add(Component.empty());
      lore.add(ComponentUtil.translate(Constant.ITEM_LORE_MATERIAL_COMPOSTABLE, Component.text(Constant.Sosu2.format(ItemStackUtil.getCompostChance(type)) + "%")));
    }

    // CucumberyItemTag - CustomItemType
    String customItemType = NBTAPI.getString(itemTag, CucumberyTag.CUSTOM_ITEM_TYPE_KEY);
    if (customItemType != null)
    {
      lore.set(1, ComponentUtil.translate("&7아이템 종류 : [%s]", customItemType));
    }
    // CucumberyItemTag - CustomRarity
    if (customRarityTag != null && customRarityTag.hasKey(CucumberyTag.VALUE_KEY))
    {
      long rarity = customRarityTag.getLong(CucumberyTag.VALUE_KEY);
      ItemLoreUtil.setItemRarityValue(lore, rarity);
    }
    String customRarityFinal = NBTAPI.getString(customRarityTag, CucumberyTag.CUSTOM_RARITY_FINAL_KEY);
    if (customRarityFinal != null)
    {
      try
      {
        ItemLoreUtil.setItemRarityValue(lore, ItemCategory.Rarity.valueOf(customRarityFinal).getRarityValue(), false);
      }
      catch (Exception ignored)
      {

      }
    }
    if (!itemMeta.isUnbreakable() && itemMeta.hasItemFlag(ItemFlag.HIDE_UNBREAKABLE))
    {
      itemMeta.removeItemFlags(ItemFlag.HIDE_UNBREAKABLE);
    }
    // 아이템 이름 속성
    Component displayName = itemMeta.displayName();
    NBTCompound displayCompound = NBTAPI.getCompound(itemTag, CucumberyTag.ITEMSTACK_DISPLAY_KEY);
    if (!(displayName instanceof TranslatableComponent t && t.args().size() == 4 && t.args().get(3) instanceof TextComponent c && c.content().equals("Custom Display")) && displayCompound != null)
    {
      NBTCompoundList prefix = displayCompound.getCompoundList(CucumberyTag.ITEMSTACK_DISPLAY_PREFIX);
      NBTCompoundList suffix = displayCompound.getCompoundList(CucumberyTag.ITEMSTACK_DISPLAY_SUFFIX);
      String name = displayCompound.getString(CucumberyTag.ITEMSTACK_DISPLAY_NAME);
      NBTItem n = new NBTItem(item, true);
      NBTCompound m = n.getCompound(CucumberyTag.KEY_MAIN);
      NBTCompound d = m.getCompound(CucumberyTag.ITEMSTACK_DISPLAY_KEY);
      d.setString(CucumberyTag.ORIGINAL_NAME, displayName == null ? "" : ComponentUtil.serializeAsJson(displayName));
      itemMeta = n.getItem().getItemMeta();
      TranslatableComponent translatableComponent = Component.translatable("%s%s%s");
      List<Component> arguments = new ArrayList<>();
      Component prefixComponent = Component.empty();
      for (NBTCompound nbtCompound : prefix)
      {
        String text = nbtCompound.getString("text");
        if (text != null)
        {
          prefixComponent = ComponentUtil.create(false, prefixComponent, text);
        }
      }
      arguments.add(prefix.isEmpty() ? Component.empty() : prefixComponent);
      if (displayName == null)
      {
        if (name != null && !name.equals(""))
        {
          displayName = ComponentUtil.create(name);
        }
        else
        {
          displayName = ItemNameUtil.itemName(item);
        }
      }
      arguments.add(displayName);
      Component suffixComponent = Component.empty();
      for (NBTCompound nbtCompound : suffix)
      {
        String text = nbtCompound.getString("text");
        if (text != null)
        {
          suffixComponent = ComponentUtil.create(false, suffixComponent, text);
        }
      }
      arguments.add(suffix.isEmpty() ? Component.empty() : suffixComponent);
      arguments.add(Component.text("Custom Display"));
      translatableComponent = translatableComponent.args(arguments);
      itemMeta.displayName(translatableComponent);
    }
    // 추가 설명으로 인한 아이템의 등급 수치 변경
    long rarity2 = ItemLoreUtil.getItemRarityValue(lore);
    String rarityDisplay = ItemCategory.Rarity.getRarityFromValue(rarity2).getDisplay();
    Component itemRarityComponent = ComponentUtil.translate("&7아이템 등급 : %s", ComponentUtil.translate(rarityDisplay));
    lore.set(2, itemRarityComponent);
    itemMeta.lore(lore);
    item.setItemMeta(itemMeta);
    return item;
  }
}
