package com.jho5245.cucumbery.util.storage;

import com.jho5245.cucumbery.util.MessageUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateItemStack
{
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
          continue;
        lore.set(i, MessageUtil.n2s(str));
      }
    }
    meta.setLore(lore);
    item.setItemMeta(meta);
    return item;
  }

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
          continue;
        lore.set(i, str);
      }
    }
    meta.lore(lore);
    item.setItemMeta(meta);
    return item;
  }

  public static ItemStack newItem(Material type, int amount, String name, boolean hideItemFlag)
  {
    return CreateItemStack.newItem(type, amount, name, new ArrayList<String>(), hideItemFlag);
  }

  public static ItemStack newItem(Material type, int amount, String name, String singleLore, boolean hideItemFlag)
  {
    List<String> newLore = new ArrayList<>(Collections.singletonList(singleLore));
    return CreateItemStack.newItem(type, amount, name, newLore, hideItemFlag);
  }

  public static ItemStack toggleItem(boolean bool, String trueName, List<String> trueLore, String falseName, List<String> falseLore)
  {
    ItemStack item;
    if (bool)
      item = newItem(Material.LIME_DYE, 1, trueName, trueLore, true);
    else
      item = newItem(Material.GRAY_DYE, 1, falseName, falseLore, true);
    return item;
  }

  public static ItemStack toggleItem(boolean bool, String itemName, List<String> sameLore, List<String> trueLore, List<String> falseLore)
  {
    ItemStack item;
    List<String> lore = new ArrayList<>(sameLore);
    if (bool)
    {
      if (trueLore != null)
        lore.addAll(trueLore);
      item = newItem(Material.LIME_DYE, 1, itemName, lore, true);
    }
    else
    {
      if (falseLore != null)
        lore.addAll(falseLore);
      item = newItem(Material.GRAY_DYE, 1, itemName, lore, true);
    }
    return item;
  }
}
