package com.jho5245.cucumbery;

import com.jho5245.cucumbery.deathmessages.CustomDeathMessage;
import com.jho5245.cucumbery.util.ItemSerializer;
import com.jho5245.cucumbery.util.MessageUtil;
import com.jho5245.cucumbery.util.Method;
import com.jho5245.cucumbery.util.SelectorUtil;
import com.jho5245.cucumbery.util.storage.ComponentUtil;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import com.jho5245.cucumbery.util.storage.data.Variable;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.util.*;

import static com.jho5245.cucumbery.Cucumbery.getPlugin;

public class Initializer
{
  public static void registerCommand(String cmd, CommandExecutor executor)
  {
    PluginCommand pluginCommand = getPlugin().getCommand(cmd);
    if (pluginCommand != null)
    {
      pluginCommand.setExecutor(executor);
      pluginCommand.setTabCompleter(Cucumbery.getPlugin().getTabCompleter());
    }
  }

  public static void registerCommand(String cmd, CommandExecutor executor, org.bukkit.command.TabCompleter tabCompleter)
  {
    if (Cucumbery.getPlugin().getDescription().getCommands().containsKey(cmd))
    {
      Objects.requireNonNull(Cucumbery.getPlugin().getCommand(cmd)).setExecutor(executor);
      Objects.requireNonNull(Cucumbery.getPlugin().getCommand(cmd)).setTabCompleter(tabCompleter);
    }
  }

  public static void registerEvent(Listener listener)
  {
    getPlugin().getPluginManager().registerEvents(listener, getPlugin());
  }

  public static boolean setupEconomy()
  {
    if (getPlugin().getPluginManager().getPlugin("Vault") == null)
    {
      return false;
    }
    RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
    if (rsp == null)
    {
      return false;
    }
    Cucumbery.eco = rsp.getProvider();
    return true;
  }

  public static void loadDeathMessagesConfig()
  {
    File file = new File(Cucumbery.getPlugin().getDataFolder() + "/DeathMessages.yml");
    if (!file.exists())
    {
      Cucumbery.getPlugin().saveResource("DeathMessages.yml", false);
    }
    CustomConfig deathMessagesConfig = CustomConfig.getCustomConfig("DeathMessages.yml");
    Variable.deathMessages = deathMessagesConfig.getConfig();
    CustomDeathMessage.register();
  }

  public static void loadLang()
  {

    File file = new File(Cucumbery.getPlugin().getDataFolder() + "/lang.yml");
    if (!file.exists())
    {
      Cucumbery.getPlugin().saveResource("lang.yml", false);
    }
    CustomConfig deathMessagesConfig = CustomConfig.getCustomConfig("lang.yml");
    Variable.lang = deathMessagesConfig.getConfig();
  }

  public static void loadCustomConfigs()
  {
    loadDeathMessagesConfig();
    loadLang();
    Variable.customRecipes.clear();
    Variable.craftingTime.clear();
    Variable.craftsLog.clear();
    Variable.lastCraftsLog.clear();
    File recipesFolder = new File(getPlugin().getDataFolder() + "/data/CustomRecipe");
    if (recipesFolder.exists())
    {
      File[] recipeFiles = recipesFolder.listFiles();
      if (recipeFiles == null)
      {
        recipeFiles = new File[]{};
      }
      for (File file : recipeFiles)
      {
        String fileName3 = file.getName();
        boolean hasColorCode = !fileName3.equals(MessageUtil.stripColor(fileName3));
        if (fileName3.endsWith(".yml") && !fileName3.contains(" ") && !hasColorCode)
        {
          fileName3 = fileName3.substring(0, fileName3.length() - 4);
          Variable.customRecipes.put(fileName3, CustomConfig.getCustomConfig(file).getConfig());
        }
      }
      File craftingTimeFolder = new File(getPlugin().getDataFolder() + "/data/CustomRecipe/CraftingTime");
      if (craftingTimeFolder.exists())
      {
        File[] craftingTimeFiles = craftingTimeFolder.listFiles();
        if (craftingTimeFiles == null)
        {
          craftingTimeFiles = new File[]{};
        }
        for (File file : craftingTimeFiles)
        {
          String fileName4 = file.getName();
          if (fileName4.endsWith(".yml"))
          {
            fileName4 = fileName4.substring(0, fileName4.length() - 4);
            Variable.craftingTime.put(UUID.fromString(fileName4), CustomConfig.getCustomConfig(file).getConfig());
          }
        }
      }
      File craftsLogFolder = new File(getPlugin().getDataFolder() + "/data/CustomRecipe/CraftsLog");
      if (craftsLogFolder.exists())
      {
        File[] craftsLogFiles = craftsLogFolder.listFiles();
        if (craftsLogFiles == null)
        {
          craftsLogFiles = new File[]{};
        }
        for (File file : craftsLogFiles)
        {
          String fileName5 = file.getName();
          if (fileName5.endsWith(".yml"))
          {
            fileName5 = fileName5.substring(0, fileName5.length() - 4);
            Variable.craftsLog.put(UUID.fromString(fileName5), CustomConfig.getCustomConfig(file).getConfig());
          }
        }
      }
      File lastCraftsLogFolder = new File(getPlugin().getDataFolder() + "/data/CustomRecipe/LastCraftsLog");
      if (lastCraftsLogFolder.exists())
      {
        File[] lastCraftsLogFiles = lastCraftsLogFolder.listFiles();
        if (lastCraftsLogFiles == null)
        {
          lastCraftsLogFiles = new File[]{};
        }
        for (File file : lastCraftsLogFiles)
        {
          String fileName6 = file.getName();
          if (fileName6.endsWith(".yml"))
          {
            fileName6 = fileName6.substring(0, fileName6.length() - 4);
            Variable.lastCraftsLog.put(UUID.fromString(fileName6), CustomConfig.getCustomConfig(file).getConfig());
          }
        }
      }
    }
    Variable.commandPacks.clear();
    File commandPacksFolder = new File(getPlugin().getDataFolder() + "/data/CommandPacks");
    if (commandPacksFolder.exists())
    {
      File[] commandPackFiles = commandPacksFolder.listFiles();
      if (commandPackFiles == null)
      {
        commandPackFiles = new File[]{};
      }
      for (File file : commandPackFiles)
      {
        String fileName7 = file.getName();
        if (fileName7.endsWith(".yml"))
        {
          fileName7 = fileName7.substring(0, fileName7.length() - 4);
          Variable.commandPacks.put(fileName7, CustomConfig.getCustomConfig(file).getConfig());
        }
      }
    }
    Variable.itemStorage.clear();
    File itemStorageFolder = new File(getPlugin().getDataFolder() + "/data/ItemStorage");
    if (itemStorageFolder.exists())
    {
      File[] itemStorageFiles = itemStorageFolder.listFiles();
      if (itemStorageFiles == null)
      {
        itemStorageFiles = new File[]{};
      }
      for (File file : itemStorageFiles)
      {
        String fileName4 = file.getName();
        if (fileName4.endsWith(".yml"))
        {
          fileName4 = fileName4.substring(0, fileName4.length() - 4);
          Variable.itemStorage.put(fileName4, CustomConfig.getCustomConfig(file).getConfig());
        }
      }
    }
    Variable.customItems.clear();
    File customItemFolder = new File(getPlugin().getDataFolder() + "/data/CustomItem");
    if (customItemFolder.exists())
    {
      File[] customItemFiles = customItemFolder.listFiles();
      if (customItemFiles == null)
      {
        customItemFiles = new File[]{};
      }
      for (File file : customItemFiles)
      {
        String fileName4 = file.getName();
        if (fileName4.endsWith(".yml"))
        {
          fileName4 = fileName4.substring(0, fileName4.length() - 4);
          Variable.customItems.put(fileName4, CustomConfig.getCustomConfig(file).getConfig());
        }
      }
    }
    Variable.songFiles.clear();
    File songsFolder = new File(Cucumbery.getPlugin().getDataFolder() + "/data/songs");
    if (!songsFolder.exists())
    {
      if (!songsFolder.mkdirs())
      {
        return;
      }
    }
    else
    {
      File[] songFiles = songsFolder.listFiles();
      if (songFiles == null)
      {
        songFiles = new File[]{};
      }
      for (File file : songFiles)
      {
        String fileName5 = file.getName();
        if (fileName5.endsWith(".nbs"))
        {
          Variable.songFiles.add(fileName5);
        }
      }
    }

    Variable.allPlayerConfig = CustomConfig.getCustomConfig(CustomConfig.CustomConfigType.ALLPLAYER).getConfig();
    Variable.warps.clear();
    File warpsFolder = new File(getPlugin().getDataFolder() + "/data/Warps");
    if (warpsFolder.exists())
    {
      File[] warpFiles = warpsFolder.listFiles();
      if (warpFiles == null)
      {
        warpFiles = new File[]{};
      }
      for (File file : warpFiles)
      {
        String fileName2 = file.getName();
        if (fileName2.endsWith(".yml"))
        {
          fileName2 = fileName2.substring(0, fileName2.length() - 4);
          Variable.warps.put(fileName2, CustomConfig.getCustomConfig(file).getConfig());
        }
      }
    }

    loadNicknamesConfig();

    Variable.blockPlaceData.clear();
    File blockPlaceDataFolder = new File(getPlugin().getDataFolder() + "/data/BlockPlaceData");
    if (blockPlaceDataFolder.exists())
    {
      File[] dataFiles = blockPlaceDataFolder.listFiles();
      if (dataFiles != null)
      {
        for (File file : dataFiles)
        {
          String fileName = file.getName();
          if (fileName.endsWith(".yml"))
          {
            fileName = fileName.substring(0, fileName.length() - 4);
            Variable.blockPlaceData.put(fileName, CustomConfig.getCustomConfig(file).getConfig());
          }
        }
      }
    }
  }

  public static void loadBrigadierTabListConfig()
  {
    CustomConfig brigadierTabListCustomConfig = CustomConfig.getCustomConfig("data/brigadier_tab_list.yml");
    YamlConfiguration brigadierTabListConfig = brigadierTabListCustomConfig.getConfig();
    List<String> worlds = new ArrayList<>();
    for (World world : Bukkit.getWorlds())
    {
      worlds.add(world.getName());
    }
    brigadierTabListConfig.set("worldNames", worlds);
    List<String> commands = Method.getAllServerCommands();
    brigadierTabListConfig.set("commands", commands);

    brigadierTabListCustomConfig.saveConfig();
  }

  public static void loadNicknamesConfig()
  {
    Variable.nickNames.clear();
    File userDataFolder = new File(getPlugin().getDataFolder() + "/data/UserData");
    if (userDataFolder.exists())
    {
      File nickNamesFile = new File(getPlugin().getDataFolder() + "/data/Nicknames.yml");
      if (!nickNamesFile.exists())
      {
        boolean success = nickNamesFile.getParentFile().mkdirs();
        if (!success)
        {
          System.err.println("[Cucumbery] Could not create data folder!");
        }
      }
      CustomConfig nickNamesCustonConfig = CustomConfig.getCustomConfig(nickNamesFile);
      YamlConfiguration nickNamesConfig = nickNamesCustonConfig.getConfig();
      List<String> nickNames = new ArrayList<>();
      File[] userDataFiles = userDataFolder.listFiles();
      if (userDataFiles == null)
      {
        userDataFiles = new File[]{};
      }
      for (File userDataFile : userDataFiles)
      {
        String fileName = userDataFile.getName();
        if (fileName.endsWith(".yml"))
        {
          CustomConfig userDataCustomConfig = CustomConfig.getCustomConfig(userDataFile);
          YamlConfiguration userDataConfig = userDataCustomConfig.getConfig();
          String uuid = fileName.substring(0, fileName.length() - 4);
          String id = userDataConfig.getString(CustomConfig.UserData.ID.getKey());
          String display = userDataConfig.getString(CustomConfig.UserData.DISPLAY_NAME.getKey());
          String listName = userDataConfig.getString(CustomConfig.UserData.PLAYER_LIST_NAME.getKey());
          if (!nickNames.contains(uuid))
          {
            nickNames.add(uuid);
          }
          if (id != null && !nickNames.contains(id))
          {
            nickNames.add(id);
          }
          if (display != null)
          {
            display = MessageUtil.stripColor(ComponentUtil.serialize(ComponentUtil.create(display)));
            if (!nickNames.contains(display))
            {
              nickNames.add(MessageUtil.stripColor(display));
            }
          }
          if (listName != null)
          {
            listName = MessageUtil.stripColor(ComponentUtil.serialize(ComponentUtil.create(listName)));
            if (!nickNames.contains(listName))
            {
              nickNames.add(MessageUtil.stripColor(listName));
            }
          }
        }
      }
      nickNamesConfig.set("nicks", nickNames);
      nickNamesCustonConfig.saveConfig();
      Variable.nickNames.addAll(nickNames);
    }
    cacheUUID();
  }

  public static void cacheUUID()
  {
    for (String nickName : Variable.nickNames)
    {
      if (Method.isUUID(nickName))
      {
        continue;
      }
      if (nickName.startsWith("@"))
      {
        continue;
      }
      OfflinePlayer offlinePlayer = SelectorUtil.getOfflinePlayer(null, nickName, false);
      if (offlinePlayer == null)
      {
        continue;
      }
      Variable.cachedUUIDs.put(nickName, offlinePlayer.getUniqueId());
    }
  }

  public static void loadPlayerConfig(Player player)
  {
    loadUserDataConfig(player);
    loadCooldownConfig(player);
  }

  public static void loadPlayersConfig()
  {
    Variable.userData.clear();
    Variable.userDataUUIDs.clear();
    Variable.cooldownsItemUsage.clear();
    for (Player player : Bukkit.getServer().getOnlinePlayers())
    {
      loadPlayerConfig(player);
    }
  }

  public static void loadCooldownConfig(Player player)
  {
    UUID playerUUID = player.getUniqueId();
    File file = new File(getPlugin().getDataFolder() + "/data/ItemUsage/Cooldown/" + playerUUID + ".yml");
    if (file.exists())
    {
      CustomConfig cooldownCustomConfig = CustomConfig.getCustomConfig(file);
      Variable.cooldownsItemUsage.put(playerUUID, cooldownCustomConfig.getConfig());
    }
  }

  private static void loadUserDataConfig(Player player)
  {
    UUID playerUUID = player.getUniqueId();
    CustomConfig customConfig = CustomConfig.getPlayerConfig(player.getUniqueId());
    YamlConfiguration userCfg = customConfig.getConfig();
    Variable.userData.put(playerUUID, userCfg);
    Variable.userDataUUIDs.add(playerUUID.toString());
    Variable.nickNames.add(player.getName());
    Variable.cachedUUIDs.put(player.getName(), playerUUID);
    String displayname = CustomConfig.UserData.DISPLAY_NAME.getString(player.getUniqueId());
    String playerListName = CustomConfig.UserData.PLAYER_LIST_NAME.getString(player.getUniqueId());
    if (displayname != null)
    {
      displayname = MessageUtil.stripColor(displayname);
      Variable.nickNames.add(displayname);
      Variable.cachedUUIDs.put(displayname, playerUUID);
      Variable.cachedUUIDs.put(displayname.replace(" ", ""), playerUUID);
      Variable.cachedUUIDs.put(displayname.replace("+", ""), playerUUID);
      Variable.cachedUUIDs.put(displayname.replace("+", "").replace(" ", ""), playerUUID);
    }
    if (playerListName != null)
    {
      playerListName = MessageUtil.stripColor(playerListName);
      Variable.nickNames.add(playerListName);
      Variable.cachedUUIDs.put(playerListName, playerUUID);
      Variable.cachedUUIDs.put(playerListName.replace(" ", ""), playerUUID);
      Variable.cachedUUIDs.put(playerListName.replace("+", ""), playerUUID);
      Variable.cachedUUIDs.put(playerListName.replace("+", "").replace(" ", ""), playerUUID);
    }
  }

  public static void saveUserData()
  {
    HashMap<UUID, YamlConfiguration> removal = new HashMap<>();
    for (UUID uuid : Variable.userData.keySet())
    {
      YamlConfiguration cacheConfig = Variable.userData.get(uuid);
      ConfigurationSection cacheSection = cacheConfig.getConfigurationSection("");
      if (cacheSection != null)
      {
        CustomConfig customConfig = CustomConfig.getPlayerConfig(uuid);
        YamlConfiguration config = customConfig.getConfig();
        ConfigurationSection section = config.getConfigurationSection("");
        if (section != null)
        {
          for (String key : section.getKeys(true))
          {
            config.set(key, null);
          }
        }
        for (String key : cacheSection.getKeys(true))
        {
          config.set(key, cacheSection.get(key));
        }
        customConfig.saveConfig();
      }
      if (Method.getPlayer(null, uuid.toString(), false) == null)
      {
        removal.put(uuid, Variable.userData.get(uuid));
      }
    }
    for (UUID uuid : removal.keySet())
    {
      Variable.userData.remove(uuid);
    }
  }

  public static void saveBlockPlaceData()
  {
    for (String worldName : Variable.blockPlaceData.keySet())
    {
      YamlConfiguration cacheConfig = Variable.blockPlaceData.get(worldName);
      ConfigurationSection cacheSection = cacheConfig.getConfigurationSection("");
      if (cacheSection != null)
      {
        CustomConfig customConfig = CustomConfig.getCustomConfig("data/BlockPlaceData/" + worldName + ".yml");
        YamlConfiguration config = customConfig.getConfig();
        ConfigurationSection section = config.getConfigurationSection("");
        if (section != null)
        {
          for (String key : section.getKeys(true))
          {
            config.set(key, null);
          }
        }
        for (String key : cacheSection.getKeys(true))
        {
          try
          {
            String[] split = key.split("_");
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);
            int z = Integer.parseInt(split[2]);
            ItemStack item = ItemSerializer.deserialize(cacheConfig.getString(key));
            Location location = new Location(Bukkit.getWorld(worldName), x, y, z);
            if (location.getBlock().getType() == item.getType())
            {
              config.set(key, cacheSection.get(key));
            }
          }
          catch (Exception e)
          {
            e.printStackTrace();
          }
        }
        customConfig.saveConfig();
      }
    }
  }

  public static void saveItemUsageData()
  {
    HashMap<UUID, YamlConfiguration> removal = new HashMap<>();
    for (UUID uuid : Variable.cooldownsItemUsage.keySet())
    {
      YamlConfiguration cacheConfig = Variable.cooldownsItemUsage.get(uuid);
      ConfigurationSection cacheSection = cacheConfig.getConfigurationSection("");
      if (cacheSection != null)
      {
        CustomConfig customConfig = CustomConfig.getCustomConfig("data/ItemUsage/Cooldown/" + uuid.toString() + ".yml");
        YamlConfiguration config = customConfig.getConfig();
        for (String key : cacheSection.getKeys(true))
        {
          config.set(key, cacheSection.get(key));
        }
        customConfig.saveConfig();
      }
      if (Method.getPlayer(null, uuid.toString(), false) == null)
      {
        removal.put(uuid, Variable.cooldownsItemUsage.get(uuid));
      }
    }
    for (UUID uuid : removal.keySet())
    {
      Variable.cooldownsItemUsage.remove(uuid);
    }
  }
}
