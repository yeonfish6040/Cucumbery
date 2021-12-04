package com.jho5245.cucumbery.util.storage;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.storage.data.Constant.AllPlayer;
import com.jho5245.cucumbery.util.storage.data.Variable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
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
            System.out.println("Error1");
          }
        }
        boolean success2 = file.createNewFile();
        if (!success2)
        {
          System.out.println("Error1.1");
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
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
    catch (InvalidConfigurationException e)
    {
      if (!this.file.getName().endsWith(".log"))
      {
        e.printStackTrace();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
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
      e.printStackTrace();
    }
  }

  public void delete()
  {
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
      e.printStackTrace();
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
    UUID("UUID", "플레이어-UUID"),
    ID("아이디", "플레이어-아이디"),
    DISPLAY_NAME("닉네임", "플레이어-아이디"),
    PLAYER_LIST_NAME("목록-닉네임", "플레이어-아이디"),
    PLAY_JOIN("입장-소리-재생", true),
    PLAY_JOIN_FORCE("입장-소리-무조건-재생", false),
    PLAY_QUIT("퇴장-소리-재생", true),
    PLAY_QUIT_FORCE("퇴장-소리-무조건-재생", false),
    PLAY_CHAT("채팅-소리-재생", true),
    PLAY_CHAT_FORCE("채팅-소리-무조건-재생", false),
    LISTEN_JOIN("입장-소리-들음", true),
    LISTEN_JOIN_FORCE("입장-소리-무조건-들음", false),
    LISTEN_QUIT("퇴장-소리-들음", true),
    LISTEN_QUIT_FORCE("퇴장-소리-무조건-들음", false),
    LISTEN_CHAT("채팅-소리-들음", true),
    LISTEN_CHAT_FORCE("채팅-소리-무조건-들음", false),
    LISTEN_COMMAND("명령어-입력-소리-들음", true),
    LISTEN_HELDITEM("손에-든-아이템-바꾸는-소리-들음", true),
    LISTEN_CONTAINER("컨테이너-열고-닫는-소리-들음", true),
    LISTEN_GLOBAL("서버-라디오-들음", true),
    LISTEN_GLOBAL_FORCE("서버-라디오-무조건-들음", false),
    LISTEN_ITEM_DROP("아이템-버리는-소리-들음", true),
    SHOW_JOIN_MESSAGE("입장-메시지-띄움", true),
    SHOW_JOIN_MESSAGE_FORCE("입장-메시지-무조건-띄움", false),
    SHOW_QUIT_MESSAGE("퇴장-메시지-띄움", true),
    SHOW_QUIT_MESSAGE_FORCE("퇴장-메시지-무조건-띄움", false),
    SHOW_ACTIONBAR_ON_ITEM_PICKUP("아이템-주울때-액션바-띄움", true),
    SHOW_ACTIONBAR_ON_ITEM_PICKUP_FORCE("아이템-주울때-액션바-무조건-띄움", false),
    SHOW_ACTIONBAR_ON_ITEM_DROP("아이템-버릴때-액션바-띄움", true),
    SHOW_ACTIONBAR_ON_ITEM_DROP_FORCE("아이템-버릴때-액션바-무조건-띄움", false),
    SHOW_ACTIONBAR_ON_ATTACK("공격할-때-액션바-띄움", true),
    SHOW_ACTIONBAR_ON_ATTACK_FORCE("공격할-때-액션바-무조건-띄움", false),
    SHOW_ITEM_BREAK_TITLE("아이템-파괴-타이틀-띄움", true),
    SHOW_JOIN_TITLE("입장-타이틀-띄움", true),
    SHOW_ACTIONBAR_ON_ATTACK_PVP("PVP할-때-액션바-띄움", true),
    SHOW_ACTIONBAR_ON_ATTACK_PVP_FORCE("PVP할-때-액션바-무조건-띄움", false),
    HIDE_ACTIONBAR_ON_ATTACK_PVP_TO_OTHERS("PVP-할-때-상대방에게-액션바-띄우지-않음", false),
    OUTPUT_JOIN_MESSAGE("입장-메시지-출력", true),
    OUTPUT_JOIN_MESSAGE_FORCE("입장-메시지-무조건-출력", false),
    OUTPUT_QUIT_MESSAGE("퇴장-메시지-출력", true),
    OUTPUT_QUIT_MESSAGE_FORCE("퇴장-메시지-무조건-출력", false),
    TRAMPLE_SOIL("경작지-파괴-금지", true),
    TRAMPLE_SOIL_FORCE("경작지-파괴-무조건-금지", false),
    TRAMPLE_SOIL_ALERT("경작지-파괴-금지-타이틀-띄움", true),
    TRAMPLE_SOIL_NO_ALERT_FORCE("경작지-파괴-금지-무조건-띄우지-않음", false),
    ENTITY_AGGRO("몬스터로부터-어그로가-끌림", true),
    FIREWORK_LAUNCH_ON_AIR("공중에서-폭죽-발사", false),
    ITEM_DROP_DELAY_ALERT("아이템-버리기-딜레이-금지-타이틀-띄움", false),
    EVENT_EXCEPTION_ACCESS("이벤트-예외-액세스", false),
    SERVER_RESOURCEPACK("서버-리소스팩-사용", false),
    USE_QUICK_COMMAND_BLOCK("빠른-명령-블록-사용", false),
    SHOW_PREVIEW_COMMAND_BLOCK_COMMAND("명령-블록-명령어-미리보기", false),
    ITEM_PICKUP_MODE("아이템-줍기-모드", "normal"),
    ITEM_DROP_MODE("아이템-버리기-모드", "normal"),
    HEALTH_BAR("HP바", 20D),
    ITEM_USE_DELAY("아이템-사용-딜레이", 2),
    ITEM_DROP_DELAY("아이템-버리기-딜레이", 2),
    COPY_NOTE_BLOCK_VALUE_WHEN_SNEAKING("웅크리기-상태에서만-소리-블록-값-복사", true),
    COPY_NOTE_BLOCK_PITCH("픽블록으로-소리-블록-음높이-복사", false),
    COPY_NOTE_BLOCK_INSTRUMENT("픽블록으로-소리-블록-악기-복사", false),
    INVERT_NOTE_BLOCK_PITCH_WHEN_SNEAKING_IN_CREATIVE_MODE("크리에이티브-모드에서-소리-블록-시프트-우클릭으로-음높이-낮춤", true),
    PLAY_NOTE_BLOCK_WHEN_SNEAKING_IN_CREATIVE_MODE("크리에이티브-모드에서-소리-블록-클릭으로-소리-재생", true),
    SHORTEND_DEBUG_MESSAGE("플러그인-대량-디버그-메시지-간소화", true),
    USE_HELPFUL_LORE_FEATURE("아이템-설명-기능-사용", true),
    SAVE_INVENTORY_UPON_DEATH("사망-시-인벤토리-보존", false),
    SAVE_EXPERIENCE_UPON_DEATH("사망-시-경험치-보존", false),
    SHOW_PLUGIN_DEV_DEBUG_MESSAGE("플러그인-개발-디버그-메시지-띄움", false),
    DISABLE_HELPFUL_FEATURE_WHEN_CREATIVE("크리에이티브-모드에서-아이템-설명-기능-비활성화", false),
    NOTIFY_IF_INVENTORY_IS_FULL("인벤토리-공간-없음-경고-메시지-띄움", true),
    NOTIFY_IF_INVENTORY_IS_FULL_FORCE_DISABLE("인벤토리-공간-없음-경고-메시지-강제-비활성화", false),
    SHOW_ACTIONBAR_WHEN_ITEM_IS_COOLDOWN("재사용-대기시간-액션바-띄움", true),
    FORCE_HIDE_ACTIONBAR_WHEN_ITEM_IS_COOLDOWN("재사용-대기시간-엑션바-무조건-숨김", false),
    SHOW_COMMAND_BLOCK_EXECUTION_LOCATION("명령-블록-실행-위치-출력", false),
    HEALTH_SCALED("HP바-최대-HP-비례", true),
    COPY_BLOCK_DATA("픽블록으로-블록-데이터-복사", false),
    COPY_BLOCK_DATA_WHEN_SNEAKING("웅크리기-상태에서만-픽블록으로-블록-데이터-복사", true),
    COPY_BLOCK_DATA_FACING("픽블록으로-블록-데이터-facing-복사", false),
    COPY_BLOCK_DATA_WATERLOGGED("픽블록으로-블록-데이터-waterlogged-복사", false),
    DISABLE_COMMAND_BLOCK_BREAK_WHEN_SNEAKING("웅크린-상태에서-명령-블록-파괴-방지", false),
    SHOW_SPECTATOR_TARGET_INFO_IN_ACTIONBAR("관전-중인-개체-정보-액션바에-표시", true),
    SHOW_SPECTATOR_TARGET_INFO_IN_ACTIONBAR_TMI_MODE("관전-중인-개체-정보-액션바에-표시(TMI 모드)", false),
    DISABLE_ITEM_COOLDOWN("아이템-재사용-대기-시간-무시(바닐라-아이템-전용)", false),
    GOD_MODE("무적-모드", false),
    IMMEDIATE_RESPAWN("즉시-리스폰", false),
    SPECTATOR_MODE("관전-모드", false),
    SPECTATOR_MODE_ON_JOIN("접속-시-자동-관전-모드-전환", false),
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

    public void set(UUID uuid, Object value)
    {
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
    }

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
