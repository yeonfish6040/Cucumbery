package com.jho5245.cucumbery.util.blockplacedata;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.no_groups.ChunkConfig;
import com.jho5245.cucumbery.util.no_groups.ItemSerializer;
import com.jho5245.cucumbery.util.no_groups.PlaceHolderUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class BlockPlaceDataConfig extends ChunkConfig
{
  protected static final HashMap<String, BlockPlaceDataConfig> MAP = new HashMap<>();

  protected BlockPlaceDataConfig(@NotNull Chunk chunk, boolean createNew)
  {
    super(chunk, "block-place-data/" + chunk.getWorld().getName(), createNew);
    MAP.put(getKey(), this);
  }

  /**
   * 이거 5분마다 호출해야함 파일 IO임
   */
  public static void saveAll()
  {
    MAP.forEach((s, blockPlaceDataConfig) -> blockPlaceDataConfig.saveConfig());
    MAP.keySet().removeIf(s -> !MAP.get(s).getChunk().isLoaded());

  }

  public void save()
  {
    Bukkit.getScheduler().runTaskLaterAsynchronously(Cucumbery.getPlugin(), () ->
    {
      saveConfig();
      MAP.remove(getKey());
    }, 0L);
  }

  public static void save(@NotNull Chunk chunk)
  {
    if (!MAP.containsKey(getKey(chunk)))
    {
      return;
    }
    MAP.get(getKey(chunk)).save();
  }

  public static BlockPlaceDataConfig getInstance(@NotNull Chunk chunk)
  {
    return getInstance(chunk, false);
  }

  public static BlockPlaceDataConfig getInstance(@NotNull Chunk chunk, boolean createNew)
  {
    String key = getKey(chunk);
    if (!MAP.containsKey(key))
    {
      new BlockPlaceDataConfig(chunk, createNew);
    }
    return MAP.get(key);
  }

  public static void removeData(@NotNull Location location)
  {
    BlockPlaceDataConfig blockPlaceDataConfig = BlockPlaceDataConfig.getInstance(location.getChunk());
    if (blockPlaceDataConfig != null)
    {
      blockPlaceDataConfig.set(location, null);
    }
  }

  @Nullable
  public static ItemStack getItem(@NotNull Location location)
  {
    return getItem(location, null);
  }

  @Nullable
  public static ItemStack getItem(@NotNull Location location, @Nullable CommandSender sender)
  {
    BlockPlaceDataConfig blockPlaceDataConfig = BlockPlaceDataConfig.getInstance(location.getChunk());
    if (blockPlaceDataConfig != null)
    {
      return blockPlaceDataConfig.getItemStack(location, sender);
    }
    return null;
  }

  public static void setItem(@NotNull Location location, @NotNull ItemStack itemStack)
  {
    getInstance(location.getChunk(), true).set(location, itemStack);
  }

  /**
   * 해당 위치에 저장된 값의 문자열 형태를 반환
   *
   * @param location 참조할 위치
   * @return 저장된 값 혹은 없으면 <code>null</code>
   */
  @Nullable
  public String getRawData(@NotNull Location location)
  {
    return getConfig().getString(locationToString(location));
  }

  /**
   * 해당 위치에 저장된 값의 아이템 형태를 반환
   * <p>만약에 아이템이 없거나 아이템에 플레이스 홀더가 지정되어 있을 경우 공기가 반환될 수 있음
   * <p>따라서 항상 아이템을 반환함
   *
   * @param location 참조할 위치
   * @return 저장된 아이템
   */
  @Nullable
  public ItemStack getItemStack(@NotNull Location location)
  {
    return getItemStack(location, null);
  }

  /**
   * 해당 위치에 저장된 값의 아이템 형태를 반환
   * <p>만약에 아이템이 없거나 아이템에 플레이스 홀더가 지정되어 있을 경우 공기가 반환될 수 있음
   * <p>따라서 항상 아이템을 반환함
   *
   * @param location 참조할 위치
   * @param sender   플레이스 홀더를 분석할 대상({@link Player} 혹은 {@link Bukkit#getConsoleSender()} 사용 가능), null을 입력하여 분석하지 않음
   * @return 저장된 아이템 혹은 null
   */
  @Nullable
  public ItemStack getItemStack(@NotNull Location location, @Nullable CommandSender sender)
  {
    String data = getConfig().getString(locationToString(location));
    if (sender != null && data != null && data.length() - data.replace("%", "").length() >= 2)
    {
      data = PlaceHolderUtil.placeholder(sender, data, null);
    }
    ItemStack itemStack = ItemSerializer.deserialize(data);
    return itemStack.getType().isAir() ? null : itemStack;
  }

  /**
   * 해당 위치에 아이템을 저장함
   *
   * @param location 저장할 위치
   * @param o        문자열 혹은 아이템 또는 <code>null</code>을 입력하여 삭제
   */
  public void set(@NotNull Location location, @Nullable Object o)
  {
    String value = o instanceof ItemStack itemStack ? ItemSerializer.serialize(itemStack) : o == null ? null : o.toString();
    getConfig().set(locationToString(location), value);
  }
}
