package com.jho5245.cucumbery.util.storage.no_groups;

import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant.ExtraTag;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateItemStack
{
  public static ItemStack toggleItem(boolean bool, String trueName, List<?> trueLore, String falseName, List<?> falseLore)
  {
    ItemStack item;
    if (bool)
    {
      item = create(Material.LIME_DYE, 1, trueName, trueLore, true);
    }
    else
    {
      item = create(Material.GRAY_DYE, 1, falseName, falseLore, true);
    }
    return item;
  }

  public static <T> ItemStack toggleItem(boolean bool, T t, List<T> sameLore, List<T> trueLore, List<T> falseLore)
  {
    ItemStack item;
    List<T> lore = new ArrayList<>(sameLore);
    if (bool)
    {
      if (trueLore != null)
      {
        lore.addAll(trueLore);
      }
      item = create(Material.LIME_DYE, 1, t, lore, true);
    }
    else
    {
      if (falseLore != null)
      {
        lore.addAll(falseLore);
      }
      item = create(Material.GRAY_DYE, 1, t, lore, true);
    }
    return item;
  }

  @NotNull
  public static ItemStack create(@NotNull Material type, @Nullable Object name)
  {
    return create(type, 1, name, null, true);
  }

  @NotNull
  public static ItemStack create(@NotNull Material type, @Nullable Object name, boolean hideItemFlag)
  {
    return create(type, 1, name, null, hideItemFlag);
  }

  @NotNull
  public static ItemStack create(@NotNull Material type, int amount, @Nullable Object name, boolean hideItemFlag)
  {
    return create(type, amount, name, null, hideItemFlag);
  }

  @NotNull
  public static ItemStack create(@NotNull Material type, int amount, @Nullable Object name, @Nullable Object singleLore, boolean hideItemFlag)
  {
    return create(type, amount, name, Collections.singletonList(singleLore), hideItemFlag);
  }

  @NotNull
  public static ItemStack create(@NotNull Material type, int amount, @Nullable Object name, @Nullable List<?> lore, boolean hideItemFlag)
  {
    ItemStack itemStack = new ItemStack(type, amount);
    NBTItem nbtItem = new NBTItem(itemStack, true);
    nbtItem.addCompound(CucumberyTag.KEY_MAIN).getStringList(CucumberyTag.EXTRA_TAGS_KEY).add(ExtraTag.NO_TMI_LORE.toString());
    ItemMeta itemMeta = itemStack.getItemMeta();
    itemMeta.displayName(name instanceof Component component ? component : name != null ? ComponentUtil.create(name) : null);
    List<Component> list = new ArrayList<>();
    if (lore != null)
    {
      lore.forEach(o ->
      {
        if (o instanceof Component component)
        {
          list.add(component);
        }
        else
        {
          list.add(ComponentUtil.create(o));
        }
      });
    }
    itemMeta.lore(list);
    if (hideItemFlag)
    {
      itemMeta.addItemFlags(ItemFlag.values());
    }
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }

  @NotNull
  public static ItemStack getPreviousButton(@NotNull Component previousTitle)
  {
    ItemStack itemStack = new ItemStack(Material.ARROW);
    ItemMeta itemMeta = itemStack.getItemMeta();
    itemMeta.displayName(ComponentUtil.translate("&b이전 메뉴로"));
    itemMeta.lore(Collections.singletonList(ComponentUtil.translate("&8(%s)", previousTitle)));
    itemStack.setItemMeta(itemMeta);
    NBTItem nbtItem = new NBTItem(itemStack, true);
    nbtItem.setBoolean(CucumberyTag.GUI_BUTTON_PREVIOUS, true);
    itemStack.setItemMeta(nbtItem.getItem().getItemMeta());
    return itemStack;
  }

  public static boolean isPreviousButton(@NotNull ItemStack itemStack)
  {
    if (itemStack.getType() != Material.ARROW)
    {
      return false;
    }
    NBTItem nbtItem = new NBTItem(itemStack);
    Boolean b = nbtItem.getBoolean(CucumberyTag.GUI_BUTTON_PREVIOUS);
    return b != null && b;
  }
}
