package com.jho5245.cucumbery.util.itemlore;

import com.destroystokyo.paper.Namespaced;
import com.destroystokyo.paper.NamespacedTag;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.component.util.ItemNameUtil;
import com.jho5245.cucumbery.util.storage.data.Constant.CucumberyHideFlag;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemLore2CanPlaceAndDestroys
{
  protected static void setItemLore(@NotNull ItemMeta itemMeta, @NotNull List<Component> lore, @Nullable NBTList<String> hideFlags)
  {
    if (itemMeta.hasDestroyableKeys())
    {
      itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
      if (!NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.CAN_DESTROY))
      {
        Set<Namespaced> destroyableKeys = itemMeta.getDestroyableKeys();
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6[%s]", ComponentUtil.translate("item.canBreak")));
        Set<Material> materials = new HashSet<>();
        for (Namespaced namespaced : destroyableKeys)
        {
          if (namespaced.getNamespace().equals("minecraft"))
          {
            if (namespaced instanceof NamespacedKey namespacedKey)
            {
              try
              {
                materials.add(Material.valueOf(namespacedKey.getKey().toUpperCase()));
              }
              catch (Exception ignored)
              {

              }
            }
            if (namespaced instanceof NamespacedTag namespacedTag)
            {
              Iterable<Tag<Material>> tags = Bukkit.getTags(Tag.REGISTRY_BLOCKS, Material.class);
              for (Tag<Material> tag : tags)
              {
                NamespacedKey namespacedKey = tag.getKey();
                if (namespacedTag.getNamespace().equals(namespacedKey.getNamespace()) && namespacedTag.getKey().equals(namespacedKey.getKey()))
                {
                  materials.addAll(tag.getValues());
                }
              }
            }
          }
          else
          {
            lore.add(ComponentUtil.translate("&7block." + namespaced.getNamespace() + "." + namespaced.getKey()));
          }
        }
        int size = materials.size();
        boolean tooMany = materials.size() > 50;
        if (materials.size() > 50)
        {
          List<Material> materialList = new ArrayList<>(materials);
          while (materialList.size() > 50)
          {
            materialList.remove(49);
          }
          materials = new HashSet<>(materialList);
        }
        if (materials.size() > 15 && Cucumbery.config.getInt("max-item-lore-width") >= 20)
        {
          StringBuilder key = new StringBuilder("&7");
          List<Component> args = new ArrayList<>();
          for (Material material : materials)
          {
            key.append("%s, ");
            args.add(ItemNameUtil.itemName(material, NamedTextColor.GRAY));
          }
          key = new StringBuilder(key.substring(0, key.length() - 2));
          lore.add(ComponentUtil.translate(key.toString(), args));
        }
        else
        {
          for (Material material : materials)
          {
            lore.add(ItemNameUtil.itemName(material, NamedTextColor.GRAY));
          }
        }
        if (tooMany)
        {
          lore.add(ComponentUtil.translate("&7&ocontainer.shulkerBox.more", size - 50));
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
      if (!NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.CAN_PLACE_ON))
      {
        Set<Namespaced> placeableKeys = itemMeta.getPlaceableKeys();
        lore.add(Component.empty());
        lore.add(ComponentUtil.translate("&6[%s]", ComponentUtil.translate("item.canPlace")));
        Set<Material> materials = new HashSet<>();
        for (Namespaced namespaced : placeableKeys)
        {
          if (namespaced.getNamespace().equals("minecraft"))
          {
            if (namespaced instanceof NamespacedKey namespacedKey)
            {
              try
              {
                materials.add(Material.valueOf(namespacedKey.getKey().toUpperCase()));
              }
              catch (Exception ignored)
              {

              }
            }
            if (namespaced instanceof NamespacedTag namespacedTag)
            {
              Iterable<Tag<Material>> tags = Bukkit.getTags(Tag.REGISTRY_BLOCKS, Material.class);
              tags.forEach(t ->
              {
                NamespacedKey namespacedKey = t.getKey();
                if (namespacedTag.getNamespace().equals(namespacedKey.getNamespace()) && namespacedTag.getKey().equals(namespacedKey.getKey()))
                {
                  materials.addAll(t.getValues());
                }
              });
            }
          }
          else
          {
            lore.add(ComponentUtil.translate("&7block." + namespaced.getNamespace() + "." + namespaced.getKey()));
          }
        }
        if (materials.size() > 15 && Cucumbery.config.getInt("max-item-lore-width") >= 20)
        {
          StringBuilder key = new StringBuilder("&7");
          List<Component> args = new ArrayList<>();
          for (Material material : materials)
          {
            key.append("%s, ");
            args.add(ItemNameUtil.itemName(material, NamedTextColor.GRAY));
          }
          key = new StringBuilder(key.substring(0, key.length() - 2));
          lore.add(ComponentUtil.translate(key.toString(), args));
        }
        else
        {
          for (Material material : materials)
          {
            lore.add(ItemNameUtil.itemName(material, NamedTextColor.GRAY));
          }
        }
      }
    }
    else
    {
      itemMeta.removeItemFlags(ItemFlag.HIDE_PLACED_ON);
    }
  }
}
