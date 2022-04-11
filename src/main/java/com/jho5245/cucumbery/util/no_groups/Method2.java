package com.jho5245.cucumbery.util.no_groups;

import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

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
    return containsIgnoreCase(fileName, "\\", "/", ":", "*", "?", "\"", "<", ">", "|", "&", "§") ||
            equalsIgnoreCase(fileName, "con", "prn", "aux", "clock$", "nul",
                    "com1", "com2", "com3", "com4", "com5", "com6", "com7", "com8", "com9",
                    "lpt1", "lpt2", "lpt3", "lpt4", "lpt5", "lpt6", "lpt7", "lpt8", "lpt9") ||
            startsWith(fileName, "con.", "prn.", "aux.", "clock$.", "nul.",
                    "com1.", "com2.", "com3.", "com4.", "com5.", "com6.", "com7.", "com8.", "com9.",
                    "lpt1.", "lpt2.", "lpt3.", "lpt4.", "lpt5.", "lpt6.", "lpt7.", "lpt8.", "lpt9.") ||
            fileName.length() > 255;
  }

  public static double distance(@NotNull Location from, @NotNull Location to)
  {
    return distance(from, to, false);
  }

  public static double distance(@NotNull Location from, @NotNull Location to, boolean ignoreWorld)
  {
    if (!ignoreWorld && !from.getWorld().getName().equals(to.getWorld().getName()))
    {
      return -1d;
    }
    double fromX = from.getX(), fromY = from.getY(), fromZ = from.getZ(), toX = to.getX(), toY = to.getY(), toZ = to.getZ();
    return Math.sqrt(Math.pow(fromX - toX, 2d) + Math.pow(fromY - toY, 2d) + Math.pow(fromZ - toZ, 2d));
  }

  @NotNull
  public static String getPercentageColor(double current, double maximum)
  {
    double ratio = current / maximum;

    int red = 255;
    int green = (int) (ratio * 2 * 255);
    if (ratio >= 0.5)
    {
      green = 255;
      red = (int) ((255d / 50) * (100 - (ratio * 100d - 50d)) - 255d);
    }
    if (green < 0)
    {
      green = 0;
    }
    if (green > 255)
    {
      green = 255;
    }
    if (red < 0)
    {
      red = 0;
    }
    if (red > 255)
    {
      red = 255;
    }

    return "rg" + red + "," + green + ";";
  }

  /**
   * 목록에서 시간에 따른 객채를 반환합니다.
   * @param list 반환할 객체가 있는 목록
   * @param period 시간 주기 (밀리초)
   * @param <E> 객체의 유형
   * @return 현재 시각에 따른 객체
   */
  @NotNull
  public static <E> E getAnimated(@NotNull List<E> list, long period)
  {
    long current = System.currentTimeMillis();
    int size = list.size();
    return list.get((int) (current / size / (period / 1000d) % 1000d * size / 1000d));
  }

  /**
   * 배열에서 시간에 따른 객채를 반환합니다.
   * @param array 반환할 객체가 있는 배열
   * @param period 시간 주기 (밀리초)
   * @param <E> 객체의 유형
   * @return 현재 시각에 따른 객체
   */
  @NotNull
  public static <E> E getAnimated(@NotNull E[] array, long period)
  {
    long current = System.currentTimeMillis();
    int size = array.length;
    return array[((int) (current / size / (period / 1000d) % 1000d * size / 1000d))];
  }

  @Nullable
  public static Entity getEntityAsync(@NotNull UUID uuid)
  {
    for (World world : Bukkit.getWorlds())
    {
      for (Chunk chunk : world.getLoadedChunks())
      {
        for (Entity entity : chunk.getEntities())
        {
          if (entity.getUniqueId().equals(uuid))
          {
            return entity;
          }
        }
      }
    }
    return null;
  }
}




















