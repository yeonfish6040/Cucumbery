package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.storage.ItemCategory;
import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemLore4
{
  protected static void setItemLore(@NotNull ItemStack itemStack)
  {
    NBTItem nbtItem = new NBTItem(itemStack);
    ItemMeta itemMeta = itemStack.getItemMeta();
    if (itemMeta != null)
    {
      List<Component> lore = itemMeta.lore();
      if (lore != null)
      {
        nbtItem.setInteger("Rarity", ItemCategory.Rarity.getRarityFromValue(ItemLoreUtil.getItemRarityValue(lore)).getRarityNumber());
      }
    }
    NBTCompound nbtCompound = nbtItem.getCompound(CucumberyTag.KEY_TMI);
    if (nbtCompound == null)
    {
      nbtCompound = nbtItem.addCompound(CucumberyTag.KEY_TMI);
    }
    NBTList<String> vanillaTags = nbtCompound.getStringList(CucumberyTag.TMI_VANILLA_TAGS);
    Material type = itemStack.getType();
    if (type.isBlock())
    {
      if (!vanillaTags.contains("blocks"))
      {
        vanillaTags.add("blocks");
      }
    }
    if (ItemStackUtil.isEdible(type))
    {
      if (!vanillaTags.contains("foods"))
      {
        vanillaTags.add("foods");
      }
    }
    if (ItemStackUtil.getCompostChance(type) > 0)
    {
      if (!vanillaTags.contains("compostable"))
      {
        vanillaTags.add("compostable");
      }
    }
    if (type.isFuel())
    {
      if (!vanillaTags.contains("fuel"))
      {
        vanillaTags.add("fuel");
      }
    }
    if (type.getMaxDurability() > 0)
    {
      if (!vanillaTags.contains("durable"))
      {
        vanillaTags.add("durable");
      }
    }
    String typeString = type.toString();
    if (typeString.endsWith("_WOOL"))
    {
      if (!vanillaTags.contains("wools"))
      {
        vanillaTags.add("wools");
      }
    }
    if (typeString.contains("WOOD"))
    {
      if (!vanillaTags.contains("woods"))
      {
        vanillaTags.add("woods");
      }
    }
    if (typeString.contains("STONE"))
    {
      if (!vanillaTags.contains("stones"))
      {
        vanillaTags.add("stones");
      }
    }
    switch (type)
    {
      case WHITE_WOOL, BLACK_WOOL, BLUE_WOOL, BROWN_WOOL, CYAN_WOOL, GRAY_WOOL -> {
      }
    }
    itemStack.setItemMeta(nbtItem.getItem().getItemMeta());
  }
}
