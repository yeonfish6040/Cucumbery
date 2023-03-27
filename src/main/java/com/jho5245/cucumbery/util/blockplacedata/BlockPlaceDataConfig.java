package com.jho5245.cucumbery.util.blockplacedata;

import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.google.common.collect.Lists;
import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.itemlore.ItemLore;
import com.jho5245.cucumbery.util.no_groups.*;
import com.jho5245.cucumbery.util.storage.no_groups.ItemStackUtil;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import de.tr7zw.changeme.nbtapi.NBTType;
import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import io.papermc.paper.event.packet.PlayerChunkUnloadEvent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;

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
    despawnItemDisplay(new ArrayList<>(Bukkit.getOnlinePlayers()), location);
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
    if (o != null)
    {
      spawnItemDisplay(location);
    }
  }

  public static final HashMap<String, Set<Integer>> ITEM_DISPLAY_MAP = new HashMap<>();

  public static void spawnItemDisplay(@NotNull Location location)
  {
    spawnItemDisplay(new ArrayList<>(Bukkit.getOnlinePlayers()), location);
  }

  public static void spawnItemDisplay(@NotNull Player player, @NotNull Location location)
  {
    spawnItemDisplay(Collections.singletonList(player), location);
  }

  private static final float[][] offsets = {
          {0f, 0f, 0f},
          {0.5f, 0f, 0.5f},
          {0.5f, 0f, -0.5f},
          {-0.5f, 0f, 0.5f},
          {-0.5f, 0f, -0.5f},
          {0.5f, -0.5f, 0.5f},
          {0.5f, -0.5f, -0.5f},
          {-0.5f, -0.5f, 0.5f},
          {-0.5f, -0.5f, -0.5f}
  };

  private static void spawnItemDisplay_(@NotNull Collection<Player> players, @NotNull Location location, @NotNull NBTItem nbtItem, @Nullable NBTList<String> urls, int modifier)
  {
    String item = nbtItem.hasTag("item") && nbtItem.getType("item") == NBTType.NBTTagString ? nbtItem.getString("item") : null;
    String url = urls == null || urls.size() != 8 ? (nbtItem.hasTag("url") && nbtItem.getType("url") == NBTType.NBTTagString ? nbtItem.getString("url") : null) : urls.get(modifier);
    if (item == null && url == null)
    {
      return;
    }
    NBTCompound transformation = nbtItem.getOrCreateCompound("transformation");

    NBTList<Float> scale = transformation.getFloatList("scale");
    float scaleX = scale != null && scale.size() == 3 ? scale.get(0) : 1f,
            scaleY = scale != null && scale.size() == 3 ? scale.get(1) : 1f,
            scaleZ = scale != null && scale.size() == 3 ? scale.get(2) : 1f;

    NBTList<Float> translation = transformation.getFloatList("translation");
    float translationX = translation != null && translation.size() == 3 ? translation.get(0) : 0f,
            translationY = translation != null && translation.size() == 3 ? translation.get(1) : 0f,
            translationZ = translation != null && translation.size() == 3 ? translation.get(2) : 0f;

    NBTList<Float> rotation = transformation.getFloatList("rotation");
    float rotationX = rotation != null && rotation.size() == 2 ? rotation.get(0) : 0f,
            rotationY = rotation != null && rotation.size() == 2 ? rotation.get(1) : 0f;

    NBTCompound brightness = nbtItem.getOrCreateCompound("brightness");
    int brightnessBlock = brightness.hasTag("block") && brightness.getType("block") == NBTType.NBTTagInt ? brightness.getInteger("block") : -1,
            brightnessSky = brightness.hasTag("sky") && brightness.getType("sky") == NBTType.NBTTagInt ? brightness.getInteger("sky") : -1;

    Boolean glowing = nbtItem.hasTag("Glowing") && nbtItem.getType("Glowing") == NBTType.NBTTagByte ? nbtItem.getBoolean("Glowing") : null;
    int glowColorOverride = nbtItem.hasTag("glow_color_override") && nbtItem.getType("glow_color_override") == NBTType.NBTTagInt ? nbtItem.getInteger("glow_color_override") : -1;

    ItemStack displayItemStack = item != null ? ItemStackUtil.createItemStack(Bukkit.getConsoleSender(), item, false) : new ItemStack(Material.PLAYER_HEAD);
    if (item == null)
    {
      SkullMeta skullMeta = (SkullMeta) displayItemStack.getItemMeta();
      displayItemStack.setItemMeta(ItemStackUtil.setTexture(skullMeta, url));
    }
    if (!ItemStackUtil.itemExists(displayItemStack))
    {
      MessageUtil.sendWarn(Bukkit.getConsoleSender(), "잘못된 아이템 데이터가 있습니다: " + item);
      return;
    }
    ItemLore.setItemLore(displayItemStack);
    Object minecraftItemStack = MinecraftReflection.getMinecraftItemStack(displayItemStack);
    int entityId = Method.random(1, Integer.MAX_VALUE);
    String key = location.getWorld().getName() + "_" + location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ();
    Set<Integer> integerSet = ITEM_DISPLAY_MAP.getOrDefault(key, new HashSet<>());
    integerSet.add(entityId);
    ITEM_DISPLAY_MAP.put(key, integerSet);
    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    float[] offset;
    if (modifier == -1)
    {
      offset = offsets[0];
    }
    else
    {
      offset = offsets[modifier + 1];
    }
    PacketContainer packet = protocolManager.createPacket(Server.SPAWN_ENTITY);
    packet.getIntegers().write(0, entityId);
    packet.getEntityTypeModifier().write(0, EntityType.ITEM_DISPLAY);
    // Set location
    packet.getDoubles().write(0, location.getX() + 0.50005);
    packet.getDoubles().write(1, location.getY() + 0.5001);
    packet.getDoubles().write(2, location.getZ() + 0.50005);
    // Set yaw/pitch
    packet.getBytes().write(0, (byte) (rotationY * 256f / 360f));
    packet.getBytes().write(1, (byte) (rotationX * 256f / 360f));
    // Set UUID
    packet.getUUIDs().write(0, UUID.randomUUID());

    PacketContainer edit = protocolManager.createPacket(Server.ENTITY_METADATA);
    StructureModifier<List<WrappedDataValue>> watchableAccessor = edit.getDataValueCollectionModifier();
    List<WrappedDataValue> values = Lists.newArrayList(
            new WrappedDataValue(10, Registry.get(Vector3f.class), new Vector3f(
                    translationX + offset[0] * scaleX * 0.5f,
                    translationY + offset[1] * scaleY + 0.50005f,
                    translationZ + offset[2] * scaleZ * 0.5f)),
            new WrappedDataValue(11, Registry.get(Vector3f.class), new Vector3f(
                    2.001f * scaleX * (modifier == -1 ? 1f : 0.5f),
                    2.001f * scaleY * (modifier == -1 ? 1f : 0.5f),
                    2.001f * scaleZ * (modifier == -1 ? 1f : 0.5f))),
            new WrappedDataValue(22, Registry.getItemStackSerializer(false), minecraftItemStack)
    );
    if (glowing != null)
    {
      values.add(new WrappedDataValue(0, Registry.get(Byte.class), (byte) 0x40));
    }
    if (brightnessBlock != -1 && brightnessSky != -1)
    {
      values.add(new WrappedDataValue(15, Registry.get(Integer.class), brightnessBlock << 4 | brightnessSky << 20));
    }
    if (glowColorOverride > -1)
    {
      values.add(new WrappedDataValue(21, Registry.get(Integer.class), glowColorOverride));
    }
    watchableAccessor.write(0, values);
    edit.getIntegers().write(0, entityId);

    for (Player player : players)
    {
      protocolManager.sendServerPacket(player, packet);
      protocolManager.sendServerPacket(player, edit);
    }
  }

  public static void spawnItemDisplay(@NotNull Collection<Player> players, @NotNull Location location)
  {
    if (!Cucumbery.using_ProtocolLib)
    {
      return;
    }
    players = new ArrayList<>(players);
    players.removeIf(player ->
    {
      double distance = Method2.distance(location, player.getLocation());
      return distance == -1 || distance > 1000d;
    });
    if (players.isEmpty())
    {
      return;
    }
    ItemStack itemStack = getItem(location);
    if (!ItemStackUtil.itemExists(itemStack))
    {
      return;
    }
    NBTItem nbtItem = new NBTItem(itemStack);
    NBTList<String> urls = nbtItem.getStringList("urls");
    if (urls.size() == 8)
    {
      for (int i = 0; i < 8; i++)
      {
        spawnItemDisplay_(players, location, nbtItem, urls, i);
      }
    }
    else
    {
      spawnItemDisplay_(players, location, nbtItem, null, -1);
    }
  }

  public static void despawnItemDisplay(@NotNull Location location)
  {
    despawnItemDisplay(new ArrayList<>(Bukkit.getOnlinePlayers()), location);
  }

  public static void despawnItemDisplay(@NotNull Player player, @NotNull Location location)
  {
    despawnItemDisplay(Collections.singletonList(player), location);
  }

  public static void despawnItemDisplay(@NotNull Collection<Player> players, @NotNull Location location)
  {
    if (!Cucumbery.using_ProtocolLib)
    {
      return;
    }
    players = new ArrayList<>(players);
    players.removeIf(player ->
    {
      double distance = Method2.distance(location, player.getLocation());
      return distance == -1 || distance > 1000d;
    });
    if (players.isEmpty())
    {
      return;
    }
    Set<Integer> integerSet = ITEM_DISPLAY_MAP.getOrDefault(location.getWorld().getName() + "_" + location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ(), new HashSet<>());
    for (int id : integerSet)
    {
      ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
      PacketContainer remove = protocolManager.createPacket(Server.ENTITY_DESTROY);
      remove.getIntLists().write(0, List.of(id));
      for (Player player : players)
      {
        protocolManager.sendServerPacket(player, remove);
      }
    }
  }

  public static void onPlayerChunkLoad(PlayerChunkLoadEvent event)
  {
    Player player = event.getPlayer();
    Chunk chunk = event.getChunk();
    BlockPlaceDataConfig blockPlaceDataConfig = BlockPlaceDataConfig.getInstance(chunk);
    YamlConfiguration cfg = blockPlaceDataConfig.getConfig();
    ConfigurationSection root = cfg.getRoot();
    if (root != null)
    {
      for (String key : root.getKeys(false))
      {
        Location location = ChunkConfig.stringToLocation(player.getWorld(), key);
        if (location != null)
        {
          BlockPlaceDataConfig.spawnItemDisplay(player, location);
        }
      }
    }
  }

  public static void onPlayerChunkUnload(PlayerChunkUnloadEvent event)
  {
    Player player = event.getPlayer();
    Chunk chunk = event.getChunk();
    BlockPlaceDataConfig blockPlaceDataConfig = BlockPlaceDataConfig.getInstance(chunk);
    YamlConfiguration cfg = blockPlaceDataConfig.getConfig();
    ConfigurationSection root = cfg.getRoot();
    if (root != null)
    {
      for (String key : root.getKeys(false))
      {
        Location location = ChunkConfig.stringToLocation(player.getWorld(), key);
        if (location != null)
        {
          BlockPlaceDataConfig.despawnItemDisplay(player, location);
        }
      }
    }
  }
}
