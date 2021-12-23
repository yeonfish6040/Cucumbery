package com.jho5245.cucumbery.util.storage;

import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
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
  @Deprecated
  public static ItemStack newItem(Material type, int amount, String name, List<String> lore, boolean hideItemFlag)
  {
    ItemStack item = new ItemStack(type, amount);
    ItemMeta meta = item.getItemMeta();
    meta.setDisplayName(MessageUtil.n2s(name));
    if (hideItemFlag)
    {
      meta.addItemFlags(ItemFlag.values());
    }
    if (lore != null)
    {
      for (int i = 0; i < lore.size(); i++)
      {
        String str = lore.get(i);
        if (str == null)
        {
          continue;
        }
        lore.set(i, MessageUtil.n2s(str));
      }
    }
    meta.setLore(lore);
    item.setItemMeta(meta);
    return item;
  }

  @Deprecated
  public static ItemStack newItem2(Material type, int amount, String name, List<Component> lore, boolean hideItemFlag)
  {
    ItemStack item = new ItemStack(type, amount);
    ItemMeta meta = item.getItemMeta();
    meta.displayName(ComponentUtil.create(name));
    if (hideItemFlag)
    {
      meta.addItemFlags(ItemFlag.values());
    }
    if (lore != null)
    {
      for (int i = 0; i < lore.size(); i++)
      {
        Component str = lore.get(i);
        if (str == null)
        {
          continue;
        }
        lore.set(i, str);
      }
    }
    meta.lore(lore);
    item.setItemMeta(meta);
    return item;
  }

  @Deprecated
  public static ItemStack newItem(Material type, int amount, String name, boolean hideItemFlag)
  {
    return CreateItemStack.newItem(type, amount, name, new ArrayList<String>(), hideItemFlag);
  }

  @Deprecated
  public static ItemStack newItem(Material type, int amount, String name, String singleLore, boolean hideItemFlag)
  {
    List<String> newLore = new ArrayList<>(Collections.singletonList(singleLore));
    return CreateItemStack.newItem(type, amount, name, newLore, hideItemFlag);
  }

  @Deprecated
  public static ItemStack toggleItem(boolean bool, String trueName, List<String> trueLore, String falseName, List<String> falseLore)
  {
    ItemStack item;
    if (bool)
    {
      item = newItem(Material.LIME_DYE, 1, trueName, trueLore, true);
    }
    else
    {
      item = newItem(Material.GRAY_DYE, 1, falseName, falseLore, true);
    }
    return item;
  }

  @Deprecated
  public static ItemStack toggleItem(boolean bool, String itemName, List<String> sameLore, List<String> trueLore, List<String> falseLore)
  {
    ItemStack item;
    List<String> lore = new ArrayList<>(sameLore);
    if (bool)
    {
      if (trueLore != null)
      {
        lore.addAll(trueLore);
      }
      item = newItem(Material.LIME_DYE, 1, itemName, lore, true);
    }
    else
    {
      if (falseLore != null)
      {
        lore.addAll(falseLore);
      }
      item = newItem(Material.GRAY_DYE, 1, itemName, lore, true);
    }
    return item;
  }

  @NotNull
  public static ItemStack create(@NotNull Material type, @Nullable Component name)
  {
    return create(type, 1, name, (List<Component>) null, true);
  }

  @NotNull
  public static ItemStack create(@NotNull Material type, @Nullable Component name, boolean hideItemFlag)
  {
    return create(type, 1, name, (List<Component>) null, hideItemFlag);
  }

  @NotNull
  public static ItemStack create(@NotNull Material type, int amount, @Nullable Component name, boolean hideItemFlag)
  {
    return create(type, amount, name, (List<Component>) null, hideItemFlag);
  }

  @NotNull
  public static ItemStack create(@NotNull Material type, int amount, @Nullable Component name, @Nullable Component singleLore, boolean hideItemFlag)
  {
    return create(type, amount, name, Collections.singletonList(singleLore), hideItemFlag);
  }

  @NotNull
  public static ItemStack create(@NotNull Material type, int amount, @Nullable Component name, @Nullable List<Component> lore, boolean hideItemFlag)
  {
    ItemStack itemStack = new ItemStack(type, amount);
    ItemMeta itemMeta = itemStack.getItemMeta();
    itemMeta.displayName(name);
    itemMeta.lore(lore);
    if (hideItemFlag)
    {
      itemMeta.addItemFlags(ItemFlag.values());
    }
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }
}
