package com.jho5245.cucumbery.util;

import com.jho5245.cucumbery.util.storage.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemSerializer
{
  @NotNull
  public static String serialize(@Nullable ItemStack itemStack)
  {
    if (!ItemStackUtil.itemExists(itemStack))
    {
      return "";
    }
    return NBTItem.convertItemtoNBT(itemStack).toString();
  }

  @NotNull
  public static ItemStack deserialize(@Nullable String itemStack)
  {
    try
    {
      if (itemStack == null || itemStack.equals(""))
      {
        return new ItemStack(Material.AIR);
      }
      return NBTItem.convertNBTtoItem(new NBTContainer(itemStack));
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return new ItemStack(Material.AIR);
    }
  }
}
