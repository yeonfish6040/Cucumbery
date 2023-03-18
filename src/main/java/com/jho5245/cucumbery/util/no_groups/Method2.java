package com.jho5245.cucumbery.util.no_groups;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class Method2 extends Method
{
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

  @Nullable
  public static Entity getEntityById(int id)
  {
    for (World world : Bukkit.getWorlds())
    {
      for (Chunk chunk : world.getLoadedChunks())
      {
        for (Entity entity : chunk.getEntities())
        {
          if (entity.getEntityId() == id)
          {
            return entity;
          }
        }
      }
    }
    return null;
  }

  /**
   * {@link Enum}을 문자열 형태로 가져옵니다
   * @param id 가져올 문자열 (자동으로 대문자로 처리해줌)
   * @param clazz 객체 유형 클래스
   * @return 상수 또는 null
   * @param <T> 객체의 유형
   */
  @Nullable
  public static <T extends Enum<T>> T valueOf(@NotNull String id, Class<T> clazz)
  {
    try
    {
      return Enum.valueOf(clazz, id.toUpperCase());
    }
    catch (Exception ignored)
    {
      return null;
    }
  }

  @NotNull
  public static List<Entity> getNearbyEntitiesAsync(@NotNull Entity entity, double distance)
  {
    return getNearbyEntitiesAsync(entity.getLocation(), distance, null);
  }

  @NotNull
  public static List<Entity> getNearbyEntitiesAsync(@NotNull Location location, double distance)
  {
    return getNearbyEntitiesAsync(location, distance, null);
  }

  @NotNull
  public static List<Entity> getNearbyEntitiesAsync(@NotNull Location location, double distance, @Nullable Predicate<Entity> exclude)
  {
    List<Entity> entities = new ArrayList<>();
    World world = location.getWorld();
    for (Chunk chunk : world.getLoadedChunks())
    {
      for (Entity entity : chunk.getEntities())
      {
        if (location.distance(entity.getLocation()) <= distance && (exclude == null || !exclude.test(entity)))
        {
          entities.add(entity);
        }
      }
    }
    return entities;
  }
}




















