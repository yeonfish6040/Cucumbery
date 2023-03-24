package com.jho5245.cucumbery.util.itemlore;

import com.jho5245.cucumbery.util.nbt.CucumberyTag;
import com.jho5245.cucumbery.util.nbt.NBTAPI;
import com.jho5245.cucumbery.util.storage.component.util.ComponentUtil;
import com.jho5245.cucumbery.util.storage.data.Constant.CucumberyHideFlag;
import com.jho5245.cucumbery.util.storage.data.Constant.RestrictionType;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTList;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemLore2Restriction
{
  protected static void setItemLore(@NotNull ItemStack item, @NotNull Material type, @NotNull List<Component> lore, @Nullable NBTList<String> hideFlags)
  {
    if (!NBTAPI.arrayContainsValue(hideFlags, CucumberyHideFlag.RESTRICTION.toString()))
    {
      NBTCompoundList restrictionTags = NBTAPI.getCompoundList(NBTAPI.getMainCompound(item), CucumberyTag.ITEM_USAGE_RESTRICTIONS_KEY);
      if (restrictionTags != null && !restrictionTags.isEmpty())
      {
        StringBuilder restrictionTagLore = new StringBuilder();
        for (ReadWriteNBT restrictionTag : restrictionTags)
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
  }
}
