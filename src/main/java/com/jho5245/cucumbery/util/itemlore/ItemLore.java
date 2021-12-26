package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.customeffect.CustomEffectType;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.ItemCategory;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.ExtraTag;
import de.tr7zw.changeme.nbtapi.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemLore
{
  @NotNull
  public static ItemStack setItemLore(@NotNull ItemStack itemStack)
  {
    return ItemLore.setItemLore(itemStack, null);
  }

  @NotNull
  public static ItemStack setItemLore(@NotNull ItemStack itemStack, @Nullable Object params)
  {
    if (!ItemLoreUtil.isCucumberyTMIFood(itemStack))
    {
      return itemStack;
    }
    NBTCompound itemTagReadOnly = NBTAPI.getMainCompound(itemStack);
    NBTList<String> extraTagsReadOnly = NBTAPI.getStringList(itemTagReadOnly, CucumberyTag.EXTRA_TAGS_KEY);
    if (NBTAPI.arrayContainsValue(extraTagsReadOnly, ExtraTag.NO_TMI_LORE.toString()))
    {
      ItemLore.removeItemLore(itemStack);
      return itemStack;
    }
    Material type = itemStack.getType();
    NBTCompoundList potionsTag = NBTAPI.getCompoundList(itemTagReadOnly, CucumberyTag.CUSTOM_EFFECTS);
    if (Cucumbery.config.getBoolean("use-no-effect-potions-weirdly") && itemStack.getItemMeta() instanceof PotionMeta potionMeta && potionsTag == null)
    {
      PotionType potionType = potionMeta.getBasePotionData().getType();
      CustomEffectType customEffectType = switch (potionType)
              {
                case AWKWARD -> CustomEffectType.AWKWARD;
                case MUNDANE -> CustomEffectType.MUNDANE;
                case THICK -> CustomEffectType.THICK;
                case UNCRAFTABLE -> CustomEffectType.UNCRAFTABLE;
                default -> null;
              };
      if (customEffectType != null)
      {
        NBTItem nbtItem = new NBTItem(itemStack, true);
        NBTCompound itemTag = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
        if (itemTag == null)
        {
          itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
        }
        potionsTag = itemTag.getCompoundList(CucumberyTag.CUSTOM_EFFECTS);
        NBTContainer nbtContainer = new NBTContainer();
        if (type == Material.LINGERING_POTION)
        {
          nbtContainer.setInteger(CucumberyTag.CUSTOM_EFFECTS_DURATION, 20 * 45);
        }
        else if (type == Material.TIPPED_ARROW)
        {
          nbtContainer.setInteger(CucumberyTag.CUSTOM_EFFECTS_DURATION, 10 * 45);
        }
        else
        {
          nbtContainer.setInteger(CucumberyTag.CUSTOM_EFFECTS_DURATION, 20 * 60 * 3);
        }
        nbtContainer.setInteger(CucumberyTag.CUSTOM_EFFECTS_AMPLIFIER, 0);
        nbtContainer.setString(CucumberyTag.CUSTOM_EFFECTS_ID, customEffectType.toString());
        nbtContainer.setString(CucumberyTag.CUSTOM_EFFECTS_DISPLAY_TYPE, customEffectType.getDefaultDisplayType().toString());
        potionsTag.addCompound(nbtContainer);
        if (type == Material.SPLASH_POTION || type == Material.LINGERING_POTION)
        {
          NBTCompoundList vanillaPotions = nbtItem.getCompoundList("CustomPotionEffects");
          if (vanillaPotions.isEmpty())
          {
            NBTContainer vanillaPotion = new NBTContainer();
            vanillaPotion.setByte("Id", (byte) (potionType == PotionType.UNCRAFTABLE ? 10 : 16));
            vanillaPotion.setInteger("Duration", 0);
            vanillaPotions.addCompound(vanillaPotion);
          }
          nbtItem.setInteger("CustomPotionColor", potionType == PotionType.UNCRAFTABLE ? 15007977 : 3298470);
        }
      }
    }
    boolean hasOnlyNbtTagLore = ItemLoreUtil.hasOnlyNbtTagLore(itemStack);
    // 아이템의 등급
    ItemCategory.Rarity rarity = ItemCategory.getItemRarirty(type);
    // 아이템의 등급(숫자)
    long rarityValue = rarity.getRarityValue();
    ItemMeta itemMeta = itemStack.getItemMeta();
    // 기본 설명 추가(공백, 종류, 등급)
    // 아이템의 맨 첫번째 설명은 아이템의 등급 수치 추가
    List<Component> defaultLore = new ArrayList<>(Collections.singletonList(Component.translatable("").args(Component.text(rarityValue))));
    // 그 다음 2번째 설명에는 아이템의 종류를 추가
    TranslatableComponent itemGroup;
    ItemCategory.ItemCategoryType itemCategoryType = ItemCategory.getItemCategoryType(type);
    switch (itemCategoryType)
    {
      case BUILDING -> itemGroup = Component.translatable("itemGroup.buildingBlocks");
      case REDSTONE, TRANSPORTATION, MISC, FOOD, COMBAT, BREWING, DECORATIONS, TOOLS -> itemGroup = Component.translatable("itemGroup." + itemCategoryType.toString().toLowerCase());
      case ENCHANTED_BOOK -> itemGroup = Component.translatable("item.minecraft.enchanted_book");
      case CHEAT_ONLY -> itemGroup = Component.translatable("selectWorld.cheats");
      default -> itemGroup = Component.translatable("selectWorld.versionUnknown");
    }
    Component itemGroupComponent = ComponentUtil.translate("&7아이템 종류 : [%s]", itemGroup);
    defaultLore.add(itemGroupComponent);
    // 그 다음 3번째 설명에는 아이템의 등급을 추가
    Component itemRarityComponent = ComponentUtil.translate("&7아이템 등급 : %s", ComponentUtil.translate(rarity.getDisplay()));
    defaultLore.add(itemRarityComponent);
    itemMeta.lore(defaultLore);
    itemStack.setItemMeta(itemMeta);

    // 이후 아이템의 추가 설명
    ItemLore2.setItemLore(itemStack, itemMeta, defaultLore, params);

    // 이후 아이템 최하단의 회색 설명 추가
    ItemLore3.setItemLore(itemStack);
    itemMeta = itemStack.getItemMeta();
    defaultLore = itemMeta.lore();
    itemStack.setItemMeta(itemMeta);
    ItemLore4.setItemLore(itemStack);
    ItemLoreUtil.removeInventoryItemLore(itemStack);
    itemMeta = itemStack.getItemMeta();
    if (defaultLore != null && defaultLore.size() > 48)
    {
      int remove = 0;
      while (defaultLore.size() >= 48)
      {
        remove++;
        defaultLore.remove(4);
      }
      defaultLore.add(4, ComponentUtil.translate("&7&o설명 %s개 중략...", remove));
    }
    // 그리고 만약 (+NBT) 설명만 추가되어 있는 아이템이였다면 최하단에 [NBT 태그 복사됨] 설명 추가
    if (hasOnlyNbtTagLore)
    {
      if (defaultLore != null)
      {
        defaultLore.add(Component.empty());
        defaultLore.add(ComponentUtil.translate("#52ee52;&o" + Constant.TMI_LORE_NBT_TAG_COPIED));
      }
    }
    itemMeta.lore(defaultLore);
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }


  @NotNull
  public static ItemStack removeItemLore(@NotNull ItemStack itemStack)
  {
    if (!ItemLoreUtil.isCucumberyTMIFood(itemStack))
    {
      return itemStack;
    }
    ItemMeta itemMeta = itemStack.getItemMeta();
    itemMeta.lore(null);
    itemMeta.removeItemFlags(ItemFlag.values());
    itemStack.setItemMeta(itemMeta);
    NBTItem nbtItem = new NBTItem(itemStack);
    nbtItem.removeKey(CucumberyTag.KEY_TMI);
    nbtItem.removeKey("Rarity");
    itemStack.setItemMeta(nbtItem.getItem().getItemMeta());
    return itemStack;
  }
}
