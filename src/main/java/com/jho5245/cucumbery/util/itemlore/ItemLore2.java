package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.PlaceHolderUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.CucumberyHideFlag;
import com.jho5245.cucumbery.util.storage.data.Constant.ExtraTag;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig.UserData;
import com.jho5245.cucumbery.util.storage.no_groups.ItemCategory;
import com.jho5245.cucumbery.util.storage.no_groups.ItemCategory.Rarity;
import de.tr7zw.changeme.nbtapi.*;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemLore2
{
  protected static ItemStack setItemLore(@NotNull ItemStack item, ItemMeta itemMeta, List<Component> lore, @Nullable Object params)
  {
    Player player = params instanceof Player p ? p : null;
    Player viewer = params instanceof ItemLoreView view ? view.player() : null;
    Material type = item.getType();
    NBTItem nbtItem = new NBTItem(item.clone());
    String customType = nbtItem.getString("id") + "";
    // CustomMaterial read
    final CustomMaterial customMaterial = CustomMaterial.itemStackOf(item);
    {
      try
      {
        type = Material.valueOf(customType.toUpperCase());
      }
      catch (Exception ignored)
      {

      }
    }
    // 아이템 태그
    @Nullable NBTCompound itemTag = nbtItem.getCompound(CucumberyTag.KEY_MAIN);
    if (lore == null)
    {
      lore = new ArrayList<>();
    }
    // 기본 내구도 커스텀
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
    // 기본 내구도 커스텀 태그 추가
    NBTCompound duraTag = itemTag != null ? itemTag.getCompound(CucumberyTag.CUSTOM_DURABILITY_KEY) : null;
    if (defaultConfigDura > 0 && duraTag == null)
    {
      if (itemTag == null)
      {
        itemTag = nbtItem.addCompound(CucumberyTag.KEY_MAIN);
      }
      duraTag = itemTag.addCompound(CucumberyTag.CUSTOM_DURABILITY_KEY);
      duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_CURRENT_KEY, 0L);
      duraTag.setLong(CucumberyTag.CUSTOM_DURABILITY_MAX_KEY, defaultConfigDura);
      itemMeta = nbtItem.getItem().getItemMeta();
    }
    // 아이템 이름 새기기
    NBTList<String> extraTags = NBTAPI.getStringList(itemTag, CucumberyTag.EXTRA_TAGS_KEY);
    if (player != null && NBTAPI.arrayContainsValue(extraTags, ExtraTag.NAME_TAG))
    {
      lore.add(Component.empty());
      lore.add(ComponentUtil.translate("&a새겨진 이름 : %s", player.displayName().hoverEvent(null).clickEvent(null)));
    }
    // 아이템 등급 오버라이드
    NBTList<String> hideFlags = NBTAPI.getStringList(itemTag, CucumberyTag.HIDE_FLAGS_KEY);
    boolean hideFlagsTagExists = hideFlags != null;
    NBTCompound customRarityTag = NBTAPI.getCompound(itemTag, CucumberyTag.CUSTOM_RARITY_KEY);
    // 기본 아이템 등급(하단에 추가 설정 있음)
    {
      String customRarityBase = NBTAPI.getString(customRarityTag, CucumberyTag.CUSTOM_RARITY_BASE_KEY);
      if (customRarityBase != null)
      {
        ItemLoreUtil.setItemRarityValue(lore, ItemCategory.Rarity.valueOf(customRarityBase).getRarityValue(), false);
      }
    }
    // 상단 아이템 설명
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
    // 기간제 아이템 설명
    ItemLore2ExpireDate.setItemLore(lore, itemTag, hideFlags);
    // 사용 제한 아이템 설명
    ItemLore2Restriction.setItemLore(item, type, lore, hideFlags);
    // HideFlags
    boolean hideDurability = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.DURABILITY.toString());
    boolean hideDurabilityChanceNotToConsume = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.DURABILITY_CHANCE_NOT_TO_CONSUME.toString());
    boolean isDrill = customMaterial != null && switch (customMaterial)
            {
              case TITANIUM_DRILL_R266, TITANIUM_DRILL_R366, TITANIUM_DRILL_R466, TITANIUM_DRILL_R566, MINDAS_DRILL -> true;
              default -> false;
            };
    boolean hideEnchant = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.ENCHANTS);
    boolean eventAccessMode = viewer != null && UserData.EVENT_EXCEPTION_ACCESS.getBoolean(viewer);
    boolean hideAttributes = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.ATTRIBUTE_MODIFIERS);
    boolean hideStatusEffects = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.STATUS_EFFECTS.toString());
    boolean hideFireworkEffects = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.FIREWORK_EFFECTS.toString());
    boolean hideBlockData = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.BLOCK_DATA);
    boolean hideBlockState = hideFlagsTagExists && NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.BLOCK_STATE);
    // 내구도 설명
    ItemLore2Durability.setItemLore(item, type, customMaterial, itemMeta, lore, duraTag, isDrill, hideDurability, hideDurabilityChanceNotToConsume);
    // 모루 사용 횟수 설명
    ItemLore2Anvil.setItemLore(item, type, itemMeta, lore);
    // 커스텀 채광 - 채광 등급/속도/행운 설명
    ItemLore2CustomMiningAndCustomMaterial.setItemLore(item, type, customMaterial, lore, nbtItem, hideFlags, viewer, params);
    // 인챈트된 아이템/마법이 부여된 책 설명 추가
    ItemLore2Enchant.setItemLore(item, type, itemMeta, lore, viewer, hideEnchant, eventAccessMode);
    // Custom Material일 경우애만 추가할 아이템 설명
    if (customMaterial != null)
    {
      ItemLore2CustomMaterial.setItemLore(viewer, customMaterial, item, itemMeta, lore);
    }
    // Attribute 설명
    ItemLore2Attribute.setItemLore(type, item, itemMeta, lore, hideAttributes);
    // 음식 추가 상태 효과
    NBTCompound foodTag = NBTAPI.getCompound(itemTag, CucumberyTag.FOOD_KEY);
    ItemLore2Food.setItemLore(item, type, lore, foodTag, viewer, hideStatusEffects);
    // Custom Material이 아닌 경우에만 추가할 아이템 설명
    if (customMaterial == null)
    {
      ItemLore2Meta.setItemLore(viewer, item, type, itemMeta, lore, nbtItem, hideFlags, hideBlockData, hideStatusEffects, params);
    }
    // custom material 유무에 상관 없이 아이템 설명 추가
    ItemLore2Meta2.setItemLore(item, type, customMaterial, itemMeta, lore, nbtItem, hideFireworkEffects);
    // 블록 엔티티 태그 설명
    ItemLore2BlockState.setItemLore(item, type, itemMeta, lore, nbtItem, hideBlockState, params);
    // 블록 데이터 태그 설명
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
        lore.add(ComponentUtil.translate("&7%s : %s", "&b" + key, Constant.THE_COLOR_HEX + value));
      }
    }
    // 설치 가능/파괴 가능 설명
    ItemLore2CanPlaceAndDestroys.setItemLore(itemMeta, lore, hideFlags);
    // 이스터 에그(만렙 낚싯대)
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
    // 설치 가능/양조 가능/제련 가능 등등 설명
    ItemLore2Ables.setItemLore(nbtItem, foodTag, hideFlags, item, lore, params);
    // 강화 태그 설명
    NBTCompound reinforceTag = NBTAPI.getCompound(itemTag, CucumberyTag.REINFORCE_TAG);
    if (reinforceTag != null)
    {
      try
      {
        Integer current = reinforceTag.getInteger(CucumberyTag.REINFORCE_CURRENT_TAG), max = reinforceTag.getInteger(CucumberyTag.REINFORCE_MAX_TAG), itemLevel = reinforceTag.getInteger(CucumberyTag.REINFORCE_ITEM_LEVEL_TAG);
        String reinforceType = reinforceTag.getString(CucumberyTag.REINFORCE_REINFORCE_TYPE_TAG), itemType = reinforceTag.getString(CucumberyTag.REINFORCE_ITEM_TYPE_TAG);
        reinforceType = switch (reinforceType)
                {
                  case "starforce" -> "rg255,204;스타포스";
                  case "superior" -> "&b슈페리얼";
                  case "cucumberforce" -> "&a오이포스";
                  case "ebebeb" -> "&d에베벱";
                  default -> reinforceType;
                };
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&3[강화 정보]"));
        if (itemType != null && !itemType.equals(""))
        {
          lore.add(ComponentUtil.translate("rg255,204;아이템 분류 : %s", itemType));
        }
        if (itemLevel == null)
        {
          itemLevel = 10;
        }
        lore.add(ComponentUtil.translate("rg255,204;아이템 강화 등급(장착 레벨) : %s", itemLevel));
        lore.add(ComponentUtil.translate("&f%s : %s / %s", reinforceType, current, max));
        List<String> keys = new ArrayList<>();
        reinforceTag.getKeys().forEach(s ->
        {
          if (reinforceTag.getType(s) == NBTType.NBTTagDouble)
          {
            keys.add(s);
          }
        });
        if (!keys.isEmpty())
        {
          lore.add(ComponentUtil.translate("&8" + Constant.SEPARATOR));
          if (reinforceTag.getType("STR") == NBTType.NBTTagDouble)
          {
            double d = reinforceTag.getDouble("STR");
            lore.add(ComponentUtil.translate("&f%s : %s", "STR", (d > 0 ? "+" : "") + Constant.Sosu2.format(d)));
          }
          if (reinforceTag.getType("DEX") == NBTType.NBTTagDouble)
          {
            double d = reinforceTag.getDouble("DEX");
            lore.add(ComponentUtil.translate("&f%s : %s", "DEX", (d > 0 ? "+" : "") + Constant.Sosu2.format(d)));
          }
          if (reinforceTag.getType("INT") == NBTType.NBTTagDouble)
          {
            double d = reinforceTag.getDouble("INT");
            lore.add(ComponentUtil.translate("&f%s : %s", "INT", (d > 0 ? "+" : "") + Constant.Sosu2.format(d)));
          }
          if (reinforceTag.getType("LUK") == NBTType.NBTTagDouble)
          {
            double d = reinforceTag.getDouble("LUK");
            lore.add(ComponentUtil.translate("&f%s : %s", "LUK", (d > 0 ? "+" : "") + Constant.Sosu2.format(d)));
          }
          if (reinforceTag.getType("최대 HP") == NBTType.NBTTagDouble)
          {
            double d = reinforceTag.getDouble("최대 HP");
            lore.add(ComponentUtil.translate("&f%s : %s", "최대 HP", (d > 0 ? "+" : "") + Constant.Sosu2.format(d)));
          }
          if (reinforceTag.getType("최대 MP") == NBTType.NBTTagDouble)
          {
            double d = reinforceTag.getDouble("최대 MP");
            lore.add(ComponentUtil.translate("&f%s : %s", "최대 MP", (d > 0 ? "+" : "") + Constant.Sosu2.format(d)));
          }
          if (reinforceTag.getType("공격력") == NBTType.NBTTagDouble)
          {
            double d = reinforceTag.getDouble("공격력");
            lore.add(ComponentUtil.translate("&f%s : %s", "공격력", (d > 0 ? "+" : "") + Constant.Sosu2.format(d)));
          }
          if (reinforceTag.getType("마력") == NBTType.NBTTagDouble)
          {
            double d = reinforceTag.getDouble("마력");
            lore.add(ComponentUtil.translate("&f%s : %s", "마력", (d > 0 ? "+" : "") + Constant.Sosu2.format(d)));
          }
          if (reinforceTag.getType("이동속도") == NBTType.NBTTagDouble)
          {
            double d = reinforceTag.getDouble("이동속도");
            lore.add(ComponentUtil.translate("&f%s : %s", "이동속도", (d > 0 ? "+" : "") + Constant.Sosu2.format(d)));
          }
          if (reinforceTag.getType("점프력") == NBTType.NBTTagDouble)
          {
            double d = reinforceTag.getDouble("점프력");
            lore.add(ComponentUtil.translate("&f%s : %s", "점프력", (d > 0 ? "+" : "") + Constant.Sosu2.format(d)));
          }
          lore.add(ComponentUtil.translate("&8" + Constant.SEPARATOR));
        }
      }
      catch (Exception ignored)
      {

      }
    }
    // 커스텀 아이템 유형
    String customItemType = NBTAPI.getString(itemTag, CucumberyTag.CUSTOM_ITEM_TYPE_KEY);
    if (customItemType != null)
    {
      lore.set(1, ComponentUtil.translate("&7아이템 종류 : [%s]", customItemType));
    }
    // 커스텀 아이템 등급(숫자놀이)
    if (customRarityTag != null && customRarityTag.hasTag(CucumberyTag.VALUE_KEY))
    {
      long rarity = customRarityTag.getLong(CucumberyTag.VALUE_KEY);
      ItemLoreUtil.setItemRarityValue(lore, rarity);
    }
    // 커스텀 아이템 등급(강제 설정)
    String customRarityFinal = NBTAPI.getString(customRarityTag, CucumberyTag.CUSTOM_RARITY_FINAL_KEY);
    if (customRarityFinal != null)
    {
      try
      {
        if (!customRarityFinal.startsWith("_"))
        {
          ItemLoreUtil.setItemRarityValue(lore, ItemCategory.Rarity.valueOf(customRarityFinal).getRarityValue(), false);
        }
      }
      catch (Exception ignored)
      {

      }
    }
    // 내구도 무한 아이템이 아니면 내구도 무한 설명 숨김 태그 제거
    if (!itemMeta.isUnbreakable() && itemMeta.hasItemFlag(ItemFlag.HIDE_UNBREAKABLE))
    {
      itemMeta.removeItemFlags(ItemFlag.HIDE_UNBREAKABLE);
    }
    // 아이템 이름 속성
    @Nullable Component displayName;
    try
    {
      displayName = itemMeta.displayName();
    }
    catch (Exception e)
    {
      displayName = null;
    }
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
      TranslatableComponent translatableComponent = ComponentUtil.translate("%s%s%s");
      List<Component> arguments = new ArrayList<>();
      Component prefixComponent = Component.empty();
      for (ReadWriteNBT nbtCompound : prefix)
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
      for (ReadWriteNBT nbtCompound : suffix)
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
    Rarity rarity = Rarity.getRarityFromValue(rarity2);
    if (customMaterial != null && customMaterial.getRarity().toString().startsWith("_"))
    {
      rarity = customMaterial.getRarity();
    }
    if (ItemCategory.getItemRarirty(type).toString().startsWith("_"))
    {
      rarity = ItemCategory.getItemRarirty(type);
    }
    if (customRarityFinal != null && customRarityFinal.startsWith("_"))
    {
      try
      {
        rarity = Rarity.valueOf(customRarityFinal);
      }
      catch (Exception ignored)
      {

      }
    }
    boolean rarityUpgraded = (nbtItem.hasTag("RarityUpgraded") && nbtItem.getType("RarityUpgraded") == NBTType.NBTTagByte && Objects.equals(nbtItem.getBoolean("RarityUpgraded"), true));
    if (rarityUpgraded)
    {
      rarity = rarity.tierUp();
    }
    String rarityDisplay = rarity.getDisplay();
    Component rarityComponent = ComponentUtil.translate(rarityDisplay);
    if (rarityUpgraded)
    {
      rarityComponent = ComponentUtil.translate("A %s A",
              rarityComponent.decoration(TextDecoration.OBFUSCATED, State.FALSE))
              .decoration(TextDecoration.OBFUSCATED, State.TRUE)
              .decoration(TextDecoration.BOLD, rarityComponent.decoration(TextDecoration.BOLD))
              .color(rarityComponent.color()
              );
    }
    Component itemRarityComponent = ComponentUtil.translate("&7아이템 등급 : %s", rarityComponent);
    lore.set(2, itemRarityComponent);
    itemMeta.lore(lore);
    item.setItemMeta(itemMeta);
    return item;
  }
}
