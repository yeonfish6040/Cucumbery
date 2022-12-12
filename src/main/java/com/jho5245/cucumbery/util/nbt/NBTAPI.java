package com.jho5245.cucumbery.util.nbt;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.storage.no_groups.CustomConfig;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import com.jho5245.cucumbery.util.storage.data.Constant;
import de.tr7zw.changeme.nbtapi.*;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class NBTAPI
{
  /**
   * 해당 json (예시) {"value":"NO_TRADE","HideFromLore":true} 에서 type에 대한 HideFromLore 값을 반환합니다.
   *
   * @param nbtCompound 확인할 json
   * @return type에 대한 HideFromLore의 값
   */
  public static boolean hideFromLore(ReadWriteNBT nbtCompound)
  {
    try
    {
      return nbtCompound.getBoolean(CucumberyTag.ITEM_USAGE_RESTRICTIONS_HIDE_FROM_LORE_KEY);
    }
    catch (Exception e)
    {
      return false;
    }
  }

  /**
   * 아이템에 사용 제한 태그가 있는지 확인합니다.
   *
   * @param item 확인할 아이템
   * @param type 사용 제한 태그
   * @return 사용 제한 태그를 가지고 있으면 true
   */
  public static boolean isRestricted(@Nullable ItemStack item, @NotNull Constant.RestrictionType type)
  {
    if (!ItemStackUtil.itemExists(item))
    {
      return false;
    }
    if (Cucumbery.getPlugin().getConfig().getBoolean("disable-item-usage-restriction"))
    {
      return false;
    }
    NBTItem nbtItem = new NBTItem(item);
    NBTCompoundList restrictionTags = getCompoundList(nbtItem.getCompound(CucumberyTag.KEY_MAIN), CucumberyTag.ITEM_USAGE_RESTRICTIONS_KEY);
    if (restrictionTags == null || restrictionTags.isEmpty())
    {
      return false;
    }
    for (ReadWriteNBT restrictionTag : restrictionTags)
    {
      String value = restrictionTag.getString(CucumberyTag.VALUE_KEY);
      if (type.toString().equals(value))
      {
        return true;
      }
    }
    return false;
  }

  /**
   * 해당 플레이어가 아이템 사용 제한 태그의 영향을 받는지 확인합니다.
   *
   * @param player 확인할 플레이어
   * @param item   확인할 아이템
   * @param type   사용 제한 태그
   * @return 영향을 받으면 true
   */
  public static boolean isRestricted(@NotNull Player player, @Nullable ItemStack item, @NotNull Constant.RestrictionType type)
  {
    if (!ItemStackUtil.itemExists(item))
    {
      return false;
    }
    boolean hasTag = false;
    boolean hasPermission = CustomConfig.UserData.EVENT_EXCEPTION_ACCESS.getBoolean(player.getUniqueId());
    NBTItem nbtItem = new NBTItem(item);
    NBTCompoundList restrictionTags = getCompoundList(nbtItem.getCompound(CucumberyTag.KEY_MAIN), CucumberyTag.ITEM_USAGE_RESTRICTIONS_KEY);
    if (restrictionTags != null)
    {
      for (ReadWriteNBT restrictionTag : restrictionTags)
      {
        String value = restrictionTag.getString(CucumberyTag.VALUE_KEY);
        if (type.toString().equals(value))
        {
          hasTag = true;
          try
          {
            String permission = getString(restrictionTag, CucumberyTag.PERMISSION_KEY);
            if (permission != null && player.hasPermission(permission))
            {
              hasPermission = true;
              break;
            }
          }
          catch (Exception ignored)
          {

          }
          break;
        }
      }
    }
    return !hasPermission && hasTag;
  }

  /**
   * 아이템의 사용 제한 우회 퍼미션 노드를 반환합니다.
   *
   * @param item 확인할 아이템
   * @param type 확인할 사용 제한 타입
   * @return 사용 제한 우회 퍼미션 노드, 없을 경우, null을 반환합니다.
   */
  @Nullable
  public static String getRestrictionOverridePermission(@Nullable ItemStack item, @NotNull Constant.RestrictionType type)
  {
    if (!ItemStackUtil.itemExists(item))
    {
      return null;
    }
    NBTItem nbtItem = new NBTItem(item);
    NBTCompoundList restrictionTags = getCompoundList(nbtItem.getCompound(CucumberyTag.KEY_MAIN), CucumberyTag.ITEM_USAGE_RESTRICTIONS_KEY);
    if (restrictionTags != null)
    {
      for (ReadWriteNBT restrictionTag : restrictionTags)
      {
        String value = restrictionTag.getString(CucumberyTag.VALUE_KEY);
        if (type.toString().equals(value))
        {
          try
          {
            String permission = getString(restrictionTag, CucumberyTag.PERMISSION_KEY);
            if (permission != null)
            {
              return permission;
            }
          }
          catch (Exception ignored)
          {

          }
          break;
        }
      }
    }
    return null;
  }

  /**
   * 아이템에 사용 제한 태그가 있는지 확인합니다.
   * <n>
   * 우회 권한이 부여되어 있다면 사용 제한 태그가 없는 걸로 간주되어 false를 반환합니다.
   * </n>
   *
   * @param item 확인할 아이템
   * @param type 사용 제한 태그
   * @return 사용 제한 태그를 가지고 있고, 우회 권한이 없을 경우에만 true, 이외의 경우 false
   */
  public static boolean isRestrictedFinal(@Nullable ItemStack item, @NotNull Constant.RestrictionType type)
  {
    return NBTAPI.isRestricted(item, type) && NBTAPI.getRestrictionOverridePermission(item, type) == null;
  }

  /**
   * NBTList(String) (문자열 배열)에 해당 문자열이 포함되어 있는지 확인합니다.
   *
   * @param array 확인할 문자열 배멸
   * @param value 포함되어 있는지 확린할 문자열
   * @return 배열에 해당 문자열이 포함되어 있으면 true 이외에는 false
   */
  public static boolean arrayContainsValue(@Nullable NBTList<String> array, @NotNull String value)
  {
    if (value.isEmpty())
    {
      return false;
    }
    if (array == null || array.isEmpty())
    {
      return false;
    }
    for (String loop : array)
    {
      if (loop.equals(value))
      {
        return true;
      }
    }
    return false;
  }

  /**
   * NBTList(String) (문자열 배열)에 해당 문자열이 포함되어 있는지 확인합니다.
   *
   * @param array 확인할 문자열 배멸
   * @param value 포함되어 있는지 확린할 문자열
   * @return 배열에 해당 문자열이 포함되어 있으면 true 이외에는 false
   */
  public static boolean arrayContainsValue(@Nullable NBTList<String> array, @NotNull Enum<?> value)
  {
    return NBTAPI.arrayContainsValue(array, value.toString());
  }

  /**
   * JSONObject 배열의 json 중에서 특정 키와 특정 값을 가진 키가 있는지 확인합니다.
   *
   * @param array 확인할 배열
   * @param key   포함되어 있는지 확인할 json의 키
   * @param value 포함되어 있는지 확인할 json의 값
   * @return 포함되어 있을 경우 true, 이외의 경우 false
   */
  public static boolean commpoundListContainsValue(@Nullable NBTCompoundList array, @NotNull String key, @NotNull String value)
  {
    if (array == null || array.isEmpty() || key.isEmpty() || value.isEmpty())
    {
      return false;
    }
    for (ReadWriteNBT nbtCompound : array)
    {
      try
      {
        if (nbtCompound.hasTag(key))
        {
          if (value.equals(nbtCompound.getString(key)))
          {
            return true;
          }
        }
      }
      catch (Exception e)
      {
        return false;
      }
    }
    return false;
  }

  public static void removeKey(NBTCompound compound, String key)
  {
    NBTCompound parent = compound;
    parent.removeKey(key);
    while (parent.getParent() != null && parent.getKeys().isEmpty())
    {
      key = parent.getName();
      parent = parent.getParent();
      parent.removeKey(key);
    }
  }

  @Nullable
  public static NBTCompound getMainCompound(@Nullable ItemStack item)
  {
    if (item == null || item.getType() == Material.AIR)
    {
      return null;
    }
    NBTItem nbtItem = new NBTItem(item);
    if (!nbtItem.hasTag(CucumberyTag.KEY_MAIN))
    {
      return null;
    }
    return nbtItem.getCompound(CucumberyTag.KEY_MAIN);
  }

  @Nullable
  public static NBTCompound getCompound(@Nullable NBTCompound parent, @NotNull String key)
  {
    if (parent == null || !parent.hasTag(key))
    {
      return null;
    }
    return parent.getCompound(key);
  }

  @Nullable
  public static NBTCompoundList getCompoundList(@Nullable NBTCompound parent, @NotNull String key)
  {
    if (parent == null)
    {
      return null;
    }
    return parent.getCompoundList(key);
  }

  @Nullable
  public static NBTList<String> getStringList(@Nullable NBTCompound parent, @NotNull String key)
  {
    if (parent == null)
    {
      return null;
    }
    return parent.getStringList(key);
  }

  @Nullable
  public static Integer getInteger(@Nullable NBTCompound parent, @NotNull String key)
  {
    if (parent == null || !parent.hasTag(key))
    {
      return null;
    }
    return parent.getInteger(key);
  }

  @Nullable
  public static Long getLong(@Nullable NBTCompound parent, @NotNull String key)
  {
    if (parent == null || !parent.hasTag(key))
    {
      return null;
    }
    return parent.getLong(key);
  }

  @Nullable
  public static Double getDouble(@Nullable NBTCompound parent, @NotNull String key)
  {
    if (parent == null || !parent.hasTag(key))
    {
      return null;
    }
    return parent.getDouble(key);
  }

  @Nullable
  public static String getString(@Nullable ReadWriteNBT parent, @NotNull String key)
  {
    if (parent == null || !parent.hasTag(key))
    {
      return null;
    }
    return parent.getString(key);
  }

  @Nullable
  public static Boolean getBoolean(@Nullable NBTCompound parent, @NotNull String key)
  {
    if (parent == null || !parent.hasTag(key))
    {
      return null;
    }
    return parent.getBoolean(key);
  }

  /**
   * 정수 배열을 UUID로 변환합니다.
   *
   * @param array UUID로 변환할 정수 배열
   * @return 배열의 길이가 4이고 올바른 배열일 경우 UUID, 그 외의 경우 null
   */
  @Nullable
  public static UUID getUUIDFromIntArray(int[] array)
  {
    if (array.length != 4)
    {
      return null;
    }
    NBTContainer nbtContainer = new NBTContainer("{}");
    nbtContainer.setIntArray("uuid", array);
    try
    {
      return nbtContainer.getUUID("uuid");
    }
    catch (Exception e)
    {
      return null;
    }
  }

  /**
   * UUID를 정수 배열로 반환합니다.
   *
   * @param uuid 정수 배열로 반환할 UUID
   * @return UUID로부터 변환된 길이가 4인 정수 배열
   */
  public static int[] getIntArrayFromUUID(@NotNull UUID uuid)
  {
    NBTContainer nbtContainer = new NBTContainer("{}");
    nbtContainer.setUUID("uuid", uuid);
    return nbtContainer.getIntArray("uuid");
  }
}
