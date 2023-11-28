package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.custom.customeffect.type.CustomEffectType;
import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.no_groups.MessageUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import com.jho5245.cucumbery.util.storage.data.Constant.CucumberyHideFlag;
import com.jho5245.cucumbery.util.storage.data.Constant.ExtraTag;
import com.jho5245.cucumbery.util.storage.data.CustomMaterial;
import com.jho5245.cucumbery.util.storage.data.Variable;
import com.jho5245.cucumbery.util.storage.no_groups.ItemCategory;
import com.jho5245.cucumbery.util.storage.no_groups.ItemCategory.Rarity;
import de.tr7zw.changeme.nbtapi.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.CreativeCategory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
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
    if (!Cucumbery.config.getBoolean("use-helpful-lore-feature") || NBTAPI.arrayContainsValue(extraTagsReadOnly, ExtraTag.NO_TMI_LORE.toString()))
    {
      ItemLore.removeItemLore(itemStack);
      return itemStack;
    }
    ItemMeta itemMeta = itemStack.getItemMeta();
    Component displayName = itemMeta.displayName();
    // config 설정 - 아이템에 이름이 있을 경우 아이템 설명 업데이트 안함(모루로 수정한 아이템은 제외)
    if (!Cucumbery.config.getBoolean("use-helpful-lore-feature-with-named-items") && displayName != null)
    {
      if (displayName.color() != null || displayName.decoration(TextDecoration.ITALIC) != State.NOT_SET || displayName.decoration(TextDecoration.BOLD) != State.NOT_SET || displayName.decoration(TextDecoration.OBFUSCATED) != State.NOT_SET || displayName.decoration(TextDecoration.STRIKETHROUGH) != State.NOT_SET || displayName.decoration(TextDecoration.UNDERLINED) != State.NOT_SET)
      {
        ItemLore.removeItemLore(itemStack);
        return itemStack;
      }
    }
    Material type = itemStack.getType();
    NBTItem nbtItem = new NBTItem(itemStack, true);
    nbtItem.removeKey("CustomMiningUpdater");
    String customType = nbtItem.getString("id");
    CustomMaterial customMaterial = CustomMaterial.itemStackOf(itemStack);
    {
      try
      {
        type = Material.valueOf(customType.toUpperCase());
      }
      catch (Exception ignored)
      {

      }
    }
    if (customMaterial != null)
    {
      ItemLoreCustomItem.itemLore(itemStack, nbtItem, customMaterial);
      itemMeta = itemStack.getItemMeta();
    }
    if (!customType.equals(""))
    {
      ItemLoreCustomItem.itemLore(itemStack, nbtItem, customType);
      itemMeta = itemStack.getItemMeta();
    }
    boolean hasOnlyNbtTagLore = ItemLoreUtil.hasOnlyNbtTagLore(itemStack) || new NBTItem(itemStack).hasTag("NBTCopied") && new NBTItem(itemStack).getBoolean("NBTCopied");
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
      }
    }
    if (hasOnlyNbtTagLore)
    {
      nbtItem.setBoolean("NBTCopied", true);
    }

    // 아이템의 등급
    Rarity rarity = ItemCategory.getItemRarirty(type);
    TranslatableComponent itemGroup;
    CreativeCategory itemCategoryType = type.getCreativeCategory();
    if (itemCategoryType == null)
    {
      itemGroup = switch (type)
      {
        case SUSPICIOUS_STEW -> ComponentUtil.translate(CreativeCategory.FOOD.translationKey());
        case ENCHANTED_BOOK -> ComponentUtil.translate(Material.ENCHANTED_BOOK.translationKey());
        case WRITTEN_BOOK -> ComponentUtil.translate(CreativeCategory.MISC.translationKey());
        default -> ComponentUtil.translate("치트");
      };
    }
    else
    {
      itemGroup = ComponentUtil.translate(itemCategoryType.translationKey());
    }
    if (customMaterial != null)
    {
      itemGroup = ComponentUtil.translate(customMaterial.getCategory());
      rarity = customMaterial.getRarity();
    }
    if (!customType.equals(""))
    {
      try
      {
        ConfigurationSection root = Variable.customItemsConfig.getConfigurationSection(customType);
        if (root != null)
        {
          String r = root.getString("Rarity");
          if (r != null)
          {
            rarity = Rarity.valueOf(r);
          }
          String g = root.getString("ItemGroup");
          if (g != null)
          {
            itemGroup = ComponentUtil.translate(g, true);
          }
        }
      }
      catch (Exception ignored)
      {

      }
    }
    // 아이템의 등급(숫자)
    long rarityValue = rarity.getRarityValue();
    // 기본 설명 추가(공백, 종류, 등급)
    // 아이템의 맨 첫번째 설명은 아이템의 등급 수치 추가
    List<Component> defaultLore = new ArrayList<>(Collections.singletonList(ComponentUtil.translate(ItemLoreUtil.FIRST_LINE_EMPTY_LORE).args(Component.text(rarityValue))));
    // 그 다음 2번째 설명에는 아이템의 종류를 추가
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
    ItemLore3.setItemLore(params instanceof ItemLoreView view ? view.getPlayer() : null, itemStack, defaultLore);
    itemMeta = itemStack.getItemMeta();
    itemStack.setItemMeta(itemMeta);
    ItemLore4.setItemLore(itemStack, defaultLore);
    ItemLoreUtil.removeInventoryItemLore(itemStack);
    itemMeta = itemStack.getItemMeta();
    // 그리고 만약 (+NBT) 설명만 추가되어 있는 아이템이였다면 최하단에 [NBT 태그 복사됨] 설명 추가
    if (hasOnlyNbtTagLore)
    {
      defaultLore.add(Component.empty());
      defaultLore.add(ComponentUtil.translate("#52ee52;" + Constant.TMI_LORE_NBT_TAG_COPIED));
    }
    if (!NBTAPI.arrayContainsValue(NBTAPI.getStringList(NBTAPI.getMainCompound(itemStack), CucumberyTag.HIDE_FLAGS_KEY), CucumberyHideFlag.ORIGINAL_DISPLAY_NAME))
    {
      if (customMaterial != null)
      {
        if (displayName != null && displayName.color() == null && displayName.decoration(TextDecoration.ITALIC) == State.NOT_SET)
        {
          defaultLore.add(Component.empty());
          defaultLore.add(ComponentUtil.translate("&7원래 아이템 이름 : %s", customMaterial.getDisplayName()));
        }
      }
      else if (nbtItem.hasTag("id") && !"".equals(nbtItem.getString("id")))
      {
        String id = nbtItem.getString("id");
        ConfigurationSection section = Variable.customItemsConfig.getConfigurationSection(id);
        if (section != null)
        {
          String configDisplayName = section.getString("display-name");
          if (configDisplayName == null)
          {
            throw new NullPointerException("display name cannot be null! CustomItem.yml - " + id);
          }
          Component component = ComponentUtil.create(MessageUtil.n2s(configDisplayName)), itemDisplayName = itemMeta.displayName();
          if (itemDisplayName != null && itemDisplayName.color() == null && itemDisplayName.decoration(TextDecoration.ITALIC) == State.NOT_SET)
          {
            defaultLore.add(Component.empty());
            defaultLore.add(ComponentUtil.translate("&7원래 아이템 이름 : %s", component));
          }
        }
      }
      else if (itemMeta.hasDisplayName())
      {
        ItemStack clone = itemStack.clone();
        ItemMeta cloneMeta = clone.getItemMeta();
        cloneMeta.displayName(null);
        clone.setItemMeta(cloneMeta);
        defaultLore.add(Component.empty());
        defaultLore.add(ComponentUtil.translate("&7원래 아이템 이름 : %s", ItemNameUtil.itemName(clone)));
      }
    }
    defaultLore.replaceAll(ComponentUtil::stripEvent);
    final int maxWidth = Cucumbery.config.getInt("max-item-lore-width");
    if (maxWidth >= 20)
    {
      for (int i = 1; i < defaultLore.size(); i++)
      {
        if (defaultLore.size() > 100)
        {
          break;
        }
        String serial = MessageUtil.stripColor(ComponentUtil.serialize(defaultLore.get(i)));
        if (serial.startsWith("YamlConfiguration[path") || serial.replace(" ", "").isEmpty())
        {
          continue;
        }
        while (serial.startsWith(" "))
        {
          serial = serial.substring(1);
        }
        List<String> list = new ArrayList<>();
        int totalLength = serial.length();
        for (char c : serial.toCharArray())
        {
          String s = String.valueOf(c);
          s += (String.valueOf(c)).repeat(3);
          totalLength += 3;
          if ((String.valueOf(c)).matches("[A-Za-z0-9\" _-]") && c != 'i')
          {
            s += (String.valueOf(c)).repeat(7);
            totalLength += 7;
          }
          if ((String.valueOf(c)).matches("[ㄱ-ㅎㅏ-ㅣ가-힣]"))
          {
            s += (String.valueOf(c)).repeat(12);
            totalLength += 12;
          }
          list.add(s);
        }
        int diff = totalLength - serial.length();
        try
        {
          if (serial.length() + diff >= maxWidth)
          {
            double stack = 0d;
            for (String s : list)
            {
              if (stack > serial.length() || stack > maxWidth / (1d * (diff + serial.length()) / serial.length()))
              {
                break;
              }
              int l = s.length();
              stack += l / 5d;
            }
            int size = (int) Math.round(stack);
            Component component = ComponentUtil.create(serial.substring(0, Math.max(size, serial.length())));
            if (component.color() == null)
            {
              component = component.color(defaultLore.get(i).color());
            }
            component = component.decorations(defaultLore.get(i).decorations());
            defaultLore.set(i, component);
            serial = serial.substring(size);
            while (serial.startsWith(" "))
            {
              serial = serial.substring(1);
            }
            if (!serial.isEmpty())
            {
              component = ComponentUtil.create(serial);
              if (component.color() == null)
              {
                component = component.color(defaultLore.get(i).color());
              }
              component = component.decorations(defaultLore.get(i).decorations());
              defaultLore.add(i + 1, component);
            }
          }
        }
        catch (IndexOutOfBoundsException e)
        {
Cucumbery.getPlugin().getLogger().warning(          e.getMessage());
        }
      }
    }
    if (defaultLore.size() > 48)
    {
      int remove = 0;
      while (defaultLore.size() >= 47)
      {
        remove++;
        defaultLore.remove(46);
      }
      defaultLore.add(46, ComponentUtil.translate("&7&o설명 %s개 중략...", remove));
    }
    itemMeta.lore(defaultLore);
    itemStack.setItemMeta(itemMeta);
    String displayId = nbtItem.getString("DisplayId");
    try
    {
      Material material = Material.valueOf(displayId.toUpperCase());
      if (material.isItem() && !material.isAir())
      {
        itemStack.setType(material);
      }
    }
    catch (Exception ignored)
    {

    }
    return itemStack;
  }

  @NotNull
  public static ItemStack removeItemLore(@NotNull ItemStack itemStack)
  {
    return removeItemLore(itemStack, false);
  }

  @NotNull
  public static ItemStack removeItemLore(@NotNull ItemStack itemStack, boolean removeUUID)
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
    itemStack.setItemMeta(nbtItem.getItem().getItemMeta());
    if (nbtItem.hasTag("id"))
    {
      itemMeta = itemStack.getItemMeta();
      CustomMaterial customMaterial = CustomMaterial.itemStackOf(itemStack);
      if (customMaterial != null && customMaterial.getDisplayName().equals(itemMeta.displayName()))
      {
        itemMeta.displayName(null);
      }
      itemStack.setItemMeta(itemMeta);
      if (itemMeta instanceof SkullMeta skullMeta)
      {
        skullMeta.setOwningPlayer(null);
        itemStack.setItemMeta(skullMeta);
      }
      if (removeUUID && nbtItem.hasTag("uuid"))
      {
        nbtItem = new NBTItem(itemStack, true);
        nbtItem.removeKey("uuid");
      }
    }
    return itemStack;
  }
}
