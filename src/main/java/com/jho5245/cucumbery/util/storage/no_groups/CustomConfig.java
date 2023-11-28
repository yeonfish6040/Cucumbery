package com.jho5245.cucumbery.util.storage.no_groups;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.commands.sound.CommandSong;
import com.jho5245.cucumbery.util.no_groups.Method;
import com.jho5245.cucumbery.util.no_groups.Method2;
import com.jho5245.cucumbery.util.storage.data.Constant.AllPlayer;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("unused")
public class CustomConfig
{
  private final File file;
  private YamlConfiguration config;

  private CustomConfig(File file)
  {
    this.file = file;
    if (!file.exists())
    {
      try
      {
        if (!file.getParentFile().exists())
        {
          boolean success = file.getParentFile().mkdirs();
          if (!success)
          {
          }
        }
        boolean success2 = file.createNewFile();
        if (!success2)
        {
        }
      }
      catch (Exception e)
      {
Cucumbery.getPlugin().getLogger().warning(        e.getMessage());
      }
    }
    this.load();
  }

  private CustomConfig(String path)
  {
    this(new File(path));
  }

  public static CustomConfig getPlayerConfig(UUID uuid)
  {
    OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(uuid);
    CustomConfig customConfig = new CustomConfig(Cucumbery.getPlugin().getDataFolder() + "/data/UserData/" + uuid + ".yml");
    YamlConfiguration config = customConfig.getConfig();
    List<String> dataKeys = UserData.getKeys();
    boolean needToUpdate = false;
    for (String key : config.getKeys(false))
    {
      if (Objects.equals(key, UserData.DISPLAY_NAME.getKey()) || Objects.equals(key, UserData.PLAYER_LIST_NAME.getKey()))
      {
        continue;
      }
      if (!dataKeys.contains(key))
      {
        config.set(key, null);
        needToUpdate = true;
      }
    }
    for (UserData key : UserData.values())
    {
      if (key == UserData.DISPLAY_NAME || key == UserData.PLAYER_LIST_NAME)
      {
        continue;
      }
      if (!config.contains(key.getKey()))
      {
        config.set(key.getKey(), key.getDefault());
        needToUpdate = true;
      }
    }
    String configUUID = config.getString(UserData.UUID.getKey());
    if (configUUID == null || configUUID.equals("플레이어-UUID"))
    {
      config.set(UserData.UUID.getKey(), uuid.toString());
      needToUpdate = true;
    }
    String configId = config.getString(UserData.ID.getKey());
    if (configId == null || !configId.equals(player.getName()))
    {
      config.set(UserData.ID.getKey(), player.getName());
      needToUpdate = true;
    }
    String displayName = config.getString(UserData.DISPLAY_NAME.getKey());
    if (displayName != null && displayName.equals("플레이어-아이디"))
    {
      config.set(UserData.DISPLAY_NAME.getKey(), null);
      needToUpdate = true;
    }
    String playerListName = config.getString(UserData.PLAYER_LIST_NAME.getKey());
    if (playerListName != null && playerListName.equals("플레이어-아이디"))
    {
      config.set(UserData.PLAYER_LIST_NAME.getKey(), null);
      needToUpdate = true;
    }
    if (needToUpdate)
    {
      customConfig.saveConfig();
    }
    return customConfig;
  }

  public static CustomConfig getPlayerConfig(OfflinePlayer player)
  {
    return CustomConfig.getPlayerConfig(player.getUniqueId());
  }

  public static CustomConfig getCustomConfig(String path)
  {
    return new CustomConfig(Cucumbery.getPlugin().getDataFolder() + "/" + path);
  }

  public static CustomConfig getCustomConfig(File file)
  {
    return new CustomConfig(file);
  }

  public static CustomConfig getCustomConfig(CustomConfigType configType)
  {
    CustomConfig customConfig;
    YamlConfiguration config;
    boolean hasLeak = false;
    if (configType == CustomConfigType.ALLPLAYER)
    {
      customConfig = new CustomConfig(Cucumbery.getPlugin().getDataFolder() + "/data/AllPlayer.yml");
      config = customConfig.getConfig();
      if (!config.contains(AllPlayer.CHAT.getKey()))
      {
        hasLeak = true;
        config.set(AllPlayer.CHAT.getKey(), false);
      }
      if (!config.contains(AllPlayer.BLOCK_BREAK.getKey()))
      {
        hasLeak = true;
        config.set(AllPlayer.BLOCK_BREAK.getKey(), false);
      }
      if (!config.contains(AllPlayer.BLOCK_PLACE.getKey()))
      {
        hasLeak = true;
        config.set(AllPlayer.BLOCK_PLACE.getKey(), false);
      }
      if (!config.contains(AllPlayer.ITEM_DROP.getKey()))
      {
        hasLeak = true;
        config.set(AllPlayer.ITEM_DROP.getKey(), false);
      }
      if (!config.contains(AllPlayer.ITEM_PICKUP.getKey()))
      {
        hasLeak = true;
        config.set(AllPlayer.ITEM_PICKUP.getKey(), false);
      }
      if (!config.contains(AllPlayer.ITEM_INTERACT.getKey()))
      {
        hasLeak = true;
        config.set(AllPlayer.ITEM_INTERACT.getKey(), false);
      }
      if (!config.contains(AllPlayer.ITEM_HELD.getKey()))
      {
        hasLeak = true;
        config.set(AllPlayer.ITEM_HELD.getKey(), false);
      }
      if (!config.contains(AllPlayer.ITEM_CONSUME.getKey()))
      {
        hasLeak = true;
        config.set(AllPlayer.ITEM_CONSUME.getKey(), false);
      }
      if (!config.contains(AllPlayer.OPEN_CONTAINER.getKey()))
      {
        hasLeak = true;
        config.set(AllPlayer.OPEN_CONTAINER.getKey(), false);
      }
      if (!config.contains(AllPlayer.MOVE.getKey()))
      {
        hasLeak = true;
        config.set(AllPlayer.MOVE.getKey(), false);
      }
    }
    else
    {
      return CustomConfig.getCustomConfig(CustomConfigType.ALLPLAYER);
    }
    if (hasLeak)
    {
      customConfig.saveConfig();
    }
    return customConfig;
  }

  private void load()
  {
    try
    {
      this.config = new YamlConfiguration();
      this.config.load(this.file);
    }
    catch (InvalidConfigurationException | FileNotFoundException e)
    {
      if (!this.file.getName().endsWith(".log"))
      {
Cucumbery.getPlugin().getLogger().warning(        e.getMessage());
      }
    }
    catch (Exception e)
    {
Cucumbery.getPlugin().getLogger().warning(      e.getMessage());
    }
  }

  public void saveConfig()
  {
    try
    {
      this.config.save(this.file);
    }
    catch (Exception e)
    {
Cucumbery.getPlugin().getLogger().warning(      e.getMessage());
    }
  }

  public void delete()
  {
    if (!this.file.exists())
    {
      return;
    }
    try
    {
      boolean success = this.file.delete();
      if (!success)
      {
        throw new Exception();
      }
    }
    catch (Exception e)
    {
Cucumbery.getPlugin().getLogger().warning(      e.getMessage());
    }
  }

  @NotNull
  public YamlConfiguration getConfig()
  {
    return config;
  }

  public File getFile()
  {
    return file;
  }


  public enum UserData
  {
    GSIT_EXEMPT("GSit-예외-모드", false),
    ANNOUNCE_ADVANCEMENTS("발전-과제-달성-메시지-띄움", true),
    COPY_BLOCK_DATA("픽블록으로-블록-데이터-복사", false),
    COPY_BLOCK_DATA_FACING("픽블록으로-블록-데이터-facing-복사", false),
    COPY_BLOCK_DATA_WATERLOGGED("픽블록으로-블록-데이터-waterlogged-복사", false),
    COPY_BLOCK_DATA_WHEN_SNEAKING("웅크리기-상태에서만-픽블록으로-블록-데이터-복사", true),
    COPY_NOTE_BLOCK_INSTRUMENT("픽블록으로-소리-블록-악기-복사", false),
    COPY_NOTE_BLOCK_PITCH("픽블록으로-소리-블록-음높이-복사", false),
    COPY_NOTE_BLOCK_VALUE_WHEN_SNEAKING("웅크리기-상태에서만-소리-블록-값-복사", true),
    CUSTOM_MINING_COOLDOWN_DISPLAY_BLOCK("커스텀-채광-쿨타임-표시-블록", Material.BEDROCK.toString()),
    DISABLE_COMMAND_BLOCK_BREAK_WHEN_SNEAKING("웅크린-상태에서-명령-블록-파괴-방지", false),
    DISABLE_HELPFUL_FEATURE_WHEN_CREATIVE("크리에이티브-모드에서-아이템-설명-기능-비활성화", false),
    DISABLE_ITEM_COOLDOWN("아이템-재사용-대기-시간-무시(바닐라-아이템-전용)", false),
    DISPLAY_NAME("닉네임", "플레이어-아이디"),
    ENTITY_AGGRO("몬스터로부터-어그로가-끌림", true),
    ENTITY_HOVER_EVENT_DISPLAY_NAME("개체-호버-이벤트-TMI-모드", false),
    ENTITY_HOVER_EVENT_TMI_MODE("개체-호버-이벤트-TMI-모드", false),
    EVENT_EXCEPTION_ACCESS("이벤트-예외-액세스", false),
    FIREWORK_LAUNCH_ON_AIR("공중에서-폭죽-발사", false),
    FORCE_HIDE_ACTIONBAR_WHEN_ITEM_IS_COOLDOWN("재사용-대기시간-엑션바-무조건-숨김", false),
    FORCE_HIDE_DROPPED_ITEM_CUSTOM_NAME("바닥에-떨어진-아이템-이름-강제-숨김", false),
    FORCE_PLAY_SERVER_RADIO("서버-라디오-강제-재생", false),
    GOD_MODE("무적-모드", false),
    HEALTH_BAR("HP바", 20D),
    HEALTH_SCALED("HP바-최대-HP-비례", true),
    HIDE_ACTIONBAR_ON_ATTACK_PVP_TO_OTHERS("PVP-할-때-상대방에게-액션바-띄우지-않음", false),
    ID("아이디", "플레이어-아이디"),
    IMMEDIATE_RESPAWN("즉시-리스폰", false),
    INVERT_NOTE_BLOCK_PITCH_WHEN_SNEAKING_IN_CREATIVE_MODE("크리에이티브-모드에서-소리-블록-시프트-우클릭으로-음높이-낮춤", true),
    INVINCIBLE_TIME("무적-시간", -1),
    INVINCIBLE_TIME_JOIN("접속-무적-시간", -1),
    ITEM_DROP_DELAY("아이템-버리기-딜레이", 2),
    ITEM_DROP_DELAY_ALERT("아이템-버리기-딜레이-금지-타이틀-띄움", false),
    ITEM_DROP_MODE("아이템-버리기-모드", "normal"),
    ITEM_PICKUP_MODE("아이템-줍기-모드", "normal"),

    ITEM_USE_DELAY("아이템-사용-딜레이", 2),
    LISTEN_CHAT("채팅-소리-들음", true),
    LISTEN_CHAT_FORCE("채팅-소리-무조건-들음", false),
    LISTEN_COMMAND("명령어-입력-소리-들음", true),
    LISTEN_CONTAINER("컨테이너-열고-닫는-소리-들음", true),
    LISTEN_GLOBAL("서버-라디오-들음", true),
    LISTEN_GLOBAL_FORCE("서버-라디오-무조건-들음", false),
    LISTEN_HELDITEM("손에-든-아이템-바꾸는-소리-들음", true),
    LISTEN_ITEM_DROP("아이템-버리는-소리-들음", true),
    LISTEN_JOIN("입장-소리-들음", true),
    LISTEN_JOIN_FORCE("입장-소리-무조건-들음", false),
    LISTEN_QUIT("퇴장-소리-들음", true),
    LISTEN_QUIT_FORCE("퇴장-소리-무조건-들음", false),
    NEWBIE_BUFF_ENABLED("뉴비-환영-효과-사용", true),
    NOTIFY_IF_INVENTORY_IS_FULL("인벤토리-공간-없음-경고-메시지-띄움", true),
    NOTIFY_IF_INVENTORY_IS_FULL_FORCE_DISABLE("인벤토리-공간-없음-경고-메시지-강제-비활성화", false),
    OUTPUT_JOIN_MESSAGE("입장-메시지-출력", true),
    OUTPUT_JOIN_MESSAGE_FORCE("입장-메시지-무조건-출력", false),
    OUTPUT_QUIT_MESSAGE("퇴장-메시지-출력", true),
    OUTPUT_QUIT_MESSAGE_FORCE("퇴장-메시지-무조건-출력", false),
    PLAYER_LIST_NAME("목록-닉네임", "플레이어-아이디"),
    PLAY_CHAT("채팅-소리-재생", true),
    PLAY_CHAT_FORCE("채팅-소리-무조건-재생", false),
    PLAY_JOIN("입장-소리-재생", true),
    PLAY_JOIN_FORCE("입장-소리-무조건-재생", false),
    PLAY_NOTE_BLOCK_WHEN_SNEAKING_IN_CREATIVE_MODE("크리에이티브-모드에서-소리-블록-클릭으로-소리-재생", true),
    PLAY_QUIT("퇴장-소리-재생", true),
    PLAY_QUIT_FORCE("퇴장-소리-무조건-재생", false),
    SAVE_EXPERIENCE_UPON_DEATH("사망-시-경험치-보존", false),
    SAVE_INVENTORY_UPON_DEATH("사망-시-인벤토리-보존", false),
    SERVER_RESOURCEPACK("서버-리소스팩-사용", false),
    SHORTEND_DEBUG_MESSAGE("플러그인-대량-디버그-메시지-간소화", true),
    SHOW_ACTIONBAR_ON_ATTACK("공격할-때-액션바-띄움", true),
    SHOW_ACTIONBAR_ON_ATTACK_FORCE("공격할-때-액션바-무조건-띄움", false),
    SHOW_ACTIONBAR_ON_ATTACK_PVP("PVP할-때-액션바-띄움", true),
    SHOW_ACTIONBAR_ON_ATTACK_PVP_FORCE("PVP할-때-액션바-무조건-띄움", false),
    SHOW_ACTIONBAR_ON_ITEM_DROP("아이템-버릴때-액션바-띄움", true),
    SHOW_ACTIONBAR_ON_ITEM_DROP_FORCE("아이템-버릴때-액션바-무조건-띄움", false),
    SHOW_ACTIONBAR_ON_ITEM_PICKUP("아이템-주울때-액션바-띄움", true),
    SHOW_ACTIONBAR_ON_ITEM_PICKUP_FORCE("아이템-주울때-액션바-무조건-띄움", false),
    SHOW_ACTIONBAR_WHEN_ITEM_IS_COOLDOWN("재사용-대기시간-액션바-띄움", true),
    SHOW_BLOCK_BREAK_PARTICLE_ON_CUSTOM_MINING("커스텀-채광-모드-블록-파괴-입자-표시", true),
    SHOW_COMMAND_BLOCK_EXECUTION_LOCATION("명령-블록-실행-위치-출력", false),
    SHOW_DAMAGE_INDICATOR("대미지-숫자-표시", true),
    SHOW_DAMAGE_INDICATOR_SPECTATING_ENTITY("관전-중인-개체의-대미지-숫자-표시", true),
    SHOW_DROPPED_ITEM_CUSTOM_NAME("바닥에-떨어진-아이템-이름-표시", true),
    SHOW_ENCHANTMENT_TMI_DESCRIPTION("부여된-마법-TMI-표시", true),
    SHOW_GIVE_COMMAND_NBT_ON_ITEM_ON_CHAT("채팅창에-있는-아이템에-nbt-표시", false),
    SHOW_ITEM_BREAK_TITLE("아이템-파괴-타이틀-띄움", true),
    SHOW_JOIN_MESSAGE("입장-메시지-띄움", true),
    SHOW_JOIN_MESSAGE_FORCE("입장-메시지-무조건-띄움", false),
    SHOW_JOIN_TITLE("입장-타이틀-띄움", true),
    SHOW_PLUGIN_DEV_DEBUG_MESSAGE("플러그인-개발-디버그-메시지-띄움", false),
    SHOW_PREVIEW_COMMAND_BLOCK_COMMAND("명령-블록-명령어-미리보기", false),
    SHOW_QUIT_MESSAGE("퇴장-메시지-띄움", true),
    SHOW_QUIT_MESSAGE_FORCE("퇴장-메시지-무조건-띄움", false),
    SHOW_SPECTATOR_TARGET_INFO_IN_ACTIONBAR("관전-중인-개체-정보-액션바에-표시", true),
    SHOW_SPECTATOR_TARGET_INFO_IN_ACTIONBAR_TMI_MODE("관전-중인-개체-정보-액션바에-표시(TMI 모드)", false),
    SHOW_WORLDEDIT_POSITION_PARTICLE("월드에딧-포지션-입자-표시", false),

    SPECTATOR_MODE("관전-모드", false),
    SPECTATOR_MODE_ON_JOIN("접속-시-자동-관전-모드-전환", false),
    TRAMPLE_SOIL("경작지-파괴-금지", true),
    TRAMPLE_SOIL_ALERT("경작지-파괴-금지-타이틀-띄움", true),
    TRAMPLE_SOIL_FORCE("경작지-파괴-무조건-금지", false),
    TRAMPLE_SOIL_NO_ALERT_FORCE("경작지-파괴-금지-무조건-띄우지-않음", false),
    USE_QUICK_COMMAND_BLOCK("빠른-명령-블록-사용", false),
    UUID("UUID", "플레이어-UUID"),
    SHOW_DEATH_MESSAGE("데스-메시지-표시", true),

    SHOW_DEATH_SELF_MESSAGE("자신의-데스-메시지-표시", true),
    SHOW_DEATH_PVP_MESSAGE("데스-메시지-PVP-표시", true),

    SHOW_DEATH_LOCATION_ON_CHAT("사망-시-죽은-위치-채팅창에-표시", false),
    ;

    private final String key;

    private final Object defaultValue;

    UserData(String key, Object defaultValue)
    {
      this.key = key;
      this.defaultValue = defaultValue;
    }

    public static List<String> getKeys()
    {
      List<String> keys = new ArrayList<>();
      for (UserData key : UserData.values())
      {
        keys.add(key.getKey());
      }
      return keys;
    }

    public String getKey()
    {
      return key;
    }

    @NotNull
    public static UserData getByKey(@NotNull String s)
    {
      for (UserData userData : UserData.values())
      {
        if (userData.getKey().equals(s))
        {
          return userData;
        }
      }
      throw new IllegalArgumentException("no user data key: " + s);
    }

    public Object getDefault()
    {
      return defaultValue;
    }

    public Object get(UUID uuid)
    {
      YamlConfiguration config = Variable.userData.get(uuid);
      if (config == null)
      {
        config = CustomConfig.getPlayerConfig(uuid).getConfig();
      }
      return config.get(this.getKey());
    }

    public Object get(OfflinePlayer player)
    {
      return this.get(player.getUniqueId());
    }

    public boolean getBoolean(UUID uuid)
    {
      YamlConfiguration config = Variable.userData.get(uuid);
      if (config == null)
      {
        config = CustomConfig.getPlayerConfig(uuid).getConfig();
      }
      return config.getBoolean(this.getKey());
    }

    public boolean getBoolean(OfflinePlayer player)
    {
      return this.getBoolean(player.getUniqueId());
    }

    public String getString(UUID uuid)
    {
      YamlConfiguration config = Variable.userData.get(uuid);
      if (config == null)
      {
        config = CustomConfig.getPlayerConfig(uuid).getConfig();
      }
      return config.getString(this.getKey());
    }

    public String getString(OfflinePlayer player)
    {
      return this.getString(player.getUniqueId());
    }

    public int getInt(UUID uuid)
    {
      YamlConfiguration config = Variable.userData.get(uuid);
      if (config == null)
      {
        config = CustomConfig.getPlayerConfig(uuid).getConfig();
      }
      return config.getInt(this.getKey());
    }

    public int getInt(OfflinePlayer player)
    {
      return this.getInt(player.getUniqueId());
    }

    public long getLong(UUID uuid)
    {
      YamlConfiguration config = Variable.userData.get(uuid);
      if (config == null)
      {
        config = CustomConfig.getPlayerConfig(uuid).getConfig();
      }
      return config.getLong(this.getKey());
    }

    public long getLong(OfflinePlayer player)
    {
      return this.getLong(player.getUniqueId());
    }

    public double getDouble(UUID uuid)
    {
      YamlConfiguration config = Variable.userData.get(uuid);
      if (config == null)
      {
        config = CustomConfig.getPlayerConfig(uuid).getConfig();
      }
      return config.getDouble(this.getKey());
    }

    public double getDouble(OfflinePlayer player)
    {
      return this.getDouble(player.getUniqueId());
    }

    @Nullable
    public Material getMaterial(@NotNull UUID uuid)
    {
      YamlConfiguration config = Variable.userData.get(uuid);
      if (config == null)
      {
        config = CustomConfig.getPlayerConfig(uuid).getConfig();
      }
      return Method2.valueOf(config.getString(this.getKey(), "null"), Material.class);
    }

    @Nullable
    public Material getMaterial(@NotNull OfflinePlayer player)
    {
      return this.getMaterial(player.getUniqueId());
    }

    public void set(UUID uuid, Object value)
    {
      Player player = Method2.getEntityAsync(uuid) instanceof Player p ? p : null;
      YamlConfiguration config = Variable.userData.get(uuid);
      boolean offline = config == null;
      if (offline)
      {
        CustomConfig customConfig = CustomConfig.getPlayerConfig(uuid);
        config = customConfig.getConfig();
        config.set(this.getKey(), value);
        customConfig.saveConfig();
      }
      else
      {
        config.set(this.getKey(), value);
        Variable.userData.put(uuid, config);
      }
      if (player != null)
      {
        switch (this)
        {
          case DISABLE_HELPFUL_FEATURE_WHEN_CREATIVE, EVENT_EXCEPTION_ACCESS, SHOW_ENCHANTMENT_TMI_DESCRIPTION -> ItemStackUtil.updateInventory(player);
          case ENTITY_AGGRO, SPECTATOR_MODE ->
          {
            if (!UserData.ENTITY_AGGRO.getBoolean(uuid) || UserData.SPECTATOR_MODE.getBoolean(uuid))
            {
              if (UserData.SPECTATOR_MODE.getBoolean(uuid))
              {
                player.setGameMode(GameMode.SPECTATOR);
              }
              Bukkit.getScheduler().runTaskLaterAsynchronously(Cucumbery.getPlugin(), () ->
              {
                for (World world : Bukkit.getWorlds())
                {
                  for (Chunk chunk : world.getLoadedChunks())
                  {
                    for (Entity entity : chunk.getEntities())
                    {
                      if (entity instanceof Mob mob)
                      {
                        LivingEntity livingEntity = mob.getTarget();
                        if (player == livingEntity)
                        {
                          mob.setTarget(null);
                        }
                      }
                    }
                  }
                }
              }, 0L);
            }
          }
          case HEALTH_SCALED -> player.setHealthScaled(UserData.HEALTH_SCALED.getBoolean(player));
          case INVINCIBLE_TIME ->
          {
            int time = UserData.INVINCIBLE_TIME.getInt(uuid);
            if (time >= 0)
            {
              player.setMaximumNoDamageTicks(UserData.INVINCIBLE_TIME.getInt(uuid));
            }
            else
            {
              player.setMaximumNoDamageTicks(20);
            }
          }
          case LISTEN_GLOBAL ->
          {
            if (CommandSong.radioSongPlayer != null)
            {
              if (UserData.LISTEN_GLOBAL.getBoolean(uuid) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(uuid))
              {
                CommandSong.radioSongPlayer.addPlayer(player);
              }
              else
              {
                CommandSong.radioSongPlayer.removePlayer(player);
              }
            }
            if (CommandSong.playerRadio.containsKey(uuid))
            {
              if (UserData.LISTEN_GLOBAL.getBoolean(uuid) || UserData.LISTEN_GLOBAL_FORCE.getBoolean(uuid))
              {
                CommandSong.playerRadio.get(uuid).addPlayer(player);
              }
              else
              {
                CommandSong.playerRadio.get(uuid).removePlayer(player);
              }
            }
          }
          case SHOW_DROPPED_ITEM_CUSTOM_NAME, FORCE_HIDE_DROPPED_ITEM_CUSTOM_NAME -> Bukkit.getScheduler().runTaskLaterAsynchronously(Cucumbery.getPlugin(), () ->
          {
            if (!tasks.isEmpty())
            {
              tasks.forEach(BukkitTask::cancel);
              tasks.clear();
            }
            if (!items.isEmpty())
            {
              items.clear();
            }
            Location location = player.getLocation();
            for (Chunk chunk : location.getWorld().getLoadedChunks())
            {
              for (Entity entity : chunk.getEntities())
              {
                if (entity instanceof Item item && entity.getLocation().distance(location) <= 32d)
                {
                  items.add(item);
                }
              }
            }
            BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Cucumbery.getPlugin(), () -> {
              items.removeIf(item -> !item.isValid());
              if (!items.isEmpty())
              {
                Method.updateItem(items.get(0));
                items.remove(0);
              }
              if (items.isEmpty() && !tasks.isEmpty())
              {
                tasks.forEach(BukkitTask::cancel);
                tasks.clear();
              }
            }, 0L, 1L);
            tasks.add(bukkitTask);
          }, 0L);
        }
      }
    }
    private final List<BukkitTask> tasks = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();

    public void set(OfflinePlayer player, Object value)
    {
      this.set(player.getUniqueId(), value);
    }

    public void setToggle(UUID uuid)
    {
      this.set(uuid, !this.getBoolean(uuid));
    }

    public void setToggle(OfflinePlayer player)
    {
      this.setToggle(player.getUniqueId());
    }
  }

  public enum CustomConfigType
  {
    ALLPLAYER
  }
}
