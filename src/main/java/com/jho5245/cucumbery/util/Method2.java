package com.jho5245.cucumbery.util;

import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Method2 extends Method
{
  /**
   * 해당 좌표에 설치되어 있는 커스텀 블록 데이터의 아이템 형태를 가져옵니다. 만약 아이템이 인벤토리에 소지할 수 없는 블록이거나 해당 좌표에 저장된 블록이 없을 경우, null을 반환합니다.
   *
   * @param worldName 월드 이름
   * @param x         x좌표
   * @param y         y좌표
   * @param z         z좌표
   * @return 해당 위치에 있는 아이템 혹은 null
   */
  @Nullable
  public static ItemStack getPlacedBlockDataAsItemStack(@NotNull String worldName, int x, int y, int z)
  {
    YamlConfiguration yamlConfiguration = Variable.blockPlaceData.get(worldName);
    if (yamlConfiguration == null)
    {
      return null;
    }
    String itemString = yamlConfiguration.getString(x + "_" + y + "_" + z);
    if (itemString == null)
    {
      return null;
    }
    ItemStack item = ItemSerializer.deserialize(itemString);
    Material type = item.getType();
    if (type == Material.AIR || !type.isItem())
    {
      return null;
    }
    return item;
  }

  /**
   * 해당 좌표에 설치되어 있는 커스텀 블록 데이터의 아이템 형태를 가져옵니다. 만약 아이템이 인벤토리에 소지할 수 없는 블록이거나 해당 좌표에 저장된 블록이 없을 경우, null을 반환합니다.
   *
   * @param location 설치되어 있는 블록의 위치
   * @return 해당 위치에 있는 아이템 혹은 null
   */
  @Nullable
  public static ItemStack getPlacedBlockDataAsItemStack(@Nullable Location location)
  {
    if (location == null)
    {
      return null;
    }
    World world = location.getWorld();
    if (world == null)
    {
      return null;
    }
    String worldName = world.getName();
    return Method2.getPlacedBlockDataAsItemStack(worldName, location.getBlockX(), location.getBlockY(), location.getBlockZ());
  }

  public static boolean isInvalidFileName(@NotNull String fileName)
  {
    return containsIgnoreCase(fileName, "\\", "/", ":", "*", "?", "\"", "<", ">", "|") ||
            equalsIgnoreCase(fileName, "con", "prn", "aux", "clock$", "nul",
                    "com1", "com2", "com3", "com4", "com5", "com6", "com7", "com8", "com9",
                    "lpt1", "lpt2", "lpt3", "lpt4", "lpt5", "lpt6", "lpt7", "lpt8", "lpt9") ||
            startsWith(fileName, "con.", "prn.", "aux.", "clock$.", "nul.",
                    "com1.", "com2.", "com3.", "com4.", "com5.", "com6.", "com7.", "com8.", "com9.",
                    "lpt1.", "lpt2.", "lpt3.", "lpt4.", "lpt5.", "lpt6.", "lpt7.", "lpt8.", "lpt9.") ||
            fileName.length() > 255;
  }
}
