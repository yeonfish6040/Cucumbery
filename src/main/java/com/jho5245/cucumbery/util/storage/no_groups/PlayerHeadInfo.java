package com.jho5245.cucumbery.util.storage.no_groups;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

public class PlayerHeadInfo
{
  public enum PlayerHeadInfoType
  {
    UUID,
    NAME,
    URL
  }

  @Nullable
  public static String getPlayerHeadInfo(@NotNull ItemStack item, @NotNull PlayerHeadInfoType type)
  {
    if (item.getType() != Material.PLAYER_HEAD)
    {
      return null;
    }
    if (type == PlayerHeadInfoType.UUID)
    {
      try
      {
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        return Objects.requireNonNull(skullMeta.getOwningPlayer()).getUniqueId().toString();
      }
      catch (Exception e)
      {
        return null;
      }
    }
    NBTItem nbtItem = new NBTItem(item);
    NBTCompound skullOwner = nbtItem.getCompound("SkullOwner");
    if (skullOwner == null)
    {
      return null;
    }
    if (type == PlayerHeadInfoType.NAME)
    {
      String name = skullOwner.getString("Name");
      if (name != null && !name.equals(""))
      {
        return name;
      }
    }
    NBTCompound properties = skullOwner.getCompound("Properties");
    if (properties == null)
    {
      return null;
    }
    NBTCompoundList textures = properties.getCompoundList("textures");
    if (textures == null || textures.isEmpty())
    {
      return null;
    }
    for (ReadWriteNBT texture : textures)
    {
      if (!texture.hasTag("Value"))
      {
        continue;
      }
      String value = texture.getString("Value");
      try
      {
        String url = new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(url);
        String name = (String) jsonObject.get("profileName");
        if (type == PlayerHeadInfoType.NAME)
        {
          return name;
        }
        JSONObject jsonTextures = (JSONObject) jsonObject.get("textures");
        JSONObject jsonSKIN = (JSONObject) jsonTextures.get("SKIN");
        String jsonURL = (String) jsonSKIN.get("url");
        if (type == PlayerHeadInfoType.URL)
        {
          return jsonURL;
        }
      }
      catch (Exception e)
      {
        return null;
      }
    }
    return null;
  }
}
